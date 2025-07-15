const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const sqlite3 = require('sqlite3').verbose();
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(express.static('public'));

// Database setup
const dbPath = path.join(__dirname, 'foodtrucks.db');
const db = new sqlite3.Database(dbPath, (err) => {
    if (err) {
        console.error('Error opening database:', err.message);
    } else {
        console.log('Connected to SQLite database');
        createTables();
    }
});

// Create tables if they don't exist
function createTables() {
    const createTableSQL = `
        CREATE TABLE IF NOT EXISTS food_trucks (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            type TEXT NOT NULL,
            description TEXT,
            latitude REAL NOT NULL,
            longitude REAL NOT NULL,
            reportedBy TEXT NOT NULL,
            reportedAt TEXT NOT NULL,
            imageUrl TEXT,
            isActive BOOLEAN DEFAULT 1,
            createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    `;
    
    db.run(createTableSQL, (err) => {
        if (err) {
            console.error('Error creating table:', err.message);
        } else {
            console.log('Food trucks table ready');
            insertSampleData();
        }
    });
}

// Insert sample data
function insertSampleData() {
    const checkData = `SELECT COUNT(*) as count FROM food_trucks`;
    db.get(checkData, (err, row) => {
        if (err) {
            console.error('Error checking data:', err.message);
            return;
        }
        
        if (row.count === 0) {
            const sampleTrucks = [
                {
                    name: 'Mee Goreng Express',
                    type: 'Mee Goreng',
                    description: 'Authentic Malaysian Mee Goreng with fresh ingredients',
                    latitude: 3.1390,
                    longitude: 101.6869,
                    reportedBy: 'Ahmad',
                    reportedAt: new Date().toISOString()
                },
                {
                    name: 'Coffee on Wheels',
                    type: 'Coffee',
                    description: 'Premium coffee and fresh pastries',
                    latitude: 3.1500,
                    longitude: 101.7000,
                    reportedBy: 'Siti',
                    reportedAt: new Date().toISOString()
                },
                {
                    name: 'BBQ Paradise',
                    type: 'BBQ',
                    description: 'Grilled meat and vegetables with special sauce',
                    latitude: 3.1300,
                    longitude: 101.6900,
                    reportedBy: 'Kumar',
                    reportedAt: new Date().toISOString()
                },
                {
                    name: 'Dessert Dreams',
                    type: 'Dessert',
                    description: 'Traditional Malaysian desserts and ice cream',
                    latitude: 3.1450,
                    longitude: 101.6950,
                    reportedBy: 'Lisa',
                    reportedAt: new Date().toISOString()
                },
                {
                    name: 'Rojak King',
                    type: 'Rojak',
                    description: 'Fresh fruit and vegetable rojak',
                    latitude: 3.1380,
                    longitude: 101.6880,
                    reportedBy: 'Raj',
                    reportedAt: new Date().toISOString()
                }
            ];
            
            sampleTrucks.forEach(truck => {
                const insertSQL = `
                    INSERT INTO food_trucks (name, type, description, latitude, longitude, reportedBy, reportedAt, isActive)
                    VALUES (?, ?, ?, ?, ?, ?, ?, 1)
                `;
                db.run(insertSQL, [truck.name, truck.type, truck.description, truck.latitude, truck.longitude, truck.reportedBy, truck.reportedAt]);
            });
            
            console.log('Sample data inserted');
        }
    });
}

// API Routes

// Get all food trucks
app.get('/api/foodtrucks', (req, res) => {
    const sql = 'SELECT * FROM food_trucks ORDER BY reportedAt DESC';
    db.all(sql, [], (err, rows) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        res.json(rows);
    });
});

// Get active food trucks only
app.get('/api/foodtrucks/active', (req, res) => {
    const sql = 'SELECT * FROM food_trucks WHERE isActive = 1 ORDER BY reportedAt DESC';
    db.all(sql, [], (err, rows) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        res.json(rows);
    });
});

// Get food truck by ID
app.get('/api/foodtrucks/:id', (req, res) => {
    const sql = 'SELECT * FROM food_trucks WHERE id = ?';
    db.get(sql, [req.params.id], (err, row) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        if (row) {
            res.json(row);
        } else {
            res.status(404).json({ error: 'Food truck not found' });
        }
    });
});

// Create new food truck
app.post('/api/foodtrucks', (req, res) => {
    const { name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl } = req.body;
    
    if (!name || !type || !latitude || !longitude || !reportedBy) {
        res.status(400).json({ error: 'Missing required fields' });
        return;
    }
    
    const sql = `
        INSERT INTO food_trucks (name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, isActive)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)
    `;
    
    const params = [name, type, description, latitude, longitude, reportedBy, reportedAt || new Date().toISOString(), imageUrl || ''];
    
    db.run(sql, params, function(err) {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        
        // Return the created food truck
        db.get('SELECT * FROM food_trucks WHERE id = ?', [this.lastID], (err, row) => {
            if (err) {
                res.status(500).json({ error: err.message });
                return;
            }
            res.status(201).json(row);
        });
    });
});

// Update food truck
app.put('/api/foodtrucks/:id', (req, res) => {
    const { name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, isActive } = req.body;
    
    const sql = `
        UPDATE food_trucks 
        SET name = ?, type = ?, description = ?, latitude = ?, longitude = ?, 
            reportedBy = ?, reportedAt = ?, imageUrl = ?, isActive = ?
        WHERE id = ?
    `;
    
    const params = [name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, isActive, req.params.id];
    
    db.run(sql, params, function(err) {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        
        if (this.changes === 0) {
            res.status(404).json({ error: 'Food truck not found' });
            return;
        }
        
        // Return the updated food truck
        db.get('SELECT * FROM food_trucks WHERE id = ?', [req.params.id], (err, row) => {
            if (err) {
                res.status(500).json({ error: err.message });
                return;
            }
            res.json(row);
        });
    });
});

// Delete food truck
app.delete('/api/foodtrucks/:id', (req, res) => {
    const sql = 'DELETE FROM food_trucks WHERE id = ?';
    db.run(sql, [req.params.id], function(err) {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        
        if (this.changes === 0) {
            res.status(404).json({ error: 'Food truck not found' });
            return;
        }
        
        res.json({ message: 'Food truck deleted successfully' });
    });
});

// Serve admin interface
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ error: 'Something went wrong!' });
});

// Handle 404
app.use((req, res) => {
    res.status(404).json({ error: 'Route not found' });
});

// Start server
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
    console.log(`Admin interface available at http://localhost:${PORT}`);
    console.log(`API available at http://localhost:${PORT}/api/foodtrucks`);
});

// Graceful shutdown
process.on('SIGINT', () => {
    db.close((err) => {
        if (err) {
            console.error(err.message);
        }
        console.log('Database connection closed');
        process.exit(0);
    });
});

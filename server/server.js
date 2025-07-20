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

// Add cache-busting headers for HTML files
app.use((req, res, next) => {
    if (req.path.endsWith('.html') || req.path === '/') {
        res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
        res.setHeader('Pragma', 'no-cache');
        res.setHeader('Expires', '0');
    }
    next();
});

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
            area TEXT,
            landmark TEXT,
            streetAddress TEXT,
            operatingHours TEXT,
            contactNumber TEXT,
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
                // Central KL - Bukit Bintang Area
                {
                    name: 'Mee Goreng Express',
                    type: 'Fried Dishes',
                    description: 'Delicious traditional Mee Goreng',
                    latitude: 3.1390,
                    longitude: 101.6869,
                    reportedBy: 'Ahmad',
                    reportedAt: new Date().toISOString(),
                    area: 'Bukit Bintang',
                    landmark: 'Near Pavilion Mall',
                    streetAddress: 'Jalan Bukit Bintang',
                    operatingHours: '6:00 PM - 11:00 PM',
                    contactNumber: '012-3456789'
                },
                // KLCC Area
                {
                    name: 'Coffee on Wheels',
                    type: 'Coffee',
                    description: 'Fresh coffee and pastries',
                    latitude: 3.1500,
                    longitude: 101.7000,
                    reportedBy: 'Siti',
                    reportedAt: new Date().toISOString(),
                    area: 'KLCC',
                    landmark: 'Near Petronas Twin Towers',
                    streetAddress: 'Jalan Ampang',
                    operatingHours: '7:00 AM - 3:00 PM',
                    contactNumber: '012-3456790'
                },
                // Petaling Street Area
                {
                    name: 'BBQ Paradise',
                    type: 'Grilled / BBQ',
                    description: 'Grilled meat and vegetables',
                    latitude: 3.1300,
                    longitude: 101.6900,
                    reportedBy: 'Kumar',
                    reportedAt: new Date().toISOString(),
                    area: 'Petaling Street',
                    landmark: 'Near Central Market',
                    streetAddress: 'Jalan Petaling',
                    operatingHours: '6:00 PM - 12:00 AM',
                    contactNumber: '012-3456791'
                },
                // Bangsar Area
                {
                    name: 'Nasi Lemak King',
                    type: 'Traditional / Local',
                    description: 'Best Nasi Lemak in town',
                    latitude: 3.1450,
                    longitude: 101.6950,
                    reportedBy: 'Aminah',
                    reportedAt: new Date().toISOString(),
                    area: 'Bangsar',
                    landmark: 'Near Bangsar Village',
                    streetAddress: 'Jalan Telawi',
                    operatingHours: '6:00 AM - 10:00 AM',
                    contactNumber: '012-3456792'
                },
                // Damansara Area
                {
                    name: 'Dessert Delight',
                    type: 'Desserts & Sweets',
                    description: 'Sweet treats and cakes',
                    latitude: 3.1420,
                    longitude: 101.6920,
                    reportedBy: 'Lim',
                    reportedAt: new Date().toISOString(),
                    area: 'Damansara',
                    landmark: 'Near One Utama',
                    streetAddress: 'Jalan Bandar Utama',
                    operatingHours: '2:00 PM - 10:00 PM',
                    contactNumber: '012-3456793'
                },
                // Mont Kiara Area
                {
                    name: 'Drinks Hub',
                    type: 'Beverage',
                    description: 'Refreshing beverages',
                    latitude: 3.1480,
                    longitude: 101.6980,
                    reportedBy: 'Raj',
                    reportedAt: new Date().toISOString(),
                    area: 'Mont Kiara',
                    landmark: 'Near Solaris Dutamas',
                    streetAddress: 'Jalan Solaris',
                    operatingHours: '11:00 AM - 9:00 PM',
                    contactNumber: '012-3456794'
                },
                // Cheras Area
                {
                    name: 'Satay Master',
                    type: 'Grilled / BBQ',
                    description: 'Authentic Malaysian satay',
                    latitude: 3.0800,
                    longitude: 101.7200,
                    reportedBy: 'Hassan',
                    reportedAt: new Date().toISOString(),
                    area: 'Cheras',
                    landmark: 'Near Taman Connaught',
                    streetAddress: 'Jalan Cheras',
                    operatingHours: '5:00 PM - 11:00 PM',
                    contactNumber: '012-3456795'
                },
                // Kepong Area
                {
                    name: 'Rojak Delight',
                    type: 'Fruits',
                    description: 'Fresh fruit and vegetable rojak',
                    latitude: 3.2100,
                    longitude: 101.6400,
                    reportedBy: 'Mei Ling',
                    reportedAt: new Date().toISOString(),
                    area: 'Kepong',
                    landmark: 'Near Kepong Mall',
                    streetAddress: 'Jalan Kepong',
                    operatingHours: '4:00 PM - 10:00 PM',
                    contactNumber: '012-3456796'
                },
                // Wangsa Maju Area
                {
                    name: 'Ice Cream Paradise',
                    type: 'Desserts & Sweets',
                    description: 'Homemade ice cream varieties',
                    latitude: 3.1800,
                    longitude: 101.7500,
                    reportedBy: 'Sarah',
                    reportedAt: new Date().toISOString(),
                    area: 'Wangsa Maju',
                    landmark: 'Near Wangsa Walk',
                    streetAddress: 'Jalan Wangsa Delima',
                    operatingHours: '1:00 PM - 9:00 PM',
                    contactNumber: '012-3456797'
                },
                // Setapak Area
                {
                    name: 'Nasi Lemak Express',
                    type: 'Traditional / Local',
                    description: 'Traditional nasi lemak with sambal',
                    latitude: 3.2000,
                    longitude: 101.7000,
                    reportedBy: 'Zainab',
                    reportedAt: new Date().toISOString(),
                    area: 'Setapak',
                    landmark: 'Near TAR University',
                    streetAddress: 'Jalan Genting Klang',
                    operatingHours: '6:00 AM - 12:00 PM',
                    contactNumber: '012-3456798'
                },
                // Gombak Area
                {
                    name: 'Mamak Corner',
                    type: 'Street Food',
                    description: 'Indian Muslim cuisine',
                    latitude: 3.2500,
                    longitude: 101.6800,
                    reportedBy: 'Ravi',
                    reportedAt: new Date().toISOString(),
                    area: 'Gombak',
                    landmark: 'Near Gombak LRT',
                    streetAddress: 'Jalan Gombak',
                    operatingHours: '24 Hours',
                    contactNumber: '012-3456799'
                },
                // Selayang Area
                {
                    name: 'Durian King',
                    type: 'Fruits',
                    description: 'Fresh durian and local fruits',
                    latitude: 3.2800,
                    longitude: 101.6500,
                    reportedBy: 'Ah Chong',
                    reportedAt: new Date().toISOString(),
                    area: 'Selayang',
                    landmark: 'Near Selayang Mall',
                    streetAddress: 'Jalan Selayang',
                    operatingHours: '10:00 AM - 8:00 PM',
                    contactNumber: '012-3456800'
                },
                // Ampang Area
                {
                    name: 'Teh Tarik Corner',
                    type: 'Non-Coffee & Tea',
                    description: 'Traditional Malaysian teh tarik',
                    latitude: 3.1600,
                    longitude: 101.7600,
                    reportedBy: 'Khalid',
                    reportedAt: new Date().toISOString(),
                    area: 'Ampang',
                    landmark: 'Near Ampang Point',
                    streetAddress: 'Jalan Ampang',
                    operatingHours: '7:00 AM - 11:00 PM',
                    contactNumber: '012-3456801'
                },
                // Sri Hartamas Area
                {
                    name: 'Sushi Express',
                    type: 'Asian Cuisine',
                    description: 'Fresh sushi and Japanese cuisine',
                    latitude: 3.1700,
                    longitude: 101.6500,
                    reportedBy: 'Yuki',
                    reportedAt: new Date().toISOString(),
                    area: 'Sri Hartamas',
                    landmark: 'Near Hartamas Shopping Centre',
                    streetAddress: 'Jalan Sri Hartamas',
                    operatingHours: '11:00 AM - 10:00 PM',
                    contactNumber: '012-3456802'
                },
                // TTDI Area
                {
                    name: 'Pasta Mobile',
                    type: 'Western Food',
                    description: 'Italian pasta and pizza',
                    latitude: 3.1400,
                    longitude: 101.6400,
                    reportedBy: 'Marco',
                    reportedAt: new Date().toISOString(),
                    area: 'TTDI',
                    landmark: 'Near TTDI Park',
                    streetAddress: 'Jalan Tun Dr Ismail',
                    operatingHours: '6:00 PM - 11:00 PM',
                    contactNumber: '012-3456803'
                },
                // Mutiara Damansara Area
                {
                    name: 'Burger Joint',
                    type: 'Western Food',
                    description: 'Gourmet burgers and fries',
                    latitude: 3.1500,
                    longitude: 101.5800,
                    reportedBy: 'Mike',
                    reportedAt: new Date().toISOString(),
                    area: 'Mutiara Damansara',
                    landmark: 'Near The Curve',
                    streetAddress: 'Jalan PJU 7/3',
                    operatingHours: '12:00 PM - 12:00 AM',
                    contactNumber: '012-3456804'
                },
                // Bandar Utama Area
                {
                    name: 'Smoothie Bar',
                    type: 'Beverage',
                    description: 'Fresh fruit smoothies',
                    latitude: 3.1400,
                    longitude: 101.5900,
                    reportedBy: 'Lisa',
                    reportedAt: new Date().toISOString(),
                    area: 'Bandar Utama',
                    landmark: 'Near 1 Utama',
                    streetAddress: 'Jalan Bandar Utama',
                    operatingHours: '10:00 AM - 8:00 PM',
                    contactNumber: '012-3456805'
                },
                // Sunway Area
                {
                    name: 'Korean BBQ',
                    type: 'Grilled / BBQ',
                    description: 'Korean barbecue and kimchi',
                    latitude: 3.0700,
                    longitude: 101.6000,
                    reportedBy: 'Ji Eun',
                    reportedAt: new Date().toISOString(),
                    area: 'Sunway',
                    landmark: 'Near Sunway Pyramid',
                    streetAddress: 'Jalan PJS 11/15',
                    operatingHours: '5:00 PM - 11:00 PM',
                    contactNumber: '012-3456806'
                },
                // Puchong Area
                {
                    name: 'Dim Sum Express',
                    type: 'Asian Cuisine',
                    description: 'Steamed dim sum and tea',
                    latitude: 3.0200,
                    longitude: 101.6200,
                    reportedBy: 'Wong',
                    reportedAt: new Date().toISOString(),
                    area: 'Puchong',
                    landmark: 'Near IOI Mall Puchong',
                    streetAddress: 'Jalan Puchong',
                    operatingHours: '7:00 AM - 3:00 PM',
                    contactNumber: '012-3456807'
                },
                // Seri Kembangan Area
                {
                    name: 'Roti Canai Master',
                    type: 'Traditional / Local',
                    description: 'Flaky roti canai with curry',
                    latitude: 3.0300,
                    longitude: 101.7000,
                    reportedBy: 'Maniam',
                    reportedAt: new Date().toISOString(),
                    area: 'Seri Kembangan',
                    landmark: 'Near South City Plaza',
                    streetAddress: 'Jalan Serdang',
                    operatingHours: '6:00 AM - 2:00 PM',
                    contactNumber: '012-3456808'
                },
                // Kajang Area
                {
                    name: 'Satay Kajang',
                    type: 'Grilled / BBQ',
                    description: 'Famous Kajang satay',
                    latitude: 2.9900,
                    longitude: 101.7900,
                    reportedBy: 'Pak Mat',
                    reportedAt: new Date().toISOString(),
                    area: 'Kajang',
                    landmark: 'Near Kajang Town Centre',
                    streetAddress: 'Jalan Kajang',
                    operatingHours: '5:00 PM - 12:00 AM',
                    contactNumber: '012-3456809'
                },
                // Semenyih Area
                {
                    name: 'Durian Farm',
                    type: 'Fruits',
                    description: 'Fresh durian from farm',
                    latitude: 2.9500,
                    longitude: 101.8500,
                    reportedBy: 'Ah Seng',
                    reportedAt: new Date().toISOString(),
                    area: 'Semenyih',
                    landmark: 'Near Semenyih Town',
                    streetAddress: 'Jalan Semenyih',
                    operatingHours: '9:00 AM - 6:00 PM',
                    contactNumber: '012-3456810'
                },
                // Cyberjaya Area
                {
                    name: 'Tech Food Hub',
                    type: 'Asian Cuisine',
                    description: 'Modern fusion cuisine',
                    latitude: 2.9200,
                    longitude: 101.6500,
                    reportedBy: 'Alex',
                    reportedAt: new Date().toISOString(),
                    area: 'Cyberjaya',
                    landmark: 'Near Cyberjaya Town Centre',
                    streetAddress: 'Persiaran Cyberjaya',
                    operatingHours: '11:00 AM - 9:00 PM',
                    contactNumber: '012-3456811'
                },
                // Putrajaya Area
                {
                    name: 'Government Canteen',
                    type: 'Traditional / Local',
                    description: 'Local Malaysian cuisine',
                    latitude: 2.9400,
                    longitude: 101.6900,
                    reportedBy: 'Ahmad',
                    reportedAt: new Date().toISOString(),
                    area: 'Putrajaya',
                    landmark: 'Near Putrajaya Centre',
                    streetAddress: 'Jalan Putrajaya',
                    operatingHours: '7:00 AM - 5:00 PM',
                    contactNumber: '012-3456812'
                },
                // Shah Alam Area
                {
                    name: 'Shah Alam Delights',
                    type: 'Traditional / Local',
                    description: 'Local Shah Alam specialties',
                    latitude: 3.0800,
                    longitude: 101.5200,
                    reportedBy: 'Siti',
                    reportedAt: new Date().toISOString(),
                    area: 'Shah Alam',
                    landmark: 'Near Shah Alam City Centre',
                    streetAddress: 'Jalan Shah Alam',
                    operatingHours: '6:00 PM - 11:00 PM',
                    contactNumber: '012-3456813'
                }
            ];
            
            sampleTrucks.forEach(truck => {
                const insertSQL = `
                    INSERT INTO food_trucks (name, type, description, latitude, longitude, reportedBy, reportedAt, area, landmark, streetAddress, operatingHours, contactNumber, isActive)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)
                `;
                db.run(insertSQL, [truck.name, truck.type, truck.description, truck.latitude, truck.longitude, truck.reportedBy, truck.reportedAt, truck.area, truck.landmark, truck.streetAddress, truck.operatingHours, truck.contactNumber]);
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
    const { name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, area, landmark, streetAddress, operatingHours, contactNumber } = req.body;
    
    if (!name || !type || !latitude || !longitude || !reportedBy) {
        res.status(400).json({ error: 'Missing required fields' });
        return;
    }
    
    const sql = `
        INSERT INTO food_trucks (name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, area, landmark, streetAddress, operatingHours, contactNumber, isActive)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)
    `;
    
    const params = [name, type, description, latitude, longitude, reportedBy, reportedAt || new Date().toISOString(), imageUrl || '', area || '', landmark || '', streetAddress || '', operatingHours || '', contactNumber || ''];
    
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
    const { name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, area, landmark, streetAddress, operatingHours, contactNumber, isActive } = req.body;
    
    const sql = `
        UPDATE food_trucks 
        SET name = ?, type = ?, description = ?, latitude = ?, longitude = ?, 
            reportedBy = ?, reportedAt = ?, imageUrl = ?, area = ?, landmark = ?, 
            streetAddress = ?, operatingHours = ?, contactNumber = ?, isActive = ?
        WHERE id = ?
    `;
    
    const params = [name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, area, landmark, streetAddress, operatingHours, contactNumber, isActive, req.params.id];
    
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

const sqlite3 = require('sqlite3').verbose();
const axios = require('axios');
const path = require('path');

// Configuration
const FRIEND_SERVER_URL = 'http://FRIEND_IP:3000'; // Replace with your friend's IP
const dbPath = path.join(__dirname, 'foodtrucks.db');

async function syncDatabase() {
    try {
        console.log('Fetching data from friend\'s server...');
        
        // Get all food trucks from friend's server
        const response = await axios.get(`${FRIEND_SERVER_URL}/api/foodtrucks`);
        const foodTrucks = response.data;
        
        console.log(`Found ${foodTrucks.length} food trucks to sync`);
        
        // Open local database
        const db = new sqlite3.Database(dbPath);
        
        // Clear existing data (optional - remove this line to keep existing data)
        db.run('DELETE FROM food_trucks', (err) => {
            if (err) {
                console.error('Error clearing database:', err);
                return;
            }
            console.log('Cleared existing data');
        });
        
        // Insert new data
        const insertStmt = db.prepare(`
            INSERT INTO food_trucks 
            (name, type, description, latitude, longitude, reportedBy, reportedAt, imageUrl, isActive)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        `);
        
        foodTrucks.forEach(truck => {
            insertStmt.run([
                truck.name,
                truck.type,
                truck.description,
                truck.latitude,
                truck.longitude,
                truck.reportedBy,
                truck.reportedAt,
                truck.imageUrl,
                truck.isActive
            ]);
        });
        
        insertStmt.finalize();
        db.close();
        
        console.log('Database sync completed successfully!');
        
    } catch (error) {
        console.error('Error syncing database:', error.message);
    }
}

// Run sync
syncDatabase();

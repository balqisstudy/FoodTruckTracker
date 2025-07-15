# Food Truck Tracker Server

## Quick Start

1. **Install Node.js** (if not already installed):
   - Download from https://nodejs.org/
   - Version 16.x or later required

2. **Install dependencies**:
   ```bash
   cd server
   npm install
   ```

3. **Start the server**:
   ```bash
   npm start
   ```

4. **Access the admin interface**:
   - Open browser: http://localhost:3000
   - API endpoint: http://localhost:3000/api/foodtrucks

## For Development
```bash
npm run dev  # Uses nodemon for auto-restart
```

## API Testing
Test the API with curl:
```bash
# Get all food trucks
curl http://localhost:3000/api/foodtrucks

# Add a new food truck
curl -X POST http://localhost:3000/api/foodtrucks \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Truck",
    "type": "Coffee",
    "description": "Test description",
    "latitude": 3.1390,
    "longitude": 101.6869,
    "reportedBy": "Test User"
  }'
```

The server will automatically create a SQLite database with sample data on first run.

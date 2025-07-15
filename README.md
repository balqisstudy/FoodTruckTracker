# Food Truck Tracker

A crowdsourced mobile application for tracking food truck locations in real-time. This Android application allows users to view food truck locations on Google Maps and report new food truck sightings.

## Features

### Mobile Application
- **Real-time Food Truck Map**: Display food trucks on Google Maps with custom markers
- **Community Reporting**: Users can report food truck locations with details
- **Food Truck Information**: Shows truck type, reporter, and timestamp
- **Location Services**: Get current location for easy reporting
- **Custom Markers**: Different colored markers for different food truck types
- **About Page**: Developer information and clickable GitHub URL

### Server-Side Web Application
- **Administrative Interface**: Web-based admin panel for managing food trucks
- **CRUD Operations**: Create, Read, Update, Delete food truck information
- **RESTful API**: Complete API for mobile app integration
- **Statistics Dashboard**: View statistics about food trucks and reports
- **SQLite Database**: Lightweight database for storing food truck data

## Technology Stack

### Mobile App
- **Platform**: Android
- **Language**: Java
- **UI**: Material Design Components
- **Maps**: Google Maps API
- **Networking**: Retrofit + OkHttp
- **Location**: Google Play Services Location
- **Architecture**: MVC pattern

### Server
- **Runtime**: Node.js
- **Framework**: Express.js
- **Database**: SQLite
- **API**: RESTful JSON API
- **Frontend**: HTML5, CSS3, Vanilla JavaScript

## Project Structure

```
FoodTruckTracker2/
├── app/                        # Android application
│   ├── src/main/
│   │   ├── java/com/example/foodtrucktracker/
│   │   │   ├── models/         # Data models
│   │   │   ├── network/        # API services
│   │   │   ├── utils/          # Utility classes
│   │   │   ├── MainActivity.java
│   │   │   ├── MapsActivity.java
│   │   │   ├── InsertDataActivity.java
│   │   │   └── AboutActivity.java
│   │   └── res/                # Resources (layouts, colors, strings)
│   └── build.gradle.kts
├── server/                     # Node.js server
│   ├── public/                 # Web admin interface
│   ├── server.js              # Main server file
│   ├── package.json
│   └── foodtrucks.db          # SQLite database (auto-created)
└── README.md
```

## Setup Instructions

### Prerequisites
- Android Studio 2024.1.1 or later
- Android SDK API 30 or higher
- Node.js 16.x or later
- Google Maps API Key

### Mobile App Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/FoodTruckTracker2.git
   cd FoodTruckTracker2
   ```

2. **Get Google Maps API Key**:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing one
   - Enable Maps SDK for Android
   - Create credentials (API Key)
   - Restrict the key to Android apps

3. **Configure API Key**:
   - Open `app/src/main/AndroidManifest.xml`
   - Replace `YOUR_API_KEY` with your actual Google Maps API key

4. **Update Server URL**:
   - Open `app/src/main/java/com/example/foodtrucktracker/network/ApiClient.java`
   - Update `BASE_URL` to your server's URL
   - For emulator: `http://10.0.2.2:3000/api/`
   - For real device: `http://your-server-ip:3000/api/`

5. **Build and Run**:
   - Open project in Android Studio
   - Sync project with Gradle files
   - Run on emulator or device

### Server Setup

1. **Navigate to server directory**:
   ```bash
   cd server
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Start the server**:
   ```bash
   npm start
   ```
   or for development:
   ```bash
   npm run dev
   ```

4. **Access Admin Interface**:
   - Open browser and go to `http://localhost:3000`
   - Use the web interface to manage food trucks

## API Endpoints

### Food Trucks
- `GET /api/foodtrucks` - Get all food trucks
- `GET /api/foodtrucks/active` - Get active food trucks only
- `GET /api/foodtrucks/:id` - Get specific food truck
- `POST /api/foodtrucks` - Create new food truck
- `PUT /api/foodtrucks/:id` - Update food truck
- `DELETE /api/foodtrucks/:id` - Delete food truck

### Data Format
```json
{
  "id": 1,
  "name": "Mee Goreng Express",
  "type": "Mee Goreng",
  "description": "Authentic Malaysian Mee Goreng",
  "latitude": 3.1390,
  "longitude": 101.6869,
  "reportedBy": "Ahmad",
  "reportedAt": "2025-01-15T10:30:00.000Z",
  "imageUrl": "",
  "isActive": true
}
```

## Food Truck Types
- Mee Goreng
- Coffee
- BBQ
- Dessert
- Drinks
- Nasi Lemak
- Rojak
- Satay
- Ice Cream
- Other

## Screenshots
[Add screenshots of your app here]

## Demo Video
[Add link to your YouTube demonstration video]

## Developer Information

### Team Members
- **Developer 1**: [Your Name]
  - Student ID: [Your Student ID]
  - Programme: [Your Programme Code]
  - Email: [your.email@example.com]

- **Developer 2**: [Partner Name]
  - Student ID: [Partner Student ID]
  - Programme: [Partner Programme Code]
  - Email: [partner.email@example.com]

### Group Information
- **Group**: [Your Group Number]
- **Course**: Mobile Application Development
- **Institution**: [Your Institution]

## Target Users and Context

### Target Users
1. **Food Enthusiasts**: People looking for diverse food options from mobile vendors
2. **Local Residents**: Community members wanting to discover nearby food trucks
3. **Tourists**: Visitors exploring local street food culture
4. **Food Truck Operators**: Vendors wanting to increase visibility

### Mobile User Characteristics
- **On-the-go Usage**: Users need quick access to location information
- **Real-time Requirements**: Current location data is essential
- **Visual Information**: Map-based interface for spatial understanding
- **Community Participation**: Users contribute by reporting sightings

### Mobile Device Characteristics
- **Location Services**: GPS for current location and navigation
- **Network Connectivity**: Internet access for real-time data
- **Touch Interface**: Intuitive map interaction and form input
- **Screen Size Optimization**: Responsive design for various device sizes

## Design Principles

### Multiple Screen Support
- **Responsive Layouts**: Adapts to different screen sizes and orientations
- **Material Design**: Consistent visual language across components
- **Touch-Friendly**: Minimum touch target sizes (48dp)
- **Accessibility**: Proper content descriptions and contrast ratios

### Error Handling
- **Network Errors**: Graceful fallback to cached or sample data
- **Permission Handling**: Clear explanations for location permissions
- **Input Validation**: Form validation with user-friendly error messages
- **Loading States**: Progress indicators for network operations

## Future Enhancements
- Push notifications for nearby food trucks
- User reviews and ratings
- Photo uploads for food truck verification
- Integration with social media platforms
- Offline map caching
- Advanced filtering and search options

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Support
For questions or support, please contact the development team or create an issue on GitHub.

---

© 2025 Food Truck Tracker Team. All rights reserved.

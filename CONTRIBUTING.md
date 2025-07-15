# Contributing to Food Truck Tracker

Thank you for contributing to the Food Truck Tracker project! This document provides guidelines for team collaboration.

## Team Structure

Our team is organized as follows:
- **Mobile Developer**: Android app development
- **Backend Developer**: Server and API development  
- **UI/UX Designer**: Design and user experience
- **Project Manager**: Documentation, testing, and coordination

## Getting Started

### Prerequisites
- Android Studio 2024.1.1+
- Node.js 16.x+
- Git
- Google Maps API Key

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/balqisstudy/FoodTruckTracker.git
   cd FoodTruckTracker
   ```

2. Install server dependencies:
   ```bash
   cd server
   npm install
   ```

3. Set up your Google Maps API key in `app/src/main/AndroidManifest.xml`

## Development Workflow

### Branch Strategy
- `main`: Production-ready code
- `develop`: Integration branch for features
- `feature/feature-name`: Individual feature development
- `hotfix/fix-name`: Critical bug fixes

### Making Changes

1. **Create a feature branch:**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following our coding standards

3. **Commit your changes:**
   ```bash
   git add .
   git commit -m "Add: brief description of changes"
   ```

4. **Push to your branch:**
   ```bash
   git push origin feature/your-feature-name
   ```

5. **Create a Pull Request** on GitHub

### Commit Message Format
Use the following format for commit messages:
```
Type: Brief description

Detailed explanation if needed
```

Types:
- `Add`: New features or files
- `Fix`: Bug fixes
- `Update`: Modifications to existing features
- `Remove`: Deletion of code or files
- `Docs`: Documentation changes
- `Style`: Code formatting changes
- `Refactor`: Code refactoring

## Code Standards

### Android (Java)
```java
// Use descriptive variable names
private List<FoodTruck> activeFoodTrucks;

// Add comments for complex logic
/**
 * Loads food trucks from server and displays them on map
 * Falls back to sample data if server is unavailable
 */
private void loadFoodTrucks() {
    // Implementation
}

// Follow naming conventions
public class FoodTruckManager {
    private static final String TAG = "FoodTruckManager";
    private ApiService apiService;
}
```

### Server (JavaScript)
```javascript
// Use consistent indentation (2 spaces)
app.get('/api/foodtrucks', (req, res) => {
  // Use descriptive variable names
  const activeTrucks = trucks.filter(truck => truck.isActive);
  
  // Add error handling
  try {
    res.json(activeTrucks);
  } catch (error) {
    console.error('Error fetching trucks:', error);
    res.status(500).json({ error: 'Server error' });
  }
});
```

## File Organization

### Mobile App Structure
```
app/src/main/java/com/example/foodtrucktracker/
├── models/          # Data models
├── network/         # API services
├── utils/           # Utility classes
├── MainActivity.java
├── MapsActivity.java
├── InsertDataActivity.java
└── AboutActivity.java
```

### Server Structure
```
server/
├── server.js        # Main server file
├── public/          # Web admin interface
├── package.json     # Dependencies
└── README.md        # Server documentation
```

## Testing Guidelines

### Mobile App Testing
- Test on both emulator and physical device
- Test different screen sizes and orientations
- Test with and without internet connection
- Test location permissions on different Android versions

### Server Testing
- Test all API endpoints with Postman or curl
- Test error scenarios (invalid data, network issues)
- Test admin interface in multiple browsers

### Manual Testing Checklist
- [ ] App launches successfully
- [ ] Map displays correctly
- [ ] Food truck markers appear
- [ ] Location services work
- [ ] Add new truck form works
- [ ] About page displays correctly
- [ ] Server API responds correctly
- [ ] Admin interface functions properly

## Pull Request Process

1. **Before submitting:**
   - [ ] Code follows our style guidelines
   - [ ] All tests pass
   - [ ] Documentation is updated
   - [ ] No merge conflicts with main branch

2. **Pull Request Requirements:**
   - Descriptive title and description
   - Link to related issues
   - Screenshots for UI changes
   - List of changes made

3. **Review Process:**
   - At least one team member must review
   - Address all review comments
   - Ensure CI checks pass

## Issue Reporting

When creating issues, use our templates:
- **Bug Report**: For reporting bugs
- **Feature Request**: For suggesting new features
- **Task**: For general development tasks

Include:
- Clear description
- Steps to reproduce (for bugs)
- Expected vs actual behavior
- Screenshots if applicable
- Environment details

## Communication

### Team Communication
- Use GitHub Issues for task tracking
- Use Pull Request comments for code discussion
- Use GitHub Discussions for general team communication

### Code Review Guidelines
- Be constructive and respectful
- Focus on code quality and functionality
- Suggest improvements, don't just point out problems
- Test the changes locally when possible

## Documentation

### Code Documentation
- Comment complex algorithms
- Document public APIs
- Update README.md for significant changes
- Include JSDoc for JavaScript functions

### Project Documentation
- Keep README.md updated
- Document setup procedures
- Include troubleshooting guides
- Document API endpoints

## Release Process

1. All features merged to `develop` branch
2. Testing phase on `develop` branch
3. Create release branch from `develop`
4. Final testing and bug fixes
5. Merge to `main` and tag release
6. Update documentation

## Getting Help

If you need help:
1. Check existing documentation
2. Search existing issues
3. Ask in GitHub Discussions
4. Contact team members directly

## Code of Conduct

- Be respectful and professional
- Help team members learn and grow
- Share knowledge and resources
- Communicate clearly and promptly
- Take responsibility for your contributions

Thank you for contributing to our Food Truck Tracker project!

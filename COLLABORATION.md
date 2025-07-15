# GitHub Collaboration Guide for Food Truck Tracker

## Team Setup and Responsibilities

Based on your assignment requirements, you should form a group of 3-4 students with divided responsibilities:

### Recommended Team Structure:
1. **Mobile Application Developer** - Android app development
2. **Web Application Developer** - Server-side and admin interface
3. **UI/UX Designer & Tester** - Design, testing, and documentation
4. **Project Manager & Presenter** - Video creation, coordination, and report writing

## GitHub Collaboration Setup

### 1. Repository Setup

**Repository Owner (Team Lead):**
```bash
# Create repository on GitHub first, then:
git init
git add .
git commit -m "Initial Food Truck Tracker project setup"
git branch -M main
git remote add origin https://github.com/yourusername/FoodTruckTracker2.git
git push -u origin main
```

**Team Members:**
```bash
# Clone the repository
git clone https://github.com/yourusername/FoodTruckTracker2.git
cd FoodTruckTracker2
```

### 2. Branch Strategy

Create feature branches for different components:

```bash
# Mobile app development
git checkout -b feature/mobile-app
git checkout -b feature/maps-integration
git checkout -b feature/location-services

# Server development
git checkout -b feature/server-api
git checkout -b feature/admin-interface
git checkout -b feature/database-setup

# UI/Design
git checkout -b feature/ui-design
git checkout -b feature/responsive-layouts

# Documentation
git checkout -b feature/documentation
git checkout -b feature/testing
```

### 3. Collaboration Workflow

**Daily Workflow:**
```bash
# 1. Start of day - get latest changes
git checkout main
git pull origin main

# 2. Create/switch to your feature branch
git checkout feature/your-feature-name

# 3. Make your changes and commit frequently
git add .
git commit -m "Add food truck marker functionality"

# 4. Push your branch
git push origin feature/your-feature-name

# 5. Create Pull Request on GitHub
# 6. Team review and merge
```

**Before starting work each day:**
```bash
git checkout main
git pull origin main
git checkout your-feature-branch
git merge main  # or git rebase main
```

### 4. File Organization for Team Collaboration

```
FoodTruckTracker2/
├── app/                          # Mobile App (Developer 1)
│   ├── src/main/java/
│   └── src/main/res/
├── server/                       # Backend (Developer 2)
│   ├── server.js
│   ├── public/
│   └── package.json
├── docs/                         # Documentation (Developer 3)
│   ├── user-guide.md
│   ├── api-documentation.md
│   └── screenshots/
├── design/                       # UI/UX Assets (Developer 3)
│   ├── mockups/
│   ├── color-schemes/
│   └── icons/
├── testing/                      # Test Cases (Developer 4)
│   ├── test-cases.md
│   └── bug-reports/
└── video/                        # Demo Video (Developer 4)
    ├── script.md
    └── final-demo.mp4
```

## Team Collaboration Best Practices

### 1. Code Review Process
- All changes must go through Pull Requests
- At least one team member must review before merging
- Use GitHub's review features for feedback

### 2. Issue Tracking
Create GitHub Issues for:
- Feature development tasks
- Bug reports
- Documentation needs
- Design requirements

### 3. Communication
- Use GitHub Discussions for team communication
- Comment on Pull Requests for code feedback
- Use project boards for task management

### 4. Commit Message Guidelines
```bash
# Good commit messages:
git commit -m "Add: Google Maps integration with custom markers"
git commit -m "Fix: Location permission handling on Android 12+"
git commit -m "Update: API endpoint for food truck creation"
git commit -m "Docs: Add server setup instructions"
```

## Individual Responsibilities

### Developer 1 - Mobile Application
**Files to focus on:**
- `app/src/main/java/com/example/foodtrucktracker/`
- `app/src/main/res/`
- `app/build.gradle.kts`

**Tasks:**
- Android activities and fragments
- Google Maps integration
- Location services
- API integration with Retrofit
- UI components and layouts

### Developer 2 - Server & Web Application
**Files to focus on:**
- `server/server.js`
- `server/public/`
- `server/package.json`

**Tasks:**
- RESTful API development
- Database design and management
- Admin web interface
- Server deployment
- API documentation

### Developer 3 - UI/UX & Testing
**Files to focus on:**
- `app/src/main/res/layout/`
- `app/src/main/res/values/`
- `docs/`
- `design/`

**Tasks:**
- UI/UX design and mockups
- Responsive layout implementation
- Color schemes and branding
- User testing and feedback
- Design documentation

### Developer 4 - Project Management & Documentation
**Files to focus on:**
- `README.md`
- `docs/`
- `testing/`
- `video/`

**Tasks:**
- Project documentation
- Test case creation
- Video script and recording
- Project coordination
- Final report writing

## GitHub Project Management

### 1. Set up GitHub Project Board
Create columns:
- **Backlog** - Features to implement
- **In Progress** - Currently being worked on
- **Review** - Pull requests waiting for review
- **Testing** - Features being tested
- **Done** - Completed features

### 2. Use GitHub Issues Templates
Create issue templates for:
- Feature requests
- Bug reports
- Documentation updates
- Testing tasks

### 3. Milestone Planning
Create milestones for:
- Week 1: Project setup and basic structure
- Week 2: Core functionality implementation
- Week 3: Integration and testing
- Week 4: Final testing and video creation

## Merge Conflict Resolution

When conflicts occur:
```bash
# 1. Pull latest changes
git pull origin main

# 2. Resolve conflicts in files
# Edit files to fix conflicts (remove <<<, ===, >>> markers)

# 3. Add resolved files
git add .

# 4. Complete the merge
git commit -m "Resolve merge conflicts"

# 5. Push changes
git push origin your-branch-name
```

## Code Quality Guidelines

### 1. Android Code Standards
- Follow Java naming conventions
- Add comments for complex logic
- Use meaningful variable names
- Implement proper error handling

### 2. Server Code Standards
- Use consistent indentation (2 spaces)
- Add JSDoc comments for functions
- Implement proper error responses
- Follow RESTful API conventions

### 3. Testing Requirements
- Test on multiple devices/screen sizes
- Test network error scenarios
- Validate all form inputs
- Test location permissions

## Final Submission Checklist

### Individual Contributions
Each team member should document their contributions in:
```markdown
## My Contributions - [Your Name]
- **Files Modified:** [List of files]
- **Features Implemented:** [List of features]
- **Commits:** [Number of commits with examples]
- **Issues Resolved:** [GitHub issue numbers]
```

### Before Submission
- [ ] All features working correctly
- [ ] Code reviewed and merged to main branch
- [ ] Documentation complete
- [ ] Demo video recorded and uploaded
- [ ] GitHub repository is public
- [ ] README.md updated with team information
- [ ] All team members listed in About page

## GitHub Repository Template

When you create your repository, include these files:
- `README.md` (comprehensive project documentation)
- `CONTRIBUTING.md` (guidelines for team collaboration)
- `.gitignore` (ignore build files, API keys, etc.)
- `LICENSE` (choose appropriate license)
- Issue templates in `.github/ISSUE_TEMPLATE/`
- Pull request template in `.github/PULL_REQUEST_TEMPLATE.md`

This collaborative approach will help your team work efficiently together and create a professional-quality project that demonstrates excellent teamwork and development practices!

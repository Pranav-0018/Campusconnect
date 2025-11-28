# Campus Connect - University Platform

A modern Java Swing desktop application that connects students and teachers in a university environment.

## Features

### 🔐 Authentication System
- Role-based login (Teacher/Student)
- Secure credential validation
- Modern gradient UI design

### 📱 Dashboard Features
- **Feed Page**: Social media-style posts with teacher identification (⭐)
- **Events Page**: Teachers create events, students view them
- **Query Page**: Students ask questions, teachers provide answers
- **Profile Page**: Editable user profiles with role-specific fields

### 👥 User Roles

#### Teachers ⭐
- Create and manage events/hackathons
- Reply to student queries
- Post in social feed
- Distinguished with red star (⭐) throughout the app

#### Students
- View events and hackathons
- Ask questions to teachers
- Post in social feed
- Manage personal profile

## Technology Stack

- **Frontend**: Java Swing with custom styling
- **Backend**: MySQL Database
- **Architecture**: MVC Pattern
- **Database Connectivity**: JDBC

## Project Structure

```
CampusConnect/
├── database/
│   └── DBConnection.java          # Database connection management
├── models/
│   ├── User.java                  # User entity
│   ├── Post.java                  # Post entity
│   ├── Event.java                 # Event entity
│   └── Query.java                 # Query entity
├── services/
│   ├── UserService.java           # User-related operations
│   ├── PostService.java           # Post-related operations
│   ├── EventService.java          # Event-related operations
│   └── QueryService.java          # Query-related operations
├── ui/
│   ├── LoginPage.java             # Login interface
│   ├── Dashboard.java             # Main dashboard
│   ├── FeedPage.java              # Social feed
│   ├── EventsPage.java            # Events management
│   ├── QueryPage.java             # Q&A system
│   └── ProfilePage.java           # User profiles
├── Main.java                      # Application entry point
├── campus_connect.sql             # Database schema
└── README.md                      # This file
```

## Setup Instructions

### Prerequisites
- Java JDK 8 or higher
- MySQL Server 5.7 or higher
- MySQL JDBC Driver (mysql-connector-java)

### Database Setup
1. Install MySQL Server
2. Run the SQL script:
   ```bash
   mysql -u root -p < campus_connect.sql
   ```
3. Update database credentials in `database/DBConnection.java`:
   ```java
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

### Running the Application
1. Compile all Java files:
   ```bash
   javac -cp ".:mysql-connector-java.jar" *.java */*.java
   ```
2. Run the main class:
   ```bash
   java -cp ".:mysql-connector-java.jar" Main
   ```

### Demo Accounts
The application includes demo accounts for testing:

**Teacher Account:**
- Email: `teacher@demo.com`
- Password: `teacher123`

**Student Account:**
- Email: `student@demo.com`
- Password: `student123`

## Database Schema

### Tables
- **users**: User authentication and basic info
- **posts**: Social media posts
- **events**: Events and hackathons
- **queries**: Student questions and teacher answers
- **profiles**: Extended user profile information

### Key Features
- Foreign key relationships for data integrity
- Timestamps for all records
- Indexed columns for better performance
- Sample data included

## UI Design Features

### Modern Styling
- Purple-blue gradient backgrounds
- Rounded corners and shadows
- Hover effects on buttons
- Responsive layout design

### User Experience
- Intuitive navigation with tabbed interface
- Real-time data updates
- Form validation and error handling
- Success/error message notifications

### Teacher Identification
- Red star (⭐) appears next to teacher names
- Special styling for teacher posts and replies
- Role-based feature access

## Development Notes

### Architecture Patterns
- **MVC Pattern**: Clear separation of concerns
- **Service Layer**: Business logic abstraction
- **DAO Pattern**: Database access abstraction

### Code Quality
- Comprehensive error handling
- Input validation
- Memory-efficient database connections
- Clean, readable code structure

### Extensibility
- Modular design for easy feature additions
- Configurable database connections
- Scalable UI components

## Future Enhancements

- [ ] File upload functionality for posts
- [ ] Real-time notifications
- [ ] Advanced search and filtering
- [ ] Email notifications for queries
- [ ] Mobile-responsive web version
- [ ] Integration with university systems

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL server is running
   - Check credentials in DBConnection.java
   - Ensure database exists and is accessible

2. **ClassNotFoundException**
   - Add MySQL JDBC driver to classpath
   - Verify driver JAR file location

3. **UI Display Issues**
   - Update Java to latest version
   - Check system look and feel compatibility

### Demo Mode
If database connection fails, the application automatically switches to demo mode with hardcoded credentials for testing the UI.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is developed for educational purposes. Feel free to use and modify as needed.

---

**Campus Connect** - Connecting minds, building futures! 🎓✨

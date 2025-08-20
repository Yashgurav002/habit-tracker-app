# Habit Tracker API
## Refer how_to_use_habit_tracker_api.md file for full detailed instructions

A comprehensive REST API for habit tracking built with Spring Boot, MySQL, and JWT authentication.

## Features

- **User Authentication**: JWT-based secure authentication
- **Habit Management**: Create, read, update, and delete habits
- **Habit Tracking**: Log habit completions with dates and progress
- **Analytics**: Track streaks, completion rates, and progress statistics
- **Data Validation**: Input validation and error handling
- **Database Integration**: MySQL with JPA/Hibernate
- **Security**: Secure endpoints with role-based access control

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security 6**
- **Spring Data JPA**
- **MySQL 8.0**
- **JWT (JSON Web Tokens)**
- **Maven**
- **BCrypt Password Encoding**

## Database Setup

1. Install MySQL 8.0 or higher
2. Create a database named `habit_tracker`
3. Update `application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/habit_tracker?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. **Clone the repository**
2. **Navigate to the project directory**
3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
4. **The API will be available at:** `http://localhost:8080/api`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - User login

### Habits
- `GET /api/habits` - Get all user habits
- `POST /api/habits` - Create a new habit
- `GET /api/habits/{id}` - Get habit by ID
- `PUT /api/habits/{id}` - Update habit
- `DELETE /api/habits/{id}` - Delete habit

### Habit Entries
- `POST /api/habits/{id}/entries` - Log habit completion
- `GET /api/habits/{id}/entries` - Get habit entries
- `PUT /api/habits/{id}/entries/{entryId}` - Update habit entry
- `DELETE /api/habits/{id}/entries/{entryId}` - Delete habit entry

### Analytics
- `GET /api/analytics` - Get user analytics and statistics

## Request/Response Examples

### Register User
```json
POST /api/auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Login
```json
POST /api/auth/login
{
  "usernameOrEmail": "john_doe",
  "password": "password123"
}
```

### Create Habit
```json
POST /api/habits
{
  "name": "Morning Exercise",
  "description": "30 minutes of cardio",
  "frequencyType": "DAILY",
  "targetCount": 1
}
```

### Log Habit Entry
```json
POST /api/habits/1/entries
{
  "entryDate": "2024-07-17",
  "completionCount": 1,
  "isCompleted": true,
  "notes": "Great workout today!"
}
```

## Security

- All endpoints except `/api/auth/**` require authentication
- JWT token must be included in the Authorization header: `Bearer <token>`
- Passwords are encrypted using BCrypt
- CORS is configured for cross-origin requests

## Testing

Run tests with:
```bash
mvn test
```

## Configuration

Key configuration properties in `application.properties`:
- Server port: `8080`
- Database connection settings
- JWT secret and expiration time
- Logging levels

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

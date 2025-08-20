# Habit Tracker API - Usage Examples

This document provides detailed examples of how to use the Habit Tracker API endpoints.

## Authentication

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepassword123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe"
  },
  "timestamp": "2024-07-17T14:30:00"
}
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "john_doe",
    "password": "securepassword123"
  }'
```

## Habit Management

### 3. Create a New Habit

```bash
curl -X POST http://localhost:8080/api/habits \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Morning Exercise",
    "description": "30 minutes of cardio every morning",
    "frequencyType": "DAILY",
    "targetCount": 1
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Habit created successfully",
  "data": {
    "id": 1,
    "name": "Morning Exercise",
    "description": "30 minutes of cardio every morning",
    "frequencyType": "DAILY",
    "targetCount": 1,
    "isActive": true,
    "streakCount": 0,
    "bestStreak": 0,
    "createdAt": "2024-07-17T14:30:00",
    "updatedAt": "2024-07-17T14:30:00"
  },
  "timestamp": "2024-07-17T14:30:00"
}
```

### 4. Get All User Habits

```bash
curl -X GET http://localhost:8080/api/habits \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Get Habit by ID

```bash
curl -X GET http://localhost:8080/api/habits/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. Update a Habit

```bash
curl -X PUT http://localhost:8080/api/habits/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Morning Exercise - Updated",
    "description": "45 minutes of cardio every morning",
    "frequencyType": "DAILY",
    "targetCount": 1
  }'
```

### 7. Delete a Habit

```bash
curl -X DELETE http://localhost:8080/api/habits/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 8. Search Habits

```bash
curl -X GET "http://localhost:8080/api/habits/search?name=exercise" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Habit Entry Management

### 9. Log a Habit Entry

```bash
curl -X POST http://localhost:8080/api/habits/1/entries \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "entryDate": "2024-07-17",
    "completionCount": 1,
    "isCompleted": true,
    "notes": "Great workout today! Felt energized."
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Habit entry created successfully",
  "data": {
    "id": 1,
    "entryDate": "2024-07-17",
    "completionCount": 1,
    "isCompleted": true,
    "notes": "Great workout today! Felt energized.",
    "createdAt": "2024-07-17T14:30:00",
    "updatedAt": "2024-07-17T14:30:00"
  },
  "timestamp": "2024-07-17T14:30:00"
}
```

### 10. Get Habit Entries

```bash
curl -X GET http://localhost:8080/api/habits/1/entries \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 11. Get Habit Entries by Date Range

```bash
curl -X GET "http://localhost:8080/api/habits/1/entries?startDate=2024-07-01&endDate=2024-07-17" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 12. Update a Habit Entry

```bash
curl -X PUT http://localhost:8080/api/habits/1/entries/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "entryDate": "2024-07-17",
    "completionCount": 2,
    "isCompleted": true,
    "notes": "Did an extra set today!"
  }'
```

### 13. Delete a Habit Entry

```bash
curl -X DELETE http://localhost:8080/api/habits/1/entries/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Analytics

### 14. Get User Analytics

```bash
curl -X GET http://localhost:8080/api/analytics \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Analytics retrieved successfully",
  "data": {
    "totalHabits": 5,
    "activeHabits": 3,
    "completedEntriesThisWeek": 15,
    "completedEntriesThisMonth": 45,
    "weeklyCompletionRate": 85.7,
    "monthlyCompletionRate": 75.0,
    "longestStreak": 12,
    "currentStreak": 5,
    "habitStreaks": [
      {
        "habitId": 1,
        "habitName": "Morning Exercise",
        "currentStreak": 5,
        "bestStreak": 12
      }
    ],
    "dailyCompletions": {
      "2024-07-17": 3,
      "2024-07-16": 2,
      "2024-07-15": 3
    },
    "habitCompletionCounts": {
      "Morning Exercise": 25,
      "Reading": 20,
      "Meditation": 18
    }
  },
  "timestamp": "2024-07-17T14:30:00"
}
```

### 15. Get Habit-Specific Analytics

```bash
curl -X GET http://localhost:8080/api/analytics/habits/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Health Check

### 16. Application Health Check

```bash
curl -X GET http://localhost:8080/api/health
```

**Response:**
```json
{
  "success": true,
  "message": "Application is running",
  "data": {
    "status": "UP",
    "application": "Habit Tracker API",
    "version": "1.0.0",
    "timestamp": "2024-07-17T14:30:00"
  },
  "timestamp": "2024-07-17T14:30:00"
}
```

## Error Handling

### Example Error Response

```json
{
  "success": false,
  "message": "Habit not found with id: '999'",
  "data": null,
  "timestamp": "2024-07-17T14:30:00"
}
```

### Validation Error Response

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "name": "Habit name is required",
    "email": "Email should be valid"
  },
  "timestamp": "2024-07-17T14:30:00"
}
```

## Testing with Postman

1. **Import the API**: Create a new collection in Postman
2. **Set Base URL**: Use `http://localhost:8080/api`
3. **Authentication**: After login, copy the JWT token and add it to the Authorization header as `Bearer YOUR_JWT_TOKEN`
4. **Test All Endpoints**: Use the examples above to test each endpoint

## Database Setup Reminder

Before running the application, ensure you have:

1. **MySQL installed** and running
2. **Database created**: `habit_tracker`
3. **Credentials configured** in `application.properties`
4. **Tables will be auto-created** by Hibernate on first run

## Running the Application

```bash
# Navigate to project directory
cd habit-tracker-api

# Run the application
mvn spring-boot:run

# Or build and run the JAR
mvn clean package
java -jar target/habit-tracker-api-1.0.0.jar
```

The API will be available at `http://localhost:8080/api`

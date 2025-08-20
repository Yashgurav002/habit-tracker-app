# 📘 How to Use the Habit Tracker API

## ⚠️ Understanding the 403 Error

The **403 Forbidden** error you're seeing at `http://localhost:8080/api/` is **EXPECTED** and **CORRECT**. This means:

✅ **Security is working properly**  
✅ **The application is running correctly**  
✅ **Authentication is required for protected endpoints**

---

## 🛠️ How to Test the API

### 1. **Start the Application**

```bash
mvn spring-boot:run
```
Wait for the message: `Started HabitTrackerApplication in X.X seconds`

---

### 2. **Verify Database Connection**

The application automatically creates the database schema on startup. Look for logs like:

```
HHH000204: Processing PersistenceUnitInfo [name: default]
HHH000400: Using dialect: org.hibernate.dialect.MySQL8Dialect
Initialized JPA EntityManagerFactory for persistence unit 'default'
```

**To manually verify in MySQL:**
```sql
USE habit_tracker;
SHOW TABLES;
-- Should list: habit_entries, habits, users
```

---

### 3. **Test Available Endpoints**

#### ✅ Health Check (No Auth Required)
```bash
curl -X GET http://localhost:8080/api/health
```
**Expected Output:**
```json
{
  "success": true,
  "message": "Application is running",
  "data": {
    "status": "UP",
    "application": "Habit Tracker API",
    "version": "1.0.0",
    "timestamp": "2024-07-17T20:17:34.7344596"
  }
}
```

#### 📝 Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "youruser",
    "password": "yourpassword",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

#### 🔐 Login a User
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "youruser",
    "password": "yourpassword"
  }'
```

#### 💡 Optional: Extract JWT Token (Linux + jq)
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"youruser","password":"yourpassword"}' | jq -r '.data.token')
echo "Your token: $TOKEN"
```

---

### 4. **Access Protected Endpoints**

#### 📋 List Habits
```bash
curl -X GET http://localhost:8080/api/habits \
  -H "Authorization: Bearer $TOKEN"
```

#### ➕ Create a Habit
```bash
curl -X POST http://localhost:8080/api/habits \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Drink Water",
    "description": "8 glasses a day",
    "frequencyType": "DAILY",
    "targetCount": 1
  }'
```

#### 📆 Log a Habit Entry
```bash
curl -X POST http://localhost:8080/api/habits/1/entries \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2024-07-17",
    "completed": true
  }'
```

#### 📊 View Analytics
```bash
curl -X GET http://localhost:8080/api/analytics \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🌐 Complete API Endpoints

### Public (No Auth Required)
- `GET /api/health` – Health Check
- `POST /api/auth/register` – Register a User
- `POST /api/auth/login` – Login a User

### Protected (JWT Required)
- `GET /api/habits` – List Habits
- `POST /api/habits` – Create Habit
- `GET /api/habits/{id}` – Get Habit
- `PUT /api/habits/{id}` – Update Habit
- `DELETE /api/habits/{id}` – Delete Habit
- `POST /api/habits/{id}/entries` – Log Entry
- `GET /api/habits/{id}/entries` – Get Entries
- `GET /api/analytics` – View Analytics

---

## 🔐 Authentication Flow

1. Register or Login
2. Copy the `token` from response
3. Use `Authorization: Bearer YOUR_TOKEN` in requests

---

## 🧩 Troubleshooting

### App Not Starting?
- Check MySQL is running
- Verify DB credentials in `application.properties`

### DB Errors?
- Confirm database: `habit_tracker`
- Confirm user/password

### 403 Errors?
- ✅ Expected for protected routes without token
- ✅ Expected for root `/api/`
- ❌ Unexpected for `/api/health`, `/api/auth/*`

---

## 📨 Using Postman to Test the Habit Tracker API

### Import Postman Collection and Environment

1. Open Postman.
2. Import the `Habit-Tracker-API.postman_collection.json` file:
   - Click **Import** → **Upload Files** → Select the collection JSON file.
3. Import the `Habit-Tracker-Environment.postman_environment.json` file:
   - Click **Import** → **Upload Files** → Select the environment JSON file.
4. Select the **Habit Tracker Environment** from the environment dropdown in Postman.

### Using the Postman Collection

- The collection contains all API requests organized in order:
  - Health Check (no auth)
  - User Registration
  - User Login
  - Habit management (CRUD)
  - Habit Entries management
  - Analytics
  - Delete operations

- After registering or logging in, the JWT token is automatically saved to the environment variable `authToken` and used in subsequent requests.

- IDs for user, habit, and habit entries are automatically populated ensuring smooth testing workflows.

### Tips for Effective Testing

- Run requests sequentially starting with health check, then user registration or login.
- Check the **Tests** tab in each request for response validation scripts.
- View Postman's Console for detailed logs and debugging.

### Manual cURL Example for Quick Testing

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"testpassword123","firstName":"Test","lastName":"User"}'
```

---

## ✅ Success Checklist

- App starts without error
- `/api/health` returns `200 OK`
- DB tables auto-created
- 403 on protected routes (correct)
- JWT Auth working
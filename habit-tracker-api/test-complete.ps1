Write-Host "=== Habit Tracker API Testing ===" -ForegroundColor Cyan
Write-Host ""

# Test 1: Health Check
Write-Host "1. Testing Health Endpoint..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get
    Write-Host "✅ Health Check: SUCCESS" -ForegroundColor Green
    Write-Host "   Status: $($health.data.status)" -ForegroundColor Gray
    Write-Host "   Application: $($health.data.application)" -ForegroundColor Gray
    Write-Host "   Version: $($health.data.version)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Health Check: FAILED - $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: User Registration
Write-Host "2. Testing User Registration..." -ForegroundColor Yellow
try {
    $registerData = @{
        username = "testuser$(Get-Random)"
        email = "test$(Get-Random)@example.com"
        password = "testpassword123"
        firstName = "Test"
        lastName = "User"
    }
    $jsonData = $registerData | ConvertTo-Json
    $registerResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method Post -Body $jsonData -ContentType "application/json"
    Write-Host "✅ User Registration: SUCCESS" -ForegroundColor Green
    Write-Host "   User ID: $($registerResponse.data.id)" -ForegroundColor Gray
    Write-Host "   Username: $($registerResponse.data.username)" -ForegroundColor Gray
    Write-Host "   Token: $($registerResponse.data.token.Substring(0,20))..." -ForegroundColor Gray
    
    # Store token for further tests
    $global:authToken = $registerResponse.data.token
    $global:userId = $registerResponse.data.id
} catch {
    Write-Host "❌ User Registration: FAILED - $($_.Exception.Message)" -ForegroundColor Red
    $global:authToken = $null
}

Write-Host ""

# Test 3: Login (only if registration failed)
if (-not $global:authToken) {
    Write-Host "3. Testing Login with default user..." -ForegroundColor Yellow
    try {
        $loginData = @{
            usernameOrEmail = "testuser"
            password = "testpassword123"
        }
        $jsonData = $loginData | ConvertTo-Json
        $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $jsonData -ContentType "application/json"
        Write-Host "✅ Login: SUCCESS" -ForegroundColor Green
        $global:authToken = $loginResponse.data.token
    } catch {
        Write-Host "❌ Login: FAILED - $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""

# Test 4: Protected endpoint test
if ($global:authToken) {
    Write-Host "4. Testing Protected Endpoint (Get Habits)..." -ForegroundColor Yellow
    try {
        $headers = @{
            Authorization = "Bearer $global:authToken"
        }
        $habitsResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/habits" -Method Get -Headers $headers
        Write-Host "✅ Get Habits: SUCCESS" -ForegroundColor Green
        Write-Host "   Number of habits: $($habitsResponse.data.Count)" -ForegroundColor Gray
    } catch {
        Write-Host "❌ Get Habits: FAILED - $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "4. ⚠️  Skipping protected endpoint test (no auth token)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== Test Summary ===" -ForegroundColor Cyan
Write-Host "Application URL: http://localhost:8080/api" -ForegroundColor White
Write-Host "Available endpoints:" -ForegroundColor White
Write-Host "  - GET  /api/health (public)" -ForegroundColor Gray
Write-Host "  - POST /api/auth/register (public)" -ForegroundColor Gray  
Write-Host "  - POST /api/auth/login (public)" -ForegroundColor Gray
Write-Host "  - GET  /api/habits (protected)" -ForegroundColor Gray
Write-Host "  - POST /api/habits (protected)" -ForegroundColor Gray
Write-Host "  - GET  /api/analytics (protected)" -ForegroundColor Gray
Write-Host ""

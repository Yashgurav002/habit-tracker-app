# Test script for Habit Tracker API
Write-Host "Testing Habit Tracker API..." -ForegroundColor Green

# Test health endpoint
Write-Host "`nTesting health endpoint..." -ForegroundColor Yellow
try {
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get
    Write-Host "Health Check: SUCCESS" -ForegroundColor Green
    $healthResponse | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Health Check: FAILED - $($_.Exception.Message)" -ForegroundColor Red
}

# Test user registration
Write-Host "`nTesting user registration..." -ForegroundColor Yellow
try {
    $registerData = @{
        username = "testuser"
        email = "test@example.com"
        password = "testpassword123"
        firstName = "Test"
        lastName = "User"
    }
    
    $registerResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method Post -Body ($registerData | ConvertTo-Json) -ContentType "application/json"
    Write-Host "User Registration: SUCCESS" -ForegroundColor Green
    $registerResponse | ConvertTo-Json -Depth 3
} catch {
    Write-Host "User Registration: FAILED - $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest completed." -ForegroundColor Green

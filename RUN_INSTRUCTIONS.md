# How to Run Both Servers and Test

## Prerequisites

1. **Java 17+** installed
2. **Maven** installed (or use Maven Wrapper included in project)
3. **MySQL** running with database `demoproj`
4. **Database schema** already created with tables

## Quick Start (From Root Directory)

If you're already in the project root directory, you can run both servers directly:

### Start Backend Server (Command Prompt / PowerShell 1)

```cmd
cd javabackend\HM\SprintProject
mvnw.cmd spring-boot:run
```

**Wait for:** `Started SprintProjectApplication in X.XXX seconds`

### Start Frontend Server (Command Prompt / PowerShell 2)

```cmd
cd frontend-server
mvnw.cmd spring-boot:run
```

**Wait for:** `Started FrontendServerApplication in X.XXX seconds`

### Access the Application

- **Frontend:** http://localhost:8080
- **Backend API:** http://localhost:8081

---

## Step-by-Step Instructions (Detailed)

### 1. Start MySQL Database

Make sure MySQL is running and the database `demoproj` exists:

```cmd
# Connect to MySQL (use MySQL Command Line Client or MySQL Workbench)
mysql -u root -p

# Verify database exists
SHOW DATABASES;
USE demoproj;
SHOW TABLES;
```

### 2. Start Backend Server (Port 8081)

Open a Command Prompt or PowerShell and navigate to the backend project:

```cmd
cd javabackend\HM\SprintProject
```

Start the backend server:

```cmd
# Using Maven Wrapper (recommended)
mvnw.cmd spring-boot:run

# OR if you have Maven installed globally
mvn spring-boot:run
```

**Wait for this message:**
```
Started SprintProjectApplication in X.XXX seconds
```

The backend will be available at: `http://localhost:8081`

### 3. Start Frontend Server (Port 8080)

Open a **NEW Command Prompt or PowerShell** (keep backend running) and navigate to the frontend project:

```cmd
cd frontend-server
```

Start the frontend server:

```cmd
# Using Maven Wrapper (recommended)
mvnw.cmd spring-boot:run

# OR if you have Maven installed globally
mvn spring-boot:run
```

**Wait for this message:**
```
Started FrontendServerApplication in X.XXX seconds
```

The frontend will be available at: `http://localhost:8080`

### 4. Test the Application

#### 4.1 Open Home Page
1. Open browser: `http://localhost:8080`
2. You should see the home page with 6 team member cards

#### 4.2 Test Kanishka Module (CRITICAL - Evaluator Requirement)
1. Click on **Kanishka** card or navigate to: `http://localhost:8080/team/kanishka`
2. **Open Browser DevTools** (F12)
3. Go to **Network** tab
4. **Refresh the page** (F5)
5. You should see:
   - ✅ `GET http://localhost:8081/patients?limit=10` - Status 200
   - ✅ Exactly **10 patient names** displayed in the list
6. Click on any patient
7. You should see:
   - ✅ `GET http://localhost:8081/patients/{ssn}` - Status 200
   - ✅ Patient details displayed (name, SSN, address, physician info)

#### 4.3 Test Other Modules

**Mahitha (Nurses):**
- Navigate to: `http://localhost:8080/team/mahitha`
- Should see nurses list from backend
- Click a nurse to see details

**Karthika (Procedures):**
- Navigate to: `http://localhost:8080/team/karthika`
- Should see procedures grid from backend
- Click a procedure to see modal with details

**Akhila (Patients & Appointments):**
- Navigate to: `http://localhost:8080/team/akhila`
- Should see patients and their appointments

**Samiksha (Physicians & Departments):**
- Navigate to: `http://localhost:8080/team/samiksha`
- Should see physicians and department relationships

**Keerthana (Room Management):**
- Navigate to: `http://localhost:8080/team/keerthana`
- Should see patients and room assignments

### 5. Verify Backend API Directly

You can test the backend API directly using PowerShell or browser:

```powershell
# Test patients endpoint with limit
Invoke-WebRequest -Uri "http://localhost:8081/patients?limit=10"

# Test all patients
Invoke-WebRequest -Uri "http://localhost:8081/patients"

# Test nurses
Invoke-WebRequest -Uri "http://localhost:8081/nurses"

# Test procedures
Invoke-WebRequest -Uri "http://localhost:8081/procedures"
```

Or open in browser:
- `http://localhost:8081/patients?limit=10`
- `http://localhost:8081/nurses`
- `http://localhost:8081/procedures`

## Troubleshooting

### Backend won't start
- **Check MySQL is running**: Open MySQL Workbench or Services (services.msc) and verify MySQL service is running
- **Check database exists**: Connect to MySQL and run `SHOW DATABASES;` - Make sure `demoproj` database exists
- **Check application.properties**: 
  - File: `javabackend\HM\SprintProject\src\main\resources\application.properties`
  - Database credentials must match your MySQL setup:
    - Username: `root`
    - Password: `jaimatadi` (update if your MySQL password is different)
    - Database: `demoproj`
- **Check port 8081 is free**: `netstat -ano | findstr :8081`

### Frontend won't start
- **Check port 8080 is free**: `netstat -ano | findstr :8080`
- **Check backend is running first**: Frontend needs backend to be available

### CORS Errors in Browser Console
- **Verify backend CORS config**: Should allow `http://localhost:8080`
- **Check backend is running on 8081**
- **Clear browser cache and reload** (Ctrl+Shift+Delete)

### No Data Showing
- **Check database has data**: Run `SELECT * FROM patient LIMIT 10;` in MySQL
- **Check backend logs**: Look for SQL errors
- **Check Network tab**: See if API calls are failing
- **Check browser console**: Look for JavaScript errors (F12 → Console tab)

### 404 Errors
- **Frontend routes**: Make sure you're using `/team/{member}` format
- **Backend routes**: Check controller mappings match what JS is calling

## Quick Test Checklist

Before submitting, verify:

- [ ] Backend starts on port 8081
- [ ] Frontend starts on port 8080
- [ ] Home page loads with 6 team cards
- [ ] Kanishka page loads and shows exactly 10 patients
- [ ] Network tab shows `GET /patients?limit=10` call
- [ ] Clicking a patient shows details
- [ ] Network tab shows `GET /patients/{ssn}` call
- [ ] At least one other module (Mahitha/Karthika) works
- [ ] No CORS errors in console
- [ ] No JavaScript errors in console

## Running Both Servers in Background (Optional)

If you want to run both servers in the background using Windows:

### From Root Directory:

```cmd
REM Terminal 1 - Backend
cd javabackend\HM\SprintProject
start /B mvnw.cmd spring-boot:run > backend.log 2>&1

REM Terminal 2 - Frontend
cd frontend-server
start /B mvnw.cmd spring-boot:run > frontend.log 2>&1
```

### From Individual Directories:

```cmd
REM Terminal 1 - Backend
cd javabackend\HM\SprintProject
start /B mvnw.cmd spring-boot:run > backend.log 2>&1

REM Terminal 2 - Frontend
cd frontend-server
start /B mvnw.cmd spring-boot:run > frontend.log 2>&1
```

To stop them:
```cmd
REM Find process IDs
netstat -ano | findstr :8081  REM Backend
netstat -ano | findstr :8080  REM Frontend

REM Kill processes (replace <PID> with actual process ID)
taskkill /PID <PID> /F
```

## Alternative: Using PowerShell Background Jobs

You can also run servers as background jobs in PowerShell:

```powershell
# Terminal 1 - Backend
cd javabackend\HM\SprintProject
Start-Job -ScriptBlock { .\mvnw.cmd spring-boot:run }

# Terminal 2 - Frontend
cd frontend-server
Start-Job -ScriptBlock { .\mvnw.cmd spring-boot:run }

# View running jobs
Get-Job

# Stop jobs
Stop-Job -Name "Job1"
Remove-Job -Name "Job1"
```

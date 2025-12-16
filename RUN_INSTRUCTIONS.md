# How to Run Both Servers and Test

## Prerequisites

1. **Java 17+** installed
2. **Maven** installed (use the included wrappers if you prefer)
3. **MySQL** running with the `hms` schema populated

## Quick Start

### 1. Start the backend (`hmsbackend` on port 8081)

**On Unix/Mac:**
```bash
cd hmsbackend
./mvnw spring-boot:run
```

**On Windows:**
```bash
cd hmsbackend
.\mvnw spring-boot:run
```
or
```bash
cd hmsbackend
mvnw.cmd spring-boot:run
```

Wait for `Started HmsbackendApplication` and confirm `http://localhost:8081/api/...` responds (e.g., visit `/api/physician`).

### 2. Start the frontend (`frontend-server` on port 8080)

**On Unix/Mac:**
```bash
cd frontend-server
./mvnw spring-boot:run
```

**On Windows:**
```bash
cd frontend-server
.\mvnw spring-boot:run
```
or
```bash
cd frontend-server
mvnw.cmd spring-boot:run
```

Once `Started FrontendServerApplication` appears, browse to `http://localhost:8080`.

### 3. High-level smoke test
1. Load `http://localhost:8080` – six teammate cards should display.
2. Open each `/team/{member}` page; the URL query string will change as you click rows (e.g., `/team/karthik?procedureCode=10`). Every refresh should render data immediately because Thymeleaf receives a fully populated model.

---

## Detailed Verification Checklist

For every team page the frontend makes exactly two server-side REST calls (see README). The easiest way to verify connectivity is to watch the backend logs or tail the terminal where `hmsbackend` is running.

### Akhila (`/team/akhila`)
- Default load triggers `GET /api/physician` then `GET /api/patient/{physicianId}`.
- Selecting a different physician reloads the page with `?physicianId=` and you should see the new patient roster server-rendered.

### Karthik (`/team/karthik`)
- Initial view calls `GET /api/procedure`.
- Every procedure link reloads the page with `?procedureCode=` and the backend logs `GET /api/trained_in/physicians/{code}`.

### Keerthana (`/team/keerthana`)
- Loads `GET /api/patient` once; choosing a patient reloads with `?patientId=` to call `GET /api/appointment/date/{patientId}`.

### Mahitha (`/team/mahitha`)
- Fetches `GET /api/nurse` and, for the highlighted nurse, `GET /api/patient/patient/{nurseId}`.

### Kanishka (`/team/kanishka`)
- Fetches `GET /api/patient`, then `GET /api/prescribes/patient/{ssn}` for the chosen patient.

### Samiksha (`/team/samiksha`)
- Calls `GET /api/physician` followed by `GET /api/affiliated_with/department/{physicianId}`.

If any call fails you will still see the page render with “No data available” states—check the backend console for stack traces and verify the database seed data.

---

## Direct API Spot Checks (optional)
You can hit the backend endpoints directly with `curl`, Postman, or a browser:
```bash
curl http://localhost:8081/api/physician
curl http://localhost:8081/api/patient/100000001
curl http://localhost:8081/api/procedure
```
These are the same URLs the frontend calls through `RestTemplate`.

---

## Troubleshooting

- **Backend fails to start:** verify MySQL credentials in `hmsbackend/src/main/resources/application.properties`, ensure port 8081 is free, and confirm the `hms` schema exists.
- **Frontend fails to start:** ensure port 8080 is free. Because there is no client-side JavaScript, any missing data is usually caused by the backend call failing—inspect the frontend logs for `BackendClientException` messages.
- **Data appears stale:** restart the backend after modifying seed data; the frontend only caches results per HTTP request and will reload on each page visit.

Following these steps guarantees the new server-side rendering flow functions end-to-end with `hmsbackend` as the only REST source.

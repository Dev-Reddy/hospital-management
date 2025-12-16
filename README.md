# Hospital Management Demo – Application & Data Flow

This README explains, in practical terms, how the Tailwind/Thymeleaf frontend now talks to the Spring Boot backend (`hmsbackend`), which APIs are invoked per teammate page, and how you can extend those flows without reintroducing client-side JavaScript.

---

## 1. High-Level Architecture

```
┌────────────┐   http://localhost:8080   ┌──────────────────────────────────────┐
│  Browser   │ ────────────────────────► │ Frontend Server (Spring MVC + HTML) │
└────────────┘        HTML only          └────────────┬────────────────────────┘
                                                      │ server-side RestTemplate calls
                                                      ▼
                                     http://localhost:8081 (JSON REST APIs)
┌────────────────────────────┐                        ┌────────────────────────────┐
│ frontend-server project    │◄──────────────────────►│ hmsbackend project         │
│ • PageController           │  JSON responses        │ • REST controllers/service │
│ • Thymeleaf templates      │                        │ • MySQL persistence        │
└────────────────────────────┘                        └────────────────────────────┘
```

**Key points**
1. Every `/team/{member}` endpoint is a Spring MVC handler that loads data with `RestTemplate` before rendering Thymeleaf.
2. No JavaScript runs in the browser—nav highlighting and data binding are 100% server-side.
3. The frontend now depends solely on the `hmsbackend` module; the legacy `javabackend` folder is no longer referenced.

---

## 2. Per-Page Backend Wiring

Each teammate page intentionally uses exactly two REST calls: one collection “GET all” to build the left-hand selection list, and one “GET by id” to render the detail pane.

| Team page | GET-all call | GET-by-id call | What the page shows |
|-----------|--------------|----------------|---------------------|
| `/team/akhila` | `GET /api/physician` | `GET /api/patient/{physicianId}` | Physician directory with the patients assigned to the selected doctor. |
| `/team/karthik` | `GET /api/procedure` | `GET /api/trained_in/physicians/{procedureCode}` | Catalogue of treatments plus the physicians certified for the highlighted procedure. |
| `/team/keerthana` | `GET /api/patient` | `GET /api/appointment/date/{patientId}` | Patient roster with the upcoming appointment dates for the chosen patient. |
| `/team/mahitha` | `GET /api/nurse` | `GET /api/patient/patient/{nurseId}` | Registered nurses and the patients they have handled. |
| `/team/kanishka` | `GET /api/patient` | `GET /api/prescribes/patient/{ssn}` | Patient list with their insurance information and prescribed medications. |
| `/team/samiksha` | `GET /api/physician` | `GET /api/affiliated_with/department/{physicianId}` | Physician directory tied to the departments they are affiliated with. |

The frontend keeps the currently selected identifier in the query string (e.g., `/team/karthik?procedureCode=10`) so a full-page reload retrieves the correct detail block without JavaScript.

---

## 3. Request Lifecycle

```
Browser  ──►  GET /team/mahitha?nurseId=101
              │
              ▼
Frontend controller
  1. RestTemplate -> GET /api/nurse
  2. RestTemplate -> GET /api/patient/patient/101
  3. Model populated with nurses + patients
              │
              ▼
Thymeleaf template renders list + detail in one response (no JS)
```

This same “fetch list → fetch detail → render HTML” pattern powers every teammate route. If either backend call fails, the controller falls back to empty lists so the template can display a graceful “no data” message.

---

## 4. Feature Walkthroughs

### 4.1 Home (`/`)
*Purpose:* Static landing page and navigation hub.

*Flow:* `GET /` → `PageController.home()` → `templates/index.html`. No backend calls are made.

### 4.2 Akhila – Physician ➜ Patients
1. Controller loads `List<PhysicianDto>` via `GET /api/physician` and picks the requested `physicianId` (or the first entry).
2. It then calls `GET /api/patient/{physicianId}` to fetch the roster for that doctor.
3. Thymeleaf renders the physician list on the left and the selected doctor’s patient cards on the right.

### 4.3 Karthik – Procedures ➜ Trained Physicians
1. Load every procedure from `/api/procedure`.
2. When one is selected (via `?procedureCode=...`), fetch `GET /api/trained_in/physicians/{code}`.
3. The template shows the chosen procedure header plus the certified physicians.

### 4.4 Keerthana – Patients ➜ Appointment Dates
1. Fetch all patients once (`/api/patient`).
2. Selecting a patient triggers `GET /api/appointment/date/{patientId}` which returns an array of ISO dates.
3. Thymeleaf prints the pending appointment timeline.

### 4.5 Mahitha – Nurses ➜ Patients seen
1. Load nurses (`/api/nurse`).
2. Fetch `/api/patient/patient/{nurseId}` for the highlighted nurse.
3. The view surfaces registration info plus the patients tied to that nurse.

### 4.6 Kanishka – Patients ➜ Prescriptions
1. Grab all patient summaries.
2. When you select a patient, call `/api/prescribes/patient/{ssn}` to obtain prescriptions (medication name, dose, physician, timestamp).
3. Thymeleaf renders a medication history list.

### 4.7 Samiksha – Physicians ➜ Departments
1. Fetch physicians.
2. Load departments for the selected physician via `/api/affiliated_with/department/{physicianId}`.
3. The UI displays affiliation chips with department IDs and names.

---

## 5. Extending the Flow

1. **Add a backend capability:** Implement or reuse an endpoint inside `hmsbackend/src/main/java/com/capgemini/hmsbackend/controller`. Make sure it is reachable under `/api/...`.
2. **Expose it to the frontend:** Add a method to `HmsBackendClient` that calls the new endpoint and maps its JSON to a DTO living under `frontend-server/src/main/java/com/medicare/frontend/dto`.
3. **Bind it to a page:** Update `PageController` (or a dedicated service) to populate the `Model` with the new data and render it inside the relevant Thymeleaf template.
4. **Keep it server-side:** Resist adding client-side JavaScript—the navigation + templates are already wired to do full page reloads with the proper query parameters.

Following this recipe keeps the architecture predictable: browser request ➜ Spring MVC controller ➜ RestTemplate calls into `hmsbackend` ➜ Thymeleaf renders HTML. Any new feature should fit somewhere in that chain.

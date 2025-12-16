# Hospital Management Demo – Application & Data Flow

This README explains, in step-by-step language, how pages are served, which APIs they call, how the backend answers, and how to extend those flows. ASCII diagrams are included so you can visualize the journey from the browser to the database and back.

---

## 1. High-Level Architecture

```
┌────────────┐   http://localhost:8080   ┌────────────────────────┐
│  Browser   │ ────────────────────────► │ Frontend Server (MVC) │
└────────────┘   HTML + JS (Thymeleaf)   └────────────┬───────────┘
                                                        │
                                                        │ apiFetch()
                                                        ▼
                                   http://localhost:8081 (JSON REST APIs)
┌────────────────────────────┐                          ┌──────────────────────┐
│ frontend-server project    │◄────────────────────────►│ javabackend project  │
│ • PageController           │  JSON responses          │ • REST controllers   │
│ • Thymeleaf templates      │                          │ • Services + DB      │
│ • JS assets (per page)     │                          │                      │
└────────────────────────────┘                          └──────────────────────┘
```

**Request lifecycle**

1. User points the browser to `/` or `/team/{member}` on `http://localhost:8080`.
2. `PageController` returns the relevant Thymeleaf template (`index.html` or `team/{member}.html`).
3. The template loads Tailwind CSS plus a page-specific JavaScript file from `static/assets/js`.
4. That JS module uses the shared `apiFetch()` helper to call the REST backend at `http://localhost:8081`.
5. A REST controller in `javabackend/HM/SprintProject` receives the call, runs a service, hits the database, and returns JSON.
6. The JS module updates its local state and re-renders the DOM (cards, tables, modals, etc.).

---

## 2. Controllers & Endpoints Map

| Frontend URL | Template | JS Modules | Backend Endpoints Hit | Backend Controller(s) |
|--------------|----------|------------|-----------------------|-----------------------|
| `/` | `templates/index.html` | (none – static content) | – | – |
| `/team/akhila` | `templates/team/akhila.html` | `assets/js/akhila.js` + `api-config.js` | `GET /patients`, `GET /appointments` | `PatientController`, `AppointmentController` |
| `/team/kanishka` | `templates/team/kanishka.html` | `assets/js/kanishka.js` | `GET /patients?limit=10`, `GET /patients/{ssn}` | `PatientController` |
| `/team/samiksha` | `templates/team/samiksha.html` | `assets/js/samiksha.js` | `GET /physicians`, `GET /departments` | `PhysicianController`, `DepartmentController` |
| `/team/keerthana` | `templates/team/keerthana.html` | `assets/js/keerthana.js` | `GET /patients`, `GET /appointments` | `PatientController`, `AppointmentController` |
| `/team/mahitha` | `templates/team/mahitha.html` | `assets/js/mahitha.js` | `GET /nurses`, `GET /nurses/{id}` | `NurseController` |
| `/team/karthik` | `templates/team/karthik.html` | `assets/js/karthik.js` | `GET /procedures`, `GET /procedures/{code}` | `ProcedureController` |

All backend controllers live under `javabackend/HM/SprintProject/src/main/java/com/training/controller/`. Each controller delegates work to a service layer (not shown) and returns JSON through Jackson.

---

## 3. Request Flow Diagrams

### 3.1 Generic Team Page Flow

```
[1] Browser hits /team/{member}
      │
      ▼
[2] PageController.team() sets model.pageRoute and returns templates/team/{member}.html
      │
      ▼
[3] Template loads CSS + member JS module (e.g., assets/js/akhila.js)
      │
      ▼
[4] JS state initialized → render "loading" cards
      │
      ▼
[5] JS calls apiFetch("/resource") AND/OR apiFetch("/resource/{id}")
      │
      ▼
[6] REST controller (e.g., PatientController) loads data via service/repository
      │
      ▼
[7] JSON response → JS updates in-memory state and re-renders the DOM
```

You can layer more data by repeating steps 5–7 with new endpoints and render functions.

### 3.2 Example: Akhila’s Patient Directory

```
Browser      Frontend Server          JS Module           Backend API
   │ GET /team/akhila │                   │                       │
   ├──────────────────► PageController    │                       │
   │  HTML Template   │                   │                       │
   ◄──────────────────┘                   │                       │
   │ (loads akhila.js)                    │                       │
   │                                      │ DOMContentLoaded      │
   │                                      ├───────────────► loadAkhilaData()
   │                                      │                       │
   │                                      │ apiFetch("/patients") │
   │                                      │──────────────────────►│ PatientController.getAllPatients()
   │                                      │                       ├──► PatientService / DB
   │                                      │                       │
   │                                      │                       ◄── JSON list
   │                                      │◄──────────────────────│
   │                                      │ apiFetch("/appointments")
   │                                      │──────────────────────►│ AppointmentController.getAllAppointments()
   │                                      │                       ├──► AppointmentService / DB
   │                                      │                       │
   │                                      │                       ◄── JSON list
   │                                      │◄──────────────────────│
   │                                      │ Render patient cards + detail panel
```

This same “double fetch + render” pattern shows up on Keerthana’s page (patients + appointments) and Samiksha’s page (physicians + departments).

---

## 4. Detailed Page Guides

### 4.1 Home (`/`)
*Purpose:* Display static branding + teammate navigation.

*Flow:* Browser → `GET /` → `PageController.home()` → `templates/index.html`. No API calls. All text/images live inside the template.

*Extend it:* Add a `<script src="/assets/js/home-metrics.js"></script>` tag and use `apiFetch("/metrics")` to show live stats without touching the backend routing.

---

### 4.2 Akhila – Patient & Appointments
*Use case:* Show every patient and their appointment history.

1. `loadAkhilaData()` fires on DOM ready.
2. Fetch `/patients` and `/appointments`; store results inside `akhilaState`.
3. Auto-select the first patient (`selectedSsn = patients[0].ssn`).
4. `renderAkhilaList()` builds the left column; clicking an entry updates `selectedSsn`.
5. `renderAkhilaDetails()` filters appointments for that SSN, then displays patient metadata + appointment cards.

*Additions:* If you create `/patients/{ssn}/notes`, call it right after selecting a patient, then append note cards in `renderAkhilaDetails()`. Everything else stays the same.

---

### 4.3 Kanishka – Physician Network
*Use case:* Inspect a single patient plus their primary physician.

1. `loadKanishkaPatients()` fetches `/patients?limit=10`.
2. First patient becomes the default selection; `selectKanishkaPatient(ssn)` runs.
3. That function fetches `/patients/{ssn}` to retrieve the full record (including nested physician).
4. `renderKanishkaDetails()` displays contact info + primary care physician.

*Adding training history:* expose `/patients/{ssn}/training`, call it inside `selectKanishkaPatient()`, store the result on `kanishkaState.selectedPatient.training`, and render a training list.

---

### 4.4 Samiksha – Departments
*Use case:* Match physicians to their departments.

1. `loadSamikshaData()` fetches `/physicians` and `/departments`.
2. The JS stores both arrays, selects the first physician, and renders two panes.
3. In the details pane, the code finds the matching department (`departmentId`) and shows leadership and identifiers.

*Extending fields:* Add properties (e.g., staff counts) to the Department entity + controller, then display the new fields inside the details template without modifying the flow.

---

### 4.5 Keerthana – Room Management
*Use case:* Track room assignments via the latest appointment.

1. Fetch `/patients` + `/appointments` (same as Akhila).
2. When a patient is selected, compute their latest appointment.
3. Render the assigned room, upcoming appointments, and physician contact.

*Adding updates:* Implement `PUT /appointments/{id}` in the backend, add a form/button in `keerthana.js`, call `apiFetch(path, { method: "PUT", body: JSON.stringify(...) })`, and reload appointments to reflect the new room.

---

### 4.6 Mahitha – Nursing Staff
*Use case:* Browse nurse profiles.

1. `loadMahithaNurses()` fetches `/nurses`.
2. Selecting a nurse triggers `selectMahithaNurse(id)` which fetches `/nurses/{id}` for full details.
3. The details pane shows employee info, registration status, and department assignment.

*Future actions:* The backend already exposes `POST /nurses`, `PUT /nurses/{id}`, and `DELETE /nurses/{id}`. Hook up forms/buttons in `mahitha.js` to call those endpoints, then refresh the list.

---

### 4.7 Karthik – Medical Procedures
*Use case:* List procedures and open detail modals.

1. `loadKarthikProcedures()` fetches `/procedures` to populate the grid.
2. Clicking a card opens the modal and fetches `/procedures/{code}`.
3. The modal shows the returned data; closing the modal simply hides the overlay.

*Enhancements:* Add filter inputs that call `/procedures?type={value}` or include additional metadata (duration, resources) in the backend response and display it inside the modal.

---

## 5. How to Extend or Modify Flows

1. **Add or change an API**
   - Implement the controller/service/repository change in `javabackend`.
   - Expose the endpoint under a clear path (e.g., `/patients/{ssn}/notes`).
   - Restart the backend service.
2. **Consume it on the frontend**
   - Import the endpoint via `apiFetch(newPath, options)`.
   - Update the relevant state object and render functions to reflect the new data.
3. **Adjust templates/navigation**
   - Update `PageController` or add new controllers if you introduce new views.
   - Create a matching template in `frontend-server/src/main/resources/templates`.
   - Link the template to a JS file in `static/assets/js` and follow the same pattern (state object + render functions).
4. **Document or share**
   - Keep this README updated so future teammates know which files to touch for each flow.

Following this recipe keeps the architecture predictable: browser → template → JS → REST controller → service → database → JSON → DOM. Any new feature should fit somewhere along this chain.

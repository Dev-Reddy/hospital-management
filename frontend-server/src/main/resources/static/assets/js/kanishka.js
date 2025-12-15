const kanishkaPatientListEl = document.getElementById("kanishka-patient-list")
const kanishkaDetailsEl = document.getElementById("kanishka-details")

const kanishkaState = {
  patients: [],
  selectedId: null,
  selectedPatient: null,
  loading: false,
  error: null,
  detailLoading: false,
  detailError: null,
}

function renderKanishkaList() {
  if (!kanishkaPatientListEl) return
  kanishkaPatientListEl.innerHTML = ""

  if (kanishkaState.loading) {
    kanishkaPatientListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">Loading patients...</div>`
    return
  }

  if (kanishkaState.error) {
    kanishkaPatientListEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 bg-rose-50 rounded-2xl text-rose-600 font-semibold">${kanishkaState.error}</div>`
    return
  }

  if (!kanishkaState.patients.length) {
    kanishkaPatientListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">No patients returned from the server.</div>`
    return
  }

  kanishkaState.patients.forEach((patient) => {
    const button = document.createElement("button")
    button.type = "button"
    button.className = "w-full text-left"
    const active = patient.ssn === kanishkaState.selectedId
    button.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        active ? "border-cyan-500 bg-cyan-50 shadow-md" : "border-slate-200 bg-white hover:border-cyan-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${patient.name || "Unnamed Patient"}</div>
        <div class="text-sm text-slate-600">SSN: ${patient.ssn || "N/A"}</div>
      </div>
    `
    button.addEventListener("click", () => selectKanishkaPatient(patient.ssn))
    kanishkaPatientListEl.appendChild(button)
  })
}

function renderKanishkaDetails() {
  if (!kanishkaDetailsEl) return
  kanishkaDetailsEl.innerHTML = ""

  if (kanishkaState.detailLoading) {
    kanishkaDetailsEl.innerHTML = `<div class="p-16 text-center border-2 border-dashed border-slate-200 rounded-3xl bg-white">Loading patient details...</div>`
    return
  }

  if (kanishkaState.detailError) {
    kanishkaDetailsEl.innerHTML = `<div class="p-16 text-center border-2 border-rose-200 bg-rose-50 rounded-3xl text-rose-600 font-semibold">${kanishkaState.detailError}</div>`
    return
  }

  if (!kanishkaState.selectedPatient) {
    kanishkaDetailsEl.innerHTML = `
      <div class="p-16 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="size-20 rounded-2xl bg-cyan-50 flex items-center justify-center mx-auto mb-5 border-2 border-cyan-200">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10 text-cyan-500">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </div>
        <div class="text-2xl font-bold mb-2 text-slate-900">Select a Patient</div>
        <div class="text-slate-600 max-w-sm mx-auto">View live patient data directly from the backend service.</div>
      </div>
    `
    return
  }

  const patient = kanishkaState.selectedPatient
  const physician = patient.primaryCarePhysician

  kanishkaDetailsEl.innerHTML = `
    <div class="p-6 mb-6 bg-gradient-to-br from-cyan-50 to-blue-50 border-2 border-cyan-200 rounded-3xl">
      <div class="flex items-start gap-5">
        <div class="size-20 rounded-2xl bg-gradient-to-br from-cyan-500 to-blue-500 flex items-center justify-center text-white text-2xl font-bold shadow-lg">
          ${patient.name ? patient.name.split(" ").map((n) => n[0]).join("") : "PT"}
        </div>
        <div class="flex-1">
          <h3 class="text-3xl font-bold mb-2 text-slate-900">${patient.name || "Unnamed Patient"}</h3>
          <div class="text-sm text-slate-600 font-medium">SSN: ${patient.ssn || "N/A"}</div>
          <div class="text-sm text-slate-600 font-medium mt-1">Insurance: ${patient.insuranceId ?? "N/A"}</div>
        </div>
      </div>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
      <div class="p-4 bg-white rounded-2xl border border-slate-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Address</div>
        <div class="font-bold text-slate-900">${patient.address || "Unavailable"}</div>
      </div>
      <div class="p-4 bg-white rounded-2xl border border-slate-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Phone</div>
        <div class="font-bold text-slate-900">${patient.phone || "Unavailable"}</div>
      </div>
    </div>
    <div class="p-6 bg-white rounded-3xl border-2 border-slate-200">
      <h4 class="text-xl font-bold mb-4 text-slate-900">Primary Care Physician</h4>
      ${
        physician
          ? `
        <div class="text-slate-700 font-semibold">${physician.name || "Name unavailable"}</div>
        <div class="text-sm text-slate-500">Employee ID: ${physician.employeeId ?? "N/A"}</div>
      `
          : `<div class="text-slate-600">No physician linked for this patient.</div>`
      }
    </div>
  `
}

async function loadKanishkaPatients() {
  if (!kanishkaPatientListEl) return
  kanishkaState.loading = true
  kanishkaState.error = null
  renderKanishkaList()

  try {
    const patients = await apiFetch("/patients?limit=10")
    kanishkaState.patients = patients
    if (patients.length > 0) {
      selectKanishkaPatient(patients[0].ssn)
    } else {
      kanishkaState.selectedPatient = null
      renderKanishkaDetails()
    }
  } catch (error) {
    kanishkaState.error = error.message
  } finally {
    kanishkaState.loading = false
    renderKanishkaList()
  }
}

async function selectKanishkaPatient(ssn) {
  kanishkaState.selectedId = ssn
  kanishkaState.detailLoading = true
  kanishkaState.detailError = null
  kanishkaState.selectedPatient = null
  renderKanishkaList()
  renderKanishkaDetails()

  try {
    const patient = await apiFetch(`/patients/${ssn}`)
    kanishkaState.selectedPatient = patient
  } catch (error) {
    kanishkaState.detailError = error.message
  } finally {
    kanishkaState.detailLoading = false
    renderKanishkaList()
    renderKanishkaDetails()
  }
}

document.addEventListener("DOMContentLoaded", loadKanishkaPatients)

const akhilaState = {
  patients: [],
  appointments: [],
  selectedSsn: null,
  loading: false,
  error: null,
  detailError: null,
}

const akhilaPatientListEl = document.getElementById("patient-list")
const akhilaDetailsEl = document.getElementById("patient-details")

function akhilaInitials(name = "") {
  return name
    .split(" ")
    .filter(Boolean)
    .map((part) => part[0])
    .join("")
    .toUpperCase()
}

function renderAkhilaList() {
  if (!akhilaPatientListEl) return
  akhilaPatientListEl.innerHTML = ""

  if (akhilaState.loading) {
    akhilaPatientListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">Loading patients...</div>`
    return
  }

  if (akhilaState.error) {
    akhilaPatientListEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600 font-semibold">${akhilaState.error}</div>`
    return
  }

  if (!akhilaState.patients.length) {
    akhilaPatientListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">No patients found.</div>`
    return
  }

  akhilaState.patients.forEach((patient) => {
    const card = document.createElement("button")
    card.type = "button"
    card.className = "w-full text-left"
    const isActive = patient.ssn === akhilaState.selectedSsn
    card.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        isActive ? "border-rose-500 bg-rose-50 shadow-md" : "border-slate-200 bg-white hover:border-rose-300 hover:shadow-sm"
      }">
        <div class="flex items-start gap-4">
          <div class="size-12 rounded-xl bg-gradient-to-br from-rose-500 to-pink-500 flex items-center justify-center text-white font-bold text-lg shadow-md">
            ${akhilaInitials(patient.name || "Patient")}
          </div>
          <div class="flex-1 min-w-0">
            <div class="font-bold text-slate-900 truncate text-lg">${patient.name || "Unnamed Patient"}</div>
            <div class="flex items-center gap-3 mt-1 text-sm text-slate-600">
              <span>SSN: ${patient.ssn || "N/A"}</span>
              <span>${patient.phone || "No phone"}</span>
            </div>
          </div>
        </div>
      </div>
    `
    card.addEventListener("click", () => {
      akhilaState.selectedSsn = patient.ssn
      renderAkhilaList()
      renderAkhilaDetails()
    })
    akhilaPatientListEl.appendChild(card)
  })
}

function renderAkhilaDetails() {
  if (!akhilaDetailsEl) return
  akhilaDetailsEl.innerHTML = ""

  if (!akhilaState.selectedSsn) {
    akhilaDetailsEl.innerHTML = `
      <div class="p-16 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="size-20 rounded-2xl bg-rose-50 flex items-center justify-center mx-auto mb-5 border-2 border-rose-200">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10 text-rose-500">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </div>
        <div class="text-2xl font-bold mb-2 text-slate-900">Select a Patient</div>
        <div class="text-slate-600 max-w-sm mx-auto">
          Live data from the backend will display appointments and profile details.
        </div>
      </div>
    `
    return
  }

  const patient = akhilaState.patients.find((p) => p.ssn === akhilaState.selectedSsn)
  const appointments = akhilaState.appointments.filter((apt) => apt.patient && apt.patient.ssn === akhilaState.selectedSsn)

  if (!patient) {
    akhilaDetailsEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600">Patient data unavailable.</div>`
    return
  }

  const headerCard = document.createElement("div")
  headerCard.className = "p-6 mb-6 bg-gradient-to-br from-rose-50 to-pink-50 border-2 border-rose-200 rounded-3xl"
  headerCard.innerHTML = `
    <div class="flex items-center gap-5">
      <div class="size-20 rounded-2xl bg-gradient-to-br from-rose-500 to-pink-500 flex items-center justify-center text-white text-2xl font-bold shadow-lg">
        ${akhilaInitials(patient.name || "Patient")}
      </div>
      <div class="flex-1">
        <h2 class="text-3xl font-bold text-slate-900 mb-1">${patient.name || "Unnamed Patient"}</h2>
        <p class="text-sm text-slate-600">SSN: ${patient.ssn || "N/A"}</p>
        <p class="text-sm text-slate-600">${patient.address || "No address on file"}</p>
      </div>
    </div>
  `
  akhilaDetailsEl.appendChild(headerCard)

  const appointmentsWrapper = document.createElement("div")
  appointmentsWrapper.innerHTML = `<h3 class="text-xl font-bold mb-5 text-slate-900">Appointment History</h3>`

  if (!appointments.length) {
    appointmentsWrapper.innerHTML += `
      <div class="p-12 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="text-lg font-semibold text-slate-900 mb-1">No appointments found</div>
        <div class="text-slate-600">This patient has no recorded appointments.</div>
      </div>
    `
  } else {
    const list = document.createElement("div")
    list.className = "space-y-4"
    appointments.forEach((apt) => {
      list.innerHTML += `
        <div class="p-5 bg-white border-2 border-slate-200 hover:border-rose-300 transition-colors shadow-sm rounded-3xl">
          <div class="flex items-start justify-between mb-4">
            <div>
              <div class="font-bold text-lg text-slate-900 mb-1">Appointment #${apt.appointmentId}</div>
              <div class="text-sm text-slate-600 font-medium">Physician: ${apt.physician?.name || "N/A"}</div>
            </div>
            <div class="px-3 py-1.5 rounded-lg bg-emerald-100 border border-emerald-200 text-emerald-700 text-xs font-bold">SCHEDULED</div>
          </div>
          <div class="flex items-center gap-6 text-sm text-slate-600 flex-wrap">
            <span>Start: ${apt.start || "TBD"}</span>
            <span>End: ${apt.endTime || "TBD"}</span>
            <span>Room: ${apt.examinationRoom || "TBD"}</span>
          </div>
        </div>
      `
    })
    appointmentsWrapper.appendChild(list)
  }

  akhilaDetailsEl.appendChild(appointmentsWrapper)
}

async function loadAkhilaData() {
  if (!akhilaPatientListEl) return
  akhilaState.loading = true
  akhilaState.error = null
  renderAkhilaList()

  try {
    const [patients, appointments] = await Promise.all([apiFetch("/patients"), apiFetch("/appointments")])
    akhilaState.patients = patients
    akhilaState.appointments = appointments
    if (patients.length) {
      akhilaState.selectedSsn = patients[0].ssn
    }
  } catch (error) {
    akhilaState.error = error.message
  } finally {
    akhilaState.loading = false
    renderAkhilaList()
    renderAkhilaDetails()
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (akhilaPatientListEl && akhilaDetailsEl) {
    loadAkhilaData()
  }
})

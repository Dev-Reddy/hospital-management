const keerthanaState = {
  patients: [],
  appointments: [],
  selectedSsn: null,
  loading: false,
  error: null,
}

const keerthanaListEl = document.getElementById("keerthana-patients")
const keerthanaDetailsEl = document.getElementById("keerthana-details")

function renderKeerthanaList() {
  if (!keerthanaListEl) return
  keerthanaListEl.innerHTML = ""

  if (keerthanaState.loading) {
    keerthanaListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">Loading patients...</div>`
    return
  }

  if (keerthanaState.error) {
    keerthanaListEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600 font-semibold">${keerthanaState.error}</div>`
    return
  }

  if (!keerthanaState.patients.length) {
    keerthanaListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">No patients available.</div>`
    return
  }

  keerthanaState.patients.forEach((patient) => {
    const btn = document.createElement("button")
    btn.type = "button"
    btn.className = "w-full text-left"
    const active = patient.ssn === keerthanaState.selectedSsn
    btn.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        active ? "border-amber-500 bg-amber-50 shadow-md" : "border-slate-200 bg-white hover:border-amber-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${patient.name || "Unnamed Patient"}</div>
        <div class="text-sm text-slate-600">${patient.address || "No address"}</div>
        <div class="text-xs text-slate-500">SSN: ${patient.ssn || "N/A"}</div>
      </div>
    `
    btn.addEventListener("click", () => {
      keerthanaState.selectedSsn = patient.ssn
      renderKeerthanaList()
      renderKeerthanaDetails()
    })
    keerthanaListEl.appendChild(btn)
  })
}

function renderKeerthanaDetails() {
  if (!keerthanaDetailsEl) return
  keerthanaDetailsEl.innerHTML = ""

  if (!keerthanaState.selectedSsn) {
    keerthanaDetailsEl.innerHTML = `
      <div class="p-16 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="size-20 rounded-2xl bg-amber-50 flex items-center justify-center mx-auto mb-5 border-2 border-amber-200">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10 text-amber-500">
            <path d="M3 7h18"></path>
            <path d="M6 7v10"></path>
            <path d="M18 7v10"></path>
            <path d="M4 17h16"></path>
          </svg>
        </div>
        <div class="text-2xl font-bold mb-2 text-slate-900">Select a Patient</div>
        <div class="text-slate-600 max-w-sm mx-auto">Room assignments will display when we fetch appointment data.</div>
      </div>
    `
    return
  }

  const patient = keerthanaState.patients.find((p) => p.ssn === keerthanaState.selectedSsn)
  const relatedAppointments = keerthanaState.appointments
    .filter((apt) => apt.patient && apt.patient.ssn === keerthanaState.selectedSsn)
    .sort((a, b) => (b.start || "").localeCompare(a.start || ""))

  const latestAppointment = relatedAppointments[0]

  if (!patient) {
    keerthanaDetailsEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600">Patient data unavailable.</div>`
    return
  }

  const roomNumber = latestAppointment?.examinationRoom || "Awaiting assignment"

  keerthanaDetailsEl.innerHTML = `
    <div class="flex items-center gap-5 mb-6">
      <div class="size-24 rounded-2xl bg-gradient-to-br from-amber-500 to-orange-500 flex items-center justify-center text-white text-3xl font-black shadow-lg">
        ${roomNumber}
      </div>
      <div class="flex-1">
        <div class="text-sm text-amber-600 mb-1 font-bold uppercase tracking-wider">Room Assignment</div>
        <h2 class="text-3xl font-black text-slate-900">${patient.name || "Unnamed Patient"}</h2>
        <p class="text-sm text-slate-600">${patient.address || "No address"}</p>
      </div>
    </div>
    <div class="p-6 bg-gradient-to-br from-amber-50 to-orange-50 border-2 border-amber-200 rounded-3xl mb-6">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <div class="p-4 bg-white rounded-xl border border-amber-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Current Room</div>
          <div class="text-3xl font-black text-slate-900">${roomNumber}</div>
        </div>
        <div class="p-4 bg-white rounded-xl border border-amber-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Latest Appointment</div>
          <div class="text-xl font-bold text-slate-900">${latestAppointment ? latestAppointment.start || "Scheduled" : "Not Scheduled"}</div>
        </div>
      </div>
      <div class="p-4 bg-white rounded-xl border border-amber-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Assigned Physician</div>
        <div class="font-bold text-slate-900">${latestAppointment?.physician?.name || "Not Assigned"}</div>
      </div>
    </div>
    <h3 class="text-xl font-bold mb-5 text-slate-900">Recent Appointments</h3>
    ${
      relatedAppointments.length
        ? relatedAppointments
            .map(
              (apt) => `
        <div class="p-4 mb-3 bg-white border-2 border-slate-200 rounded-2xl">
          <div class="flex items-center justify-between mb-2">
            <div class="font-bold text-slate-900">Appointment #${apt.appointmentId}</div>
            <span class="text-xs font-bold px-3 py-1 rounded-lg bg-emerald-100 border border-emerald-200 text-emerald-700">ACTIVE</span>
          </div>
          <div class="text-sm text-slate-600">Room: ${apt.examinationRoom || "TBD"}</div>
          <div class="text-sm text-slate-600">Start: ${apt.start || "TBD"}</div>
        </div>
      `
            )
            .join("")
        : `<div class="p-12 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">No appointments mapped to this patient yet.</div>`
    }
  `
}

async function loadKeerthanaData() {
  if (!keerthanaListEl) return
  keerthanaState.loading = true
  keerthanaState.error = null
  renderKeerthanaList()

  try {
    const [patients, appointments] = await Promise.all([apiFetch("/patients"), apiFetch("/appointments")])
    keerthanaState.patients = patients
    keerthanaState.appointments = appointments
    if (patients.length) {
      keerthanaState.selectedSsn = patients[0].ssn
    }
  } catch (error) {
    keerthanaState.error = error.message
  } finally {
    keerthanaState.loading = false
    renderKeerthanaList()
    renderKeerthanaDetails()
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (keerthanaListEl && keerthanaDetailsEl) {
    loadKeerthanaData()
  }
})

const akhilaPatients = [
  { id: 1, name: "Sarah Johnson", age: 34, bloodType: "A+", phone: "(555) 123-4567" },
  { id: 2, name: "Michael Chen", age: 42, bloodType: "O-", phone: "(555) 234-5678" },
  { id: 3, name: "Emily Rodriguez", age: 28, bloodType: "B+", phone: "(555) 345-6789" },
  { id: 4, name: "James Wilson", age: 55, bloodType: "AB+", phone: "(555) 456-7890" },
  { id: 5, name: "Lisa Anderson", age: 31, bloodType: "A-", phone: "(555) 567-8901" },
]

const akhilaAppointments = {
  1: [
    { id: 1, date: "2024-01-15", time: "10:00 AM", doctor: "Dr. Smith", type: "Checkup" },
    { id: 2, date: "2024-01-22", time: "2:30 PM", doctor: "Dr. Johnson", type: "Follow-up" },
  ],
  2: [{ id: 3, date: "2024-01-18", time: "11:00 AM", doctor: "Dr. Williams", type: "Consultation" }],
  3: [
    { id: 4, date: "2024-01-20", time: "9:00 AM", doctor: "Dr. Brown", type: "Annual Physical" },
    { id: 5, date: "2024-02-05", time: "3:00 PM", doctor: "Dr. Davis", type: "Lab Results" },
  ],
  4: [{ id: 6, date: "2024-01-25", time: "1:00 PM", doctor: "Dr. Martinez", type: "Cardiology" }],
  5: [{ id: 7, date: "2024-01-28", time: "10:30 AM", doctor: "Dr. Garcia", type: "Dermatology" }],
}

const patientListEl = document.getElementById("patient-list")
const patientDetailsEl = document.getElementById("patient-details")
let selectedPatientId = null

function getInitials(name) {
  return name
    .split(" ")
    .map((n) => n[0])
    .join("")
}

function renderPatientList() {
  if (!patientListEl) return
  patientListEl.innerHTML = ""
  akhilaPatients.forEach((patient) => {
    const card = document.createElement("button")
    card.type = "button"
    card.className = "w-full text-left"
    const isActive = patient.id === selectedPatientId
    card.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        isActive
          ? "border-rose-500 bg-rose-50 shadow-md"
          : "border-slate-200 bg-white hover:border-rose-300 hover:shadow-sm"
      }">
        <div class="flex items-start gap-4">
          <div class="size-12 rounded-xl bg-gradient-to-br from-rose-500 to-pink-500 flex items-center justify-center text-white font-bold text-lg shadow-md">
            ${getInitials(patient.name)}
          </div>
          <div class="flex-1 min-w-0">
            <div class="font-bold text-slate-900 truncate text-lg">${patient.name}</div>
            <div class="flex items-center gap-3 mt-1 text-sm text-slate-600">
              <span class="flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="size-3.5">
                  <path d="M12 12c2.21 0 4-1.79 4-4S14.21 4 12 4 8 5.79 8 8s1.79 4 4 4z"></path>
                  <path d="M6 20v-1a6 6 0 0 1 12 0v1"></path>
                </svg>
                ${patient.age} yrs
              </span>
              <span class="flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="size-3.5">
                  <path d="M12 21s-8-4.5-8-11a8 8 0 0 1 16 0c0 6.5-8 11-8 11z"></path>
                </svg>
                ${patient.bloodType}
              </span>
            </div>
          </div>
        </div>
      </div>
    `
    card.addEventListener("click", () => {
      selectedPatientId = patient.id
      renderPatientList()
      renderPatientDetails()
    })
    patientListEl.appendChild(card)
  })
}

function renderPatientDetails() {
  if (!patientDetailsEl) return
  patientDetailsEl.innerHTML = ""
  if (!selectedPatientId) {
    patientDetailsEl.innerHTML = `
      <div class="p-16 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="size-20 rounded-2xl bg-rose-50 flex items-center justify-center mx-auto mb-5 border-2 border-rose-200">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10 text-rose-500">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </div>
        <div class="text-2xl font-bold mb-2 text-slate-900">Select a Patient</div>
        <div class="text-slate-600 max-w-sm mx-auto">
          Choose a patient from the directory to view their complete appointment history and medical records.
        </div>
      </div>
    `
    return
  }

  const patient = akhilaPatients.find((p) => p.id === selectedPatientId)
  const appointments = akhilaAppointments[selectedPatientId] || []

  const headerCard = document.createElement("div")
  headerCard.className = "p-6 mb-6 bg-gradient-to-br from-rose-50 to-pink-50 border-2 border-rose-200 rounded-3xl"
  headerCard.innerHTML = `
    <div class="flex items-center gap-5">
      <div class="size-20 rounded-2xl bg-gradient-to-br from-rose-500 to-pink-500 flex items-center justify-center text-white text-2xl font-bold shadow-lg">
        ${getInitials(patient.name)}
      </div>
      <div class="flex-1">
        <h2 class="text-3xl font-bold text-slate-900 mb-2">${patient.name}</h2>
        <div class="flex items-center gap-4 text-sm text-slate-600 flex-wrap">
          <span class="flex items-center gap-1.5 px-3 py-1 rounded-lg bg-white border border-slate-200">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
              <circle cx="12" cy="7" r="4"></circle>
            </svg>
            ${patient.age} years old
          </span>
          <span class="flex items-center gap-1.5 px-3 py-1 rounded-lg bg-white border border-slate-200">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4">
              <path d="M12 21s-8-4.5-8-11a8 8 0 0 1 16 0c0 6.5-8 11-8 11z"></path>
            </svg>
            Blood Type: ${patient.bloodType}
          </span>
          <span class="flex items-center gap-1.5 px-3 py-1 rounded-lg bg-white border border-slate-200">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4">
              <path d="M22 16.92V19a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6A19.79 19.79 0 0 1 2 3.18 2 2 0 0 1 4 1h2.09a2 2 0 0 1 2 1.72 12.44 12.44 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L7.09 8.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.44 12.44 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
            </svg>
            ${patient.phone}
          </span>
        </div>
      </div>
    </div>
  `
  patientDetailsEl.appendChild(headerCard)

  const appointmentsWrapper = document.createElement("div")
  appointmentsWrapper.innerHTML = `<h3 class="text-xl font-bold mb-5 text-slate-900">Appointment History</h3>`
  const list = document.createElement("div")
  list.className = "space-y-4"

  if (appointments.length === 0) {
    list.innerHTML = `
      <div class="p-12 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-16 text-slate-300 mx-auto mb-4">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
          <line x1="16" y1="2" x2="16" y2="6"></line>
          <line x1="8" y1="2" x2="8" y2="6"></line>
          <line x1="3" y1="10" x2="21" y2="10"></line>
        </svg>
        <div class="text-lg font-semibold text-slate-900 mb-1">No appointments found</div>
        <div class="text-slate-600">This patient has no appointment history</div>
      </div>
    `
  } else {
    appointments.forEach((apt) => {
      const card = document.createElement("div")
      card.className = "p-5 bg-white border-2 border-slate-200 hover:border-rose-300 transition-colors shadow-sm rounded-3xl"
      card.innerHTML = `
        <div class="flex items-start justify-between mb-4">
          <div>
            <div class="font-bold text-lg text-slate-900 mb-1">${apt.type}</div>
            <div class="text-sm text-slate-600 font-medium">${apt.doctor}</div>
          </div>
          <div class="px-3 py-1.5 rounded-lg bg-emerald-100 border border-emerald-200 text-emerald-700 text-xs font-bold">COMPLETED</div>
        </div>
        <div class="flex items-center gap-6 text-sm text-slate-600 flex-wrap">
          <div class="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-rose-50 border border-rose-200 text-rose-700">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
              <line x1="16" y1="2" x2="16" y2="6"></line>
              <line x1="8" y1="2" x2="8" y2="6"></line>
              <line x1="3" y1="10" x2="21" y2="10"></line>
            </svg>
            <span class="font-medium">${apt.date}</span>
          </div>
          <div class="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-pink-50 border border-pink-200 text-pink-700">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
            <span class="font-medium">${apt.time}</span>
          </div>
        </div>
      `
      list.appendChild(card)
    })
  }

  appointmentsWrapper.appendChild(list)
  patientDetailsEl.appendChild(appointmentsWrapper)
}

document.addEventListener("DOMContentLoaded", () => {
  if (!patientListEl || !patientDetailsEl) return
  renderPatientList()
  renderPatientDetails()
})

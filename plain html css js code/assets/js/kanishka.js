const kanishkaPatients = [
  { id: 1, name: "Sarah Johnson", condition: "Cardiac Assessment" },
  { id: 2, name: "Michael Chen", condition: "Orthopedic Evaluation" },
  { id: 3, name: "Emily Rodriguez", condition: "Neurological Screening" },
]

const kanishkaData = {
  1: {
    physician: { name: "Dr. Robert Smith", specialty: "Cardiology", experience: "15 years" },
    procedures: ["Echocardiogram", "Cardiac Catheterization", "Stress Test"],
  },
  2: {
    physician: { name: "Dr. Jennifer Williams", specialty: "Orthopedics", experience: "12 years" },
    procedures: ["Joint Replacement", "Arthroscopy", "Fracture Repair"],
  },
  3: {
    physician: { name: "Dr. David Brown", specialty: "Neurology", experience: "18 years" },
    procedures: ["EEG Interpretation", "Lumbar Puncture", "Nerve Conduction Study"],
  },
}

const kanishkaPatientListEl = document.getElementById("kanishka-patient-list")
const kanishkaDetailsEl = document.getElementById("kanishka-details")
let kanishkaSelectedId = null

function renderKanishkaPatients() {
  if (!kanishkaPatientListEl) return
  kanishkaPatientListEl.innerHTML = ""
  kanishkaPatients.forEach((patient) => {
    const wrapper = document.createElement("button")
    wrapper.type = "button"
    wrapper.className = "w-full text-left"
    const isActive = patient.id === kanishkaSelectedId
    wrapper.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        isActive
          ? "border-cyan-500 bg-cyan-50 shadow-md"
          : "border-slate-200 bg-white hover:border-cyan-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${patient.name}</div>
        <div class="text-sm text-slate-600">${patient.condition}</div>
      </div>
    `
    wrapper.addEventListener("click", () => {
      kanishkaSelectedId = patient.id
      renderKanishkaPatients()
      renderKanishkaDetails()
    })
    kanishkaPatientListEl.appendChild(wrapper)
  })
}

function renderKanishkaDetails() {
  if (!kanishkaDetailsEl) return
  kanishkaDetailsEl.innerHTML = ""
  if (!kanishkaSelectedId) {
    kanishkaDetailsEl.innerHTML = `
      <div class="p-16 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="size-20 rounded-2xl bg-cyan-50 flex items-center justify-center mx-auto mb-5 border-2 border-cyan-200">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10 text-cyan-500">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </div>
        <div class="text-2xl font-bold mb-2 text-slate-900">Select a Patient</div>
        <div class="text-slate-600 max-w-sm mx-auto">View assigned physician and their certified procedures.</div>
      </div>
    `
    return
  }

  const data = kanishkaData[kanishkaSelectedId]
  if (!data) return
  const card = document.createElement("div")
  card.className = "p-6 mb-6 bg-gradient-to-br from-cyan-50 to-blue-50 border-2 border-cyan-200 rounded-3xl"
  card.innerHTML = `
    <div class="flex items-start gap-5">
      <div class="size-20 rounded-2xl bg-gradient-to-br from-cyan-500 to-blue-500 flex items-center justify-center text-white shadow-lg">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10">
          <path d="M6 16v-4a4 4 0 0 1 8 0v4"></path>
          <circle cx="12" cy="8" r="4"></circle>
        </svg>
      </div>
      <div class="flex-1">
        <h3 class="text-3xl font-bold mb-2 text-slate-900">${data.physician.name}</h3>
        <div class="text-cyan-600 font-bold text-lg mb-2">${data.physician.specialty}</div>
        <div class="text-sm text-slate-600 font-medium">${data.physician.experience} of experience</div>
      </div>
    </div>
  `
  kanishkaDetailsEl.appendChild(card)

  const title = document.createElement("h3")
  title.className = "text-xl font-bold mb-5 text-slate-900"
  title.textContent = "Certified Procedures"
  kanishkaDetailsEl.appendChild(title)

  const list = document.createElement("div")
  list.className = "space-y-3"
  data.procedures.forEach((proc) => {
    const procCard = document.createElement("div")
    procCard.className = "p-5 bg-white border-2 border-slate-200 hover:border-cyan-300 transition-colors shadow-sm rounded-3xl"
    procCard.innerHTML = `
      <div class="flex items-center justify-between">
        <div class="font-bold text-lg text-slate-900">${proc}</div>
        <div class="px-3 py-1.5 rounded-lg bg-emerald-100 border border-emerald-200 text-emerald-700 text-xs font-bold">CERTIFIED</div>
      </div>
    `
    list.appendChild(procCard)
  })
  kanishkaDetailsEl.appendChild(list)
}

document.addEventListener("DOMContentLoaded", () => {
  if (!kanishkaPatientListEl || !kanishkaDetailsEl) return
  renderKanishkaPatients()
  renderKanishkaDetails()
})

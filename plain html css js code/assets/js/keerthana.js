const keerthanaPatients = [
  { id: 1, name: "Robert Williams", admitted: "2024-01-10", condition: "Post-Surgery Recovery" },
  { id: 2, name: "Linda Martinez", admitted: "2024-01-12", condition: "Pneumonia Treatment" },
  { id: 3, name: "David Lee", admitted: "2024-01-14", condition: "Cardiac Monitoring" },
  { id: 4, name: "Patricia Davis", admitted: "2024-01-15", condition: "Observation" },
]

const keerthanaRooms = {
  1: { number: "301", type: "Private", floor: "3rd Floor", features: ["Private Bath", "TV", "Wi-Fi"] },
  2: { number: "215", type: "Semi-Private", floor: "2nd Floor", features: ["Shared Bath", "TV"] },
  3: { number: "412", type: "ICU", floor: "4th Floor", features: ["24/7 Monitoring", "Advanced Equipment"] },
  4: { number: "108", type: "Standard", floor: "1st Floor", features: ["Shared Bath", "Basic Amenities"] },
}

const keerthanaListEl = document.getElementById("keerthana-patients")
const keerthanaDetailsEl = document.getElementById("keerthana-details")
let keerthanaSelectedId = null

function renderKeerthanaList() {
  if (!keerthanaListEl) return
  keerthanaListEl.innerHTML = ""
  keerthanaPatients.forEach((patient) => {
    const btn = document.createElement("button")
    btn.type = "button"
    btn.className = "w-full text-left"
    const active = patient.id === keerthanaSelectedId
    btn.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        active ? "border-amber-500 bg-amber-50 shadow-md" : "border-slate-200 bg-white hover:border-amber-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${patient.name}</div>
        <div class="text-sm text-slate-600 mb-1 font-medium">${patient.condition}</div>
        <div class="text-xs text-slate-500">Admitted: ${patient.admitted}</div>
      </div>
    `
    btn.addEventListener("click", () => {
      keerthanaSelectedId = patient.id
      renderKeerthanaList()
      renderKeerthanaDetails()
    })
    keerthanaListEl.appendChild(btn)
  })
}

function renderKeerthanaDetails() {
  if (!keerthanaDetailsEl) return
  keerthanaDetailsEl.innerHTML = ""
  if (!keerthanaSelectedId) {
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
        <div class="text-slate-600 max-w-sm mx-auto">View room assignment and accommodation details.</div>
      </div>
    `
    return
  }

  const patient = keerthanaPatients.find((p) => p.id === keerthanaSelectedId)
  const room = keerthanaRooms[keerthanaSelectedId]
  if (!patient || !room) return

  const header = document.createElement("div")
  header.className = "flex items-center gap-5 mb-6"
  header.innerHTML = `
    <div class="size-24 rounded-2xl bg-gradient-to-br from-amber-500 to-orange-500 flex items-center justify-center text-white text-3xl font-black shadow-lg">
      ${room.number}
    </div>
    <div class="flex-1">
      <div class="text-sm text-amber-600 mb-1 font-bold uppercase tracking-wider">Room Assignment</div>
      <h2 class="text-3xl font-black text-slate-900">${patient.name}</h2>
    </div>
  `
  keerthanaDetailsEl.appendChild(header)

  const card = document.createElement("div")
  card.className = "p-6 bg-gradient-to-br from-amber-50 to-orange-50 border-2 border-amber-200 rounded-3xl mb-6"
  card.innerHTML = `
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
      <div class="p-4 bg-white rounded-xl border border-amber-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Room Number</div>
        <div class="text-3xl font-black text-slate-900">${room.number}</div>
      </div>
      <div class="p-4 bg-white rounded-xl border border-amber-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Room Type</div>
        <div class="text-xl font-bold text-slate-900">${room.type}</div>
      </div>
    </div>
    <div class="p-4 bg-white rounded-xl border border-amber-200">
      <div class="text-sm text-slate-600 mb-1 font-semibold">Location</div>
      <div class="font-bold text-slate-900">${room.floor}</div>
    </div>
  `
  keerthanaDetailsEl.appendChild(card)

  const featuresTitle = document.createElement("h3")
  featuresTitle.className = "text-xl font-bold mb-5 text-slate-900"
  featuresTitle.textContent = "Room Features"
  keerthanaDetailsEl.appendChild(featuresTitle)

  const list = document.createElement("div")
  list.className = "space-y-3"
  room.features.forEach((feature) => {
    const featureCard = document.createElement("div")
    featureCard.className = "p-4 bg-white border-2 border-slate-200 rounded-2xl"
    featureCard.innerHTML = `
      <div class="flex items-center gap-3">
        <div class="size-3 rounded-full bg-amber-500"></div>
        <span class="font-medium text-slate-900">${feature}</span>
      </div>
    `
    list.appendChild(featureCard)
  })
  keerthanaDetailsEl.appendChild(list)
}

document.addEventListener("DOMContentLoaded", () => {
  if (!keerthanaListEl || !keerthanaDetailsEl) return
  renderKeerthanaList()
  renderKeerthanaDetails()
})

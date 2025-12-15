const samikshaPhysicians = [
  { id: 1, name: "Dr. Sarah Mitchell", specialty: "Cardiology", experience: "14 years" },
  { id: 2, name: "Dr. James Patterson", specialty: "Pediatrics", experience: "10 years" },
  { id: 3, name: "Dr. Maria Garcia", specialty: "Radiology", experience: "16 years" },
  { id: 4, name: "Dr. Thomas Anderson", specialty: "Surgery", experience: "20 years" },
]

const samikshaDepartments = {
  1: { name: "Cardiology Department", head: "Dr. Robert Smith", staff: 12, floor: "3rd Floor" },
  2: { name: "Pediatrics Department", head: "Dr. Emily Johnson", staff: 18, floor: "2nd Floor" },
  3: { name: "Radiology Department", head: "Dr. Michael Brown", staff: 8, floor: "Ground Floor" },
  4: { name: "Surgery Department", head: "Dr. Jennifer Wilson", staff: 25, floor: "4th Floor" },
}

const samikshaListEl = document.getElementById("samiksha-physicians")
const samikshaDetailsEl = document.getElementById("samiksha-details")
let samikshaSelectedId = null

function renderSamikshaList() {
  if (!samikshaListEl) return
  samikshaListEl.innerHTML = ""
  samikshaPhysicians.forEach((phys) => {
    const btn = document.createElement("button")
    btn.type = "button"
    btn.className = "w-full text-left"
    const active = phys.id === samikshaSelectedId
    btn.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        active ? "border-emerald-500 bg-emerald-50 shadow-md" : "border-slate-200 bg-white hover:border-emerald-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${phys.name}</div>
        <div class="text-sm text-slate-600">${phys.specialty}</div>
      </div>
    `
    btn.addEventListener("click", () => {
      samikshaSelectedId = phys.id
      renderSamikshaList()
      renderSamikshaDetails()
    })
    samikshaListEl.appendChild(btn)
  })
}

function renderSamikshaDetails() {
  if (!samikshaDetailsEl) return
  samikshaDetailsEl.innerHTML = ""
  if (!samikshaSelectedId) {
    samikshaDetailsEl.innerHTML = `
      <div class="p-16 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="size-20 rounded-2xl bg-emerald-50 flex items-center justify-center mx-auto mb-5 border-2 border-emerald-200">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10 text-emerald-500">
            <circle cx="12" cy="12" r="10"></circle>
            <path d="M12 16v-4"></path>
            <path d="M12 8h.01"></path>
          </svg>
        </div>
        <div class="text-2xl font-bold mb-2 text-slate-900">Select a Physician</div>
        <div class="text-slate-600 max-w-sm mx-auto">View department affiliation and organizational structure.</div>
      </div>
    `
    return
  }

  const physician = samikshaPhysicians.find((p) => p.id === samikshaSelectedId)
  const department = samikshaDepartments[samikshaSelectedId]
  if (!physician || !department) return

  const header = document.createElement("div")
  header.className = "flex items-center gap-5 mb-6"
  header.innerHTML = `
    <div class="size-20 rounded-2xl bg-gradient-to-br from-emerald-500 to-teal-500 flex items-center justify-center text-white shadow-lg">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10">
        <path d="M3 21V7a4 4 0 0 1 4-4h10a4 4 0 0 1 4 4v14"></path>
        <path d="M7 3v18"></path>
        <path d="M17 3v18"></path>
      </svg>
    </div>
    <div class="flex-1">
      <div class="text-sm text-emerald-600 mb-1 font-bold uppercase tracking-wider">Physician Details</div>
      <h2 class="text-3xl font-black text-slate-900">${physician.name}</h2>
      <p class="text-sm text-slate-600">${physician.specialty} • ${physician.experience}</p>
    </div>
  `
  samikshaDetailsEl.appendChild(header)

  const card = document.createElement("div")
  card.className = "p-6 bg-gradient-to-br from-emerald-50 to-teal-50 border-2 border-emerald-200 rounded-3xl"
  card.innerHTML = `
    <h3 class="text-xl font-bold mb-5 text-slate-900">Department Affiliation</h3>
    <div class="space-y-4">
      <div class="p-4 bg-white rounded-xl border border-emerald-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Department</div>
        <div class="text-2xl font-bold text-slate-900">${department.name}</div>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="p-4 bg-white rounded-xl border border-emerald-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Department Head</div>
          <div class="font-bold text-slate-900">${department.head}</div>
        </div>
        <div class="p-4 bg-white rounded-xl border border-emerald-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Staff Count</div>
          <div class="font-bold text-slate-900">${department.staff} members</div>
        </div>
        <div class="p-4 bg-white rounded-xl border border-emerald-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Location</div>
          <div class="font-bold text-slate-900">${department.floor}</div>
        </div>
      </div>
    </div>
  `
  samikshaDetailsEl.appendChild(card)
}

document.addEventListener("DOMContentLoaded", () => {
  if (!samikshaListEl || !samikshaDetailsEl) return
  renderSamikshaList()
  renderSamikshaDetails()
})

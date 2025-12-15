const mahithaNurses = [
  {
    id: 1,
    name: "Amanda Foster",
    department: "Emergency",
    certification: "RN, CCRN",
    experience: "8 years",
    phone: "(555) 111-2222",
    email: "a.foster@hospital.com",
    shift: "Day Shift",
  },
  {
    id: 2,
    name: "Marcus Thompson",
    department: "Pediatrics",
    certification: "RN, CPN",
    experience: "6 years",
    phone: "(555) 222-3333",
    email: "m.thompson@hospital.com",
    shift: "Night Shift",
  },
  {
    id: 3,
    name: "Rachel Kim",
    department: "Surgery",
    certification: "RN, CNOR",
    experience: "10 years",
    phone: "(555) 333-4444",
    email: "r.kim@hospital.com",
    shift: "Day Shift",
  },
  {
    id: 4,
    name: "Daniel Cooper",
    department: "ICU",
    certification: "RN, CCRN",
    experience: "12 years",
    phone: "(555) 444-5555",
    email: "d.cooper@hospital.com",
    shift: "Rotating",
  },
]

const mahithaListEl = document.getElementById("mahitha-nurses")
const mahithaDetailsEl = document.getElementById("mahitha-details")
let mahithaSelectedId = null

function getInitials(name) {
  return name
    .split(" ")
    .map((part) => part[0])
    .join("")
}

function renderMahithaList() {
  if (!mahithaListEl) return
  mahithaListEl.innerHTML = ""
  mahithaNurses.forEach((nurse) => {
    const btn = document.createElement("button")
    btn.type = "button"
    btn.className = "w-full text-left"
    const active = nurse.id === mahithaSelectedId
    btn.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        active ? "border-violet-500 bg-violet-50 shadow-md" : "border-slate-200 bg-white hover:border-violet-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${nurse.name}</div>
        <div class="text-sm text-slate-600">${nurse.department} · ${nurse.shift}</div>
      </div>
    `
    btn.addEventListener("click", () => {
      mahithaSelectedId = nurse.id
      renderMahithaList()
      renderMahithaDetails()
    })
    mahithaListEl.appendChild(btn)
  })
}

function renderMahithaDetails() {
  if (!mahithaDetailsEl) return
  mahithaDetailsEl.innerHTML = ""
  if (!mahithaSelectedId) {
    mahithaDetailsEl.innerHTML = `
      <div class="p-16 text-center border-2 border-dashed border-slate-200 bg-white rounded-3xl">
        <div class="size-20 rounded-2xl bg-violet-50 flex items-center justify-center mx-auto mb-5 border-2 border-violet-200">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10 text-violet-500">
            <circle cx="12" cy="12" r="10"></circle>
            <path d="M12 16v-4"></path>
            <path d="M12 8h.01"></path>
          </svg>
        </div>
        <div class="text-2xl font-bold mb-2 text-slate-900">Select a Nurse</div>
        <div class="text-slate-600 max-w-sm mx-auto">View nursing staff details and contact information.</div>
      </div>
    `
    return
  }

  const nurse = mahithaNurses.find((n) => n.id === mahithaSelectedId)
  if (!nurse) return

  const header = document.createElement("div")
  header.className = "flex items-center gap-5 mb-6"
  header.innerHTML = `
    <div class="size-20 rounded-full bg-gradient-to-br from-violet-500 to-purple-500 flex items-center justify-center text-white text-2xl font-bold shadow-lg">
      ${getInitials(nurse.name)}
    </div>
    <div class="flex-1">
      <h2 class="text-3xl font-black text-slate-900">${nurse.name}</h2>
      <div class="text-violet-600 font-bold text-lg">${nurse.certification}</div>
    </div>
  `
  mahithaDetailsEl.appendChild(header)

  const gridCard = document.createElement("div")
  gridCard.className = "p-6 bg-gradient-to-br from-violet-50 to-purple-50 border-2 border-violet-200 rounded-3xl mb-6"
  gridCard.innerHTML = `
    <div class="grid md:grid-cols-2 gap-4">
      <div class="p-4 bg-white rounded-xl border border-violet-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Department</div>
        <div class="text-lg font-bold text-slate-900">${nurse.department}</div>
      </div>
      <div class="p-4 bg-white rounded-xl border border-violet-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Experience</div>
        <div class="text-lg font-bold text-slate-900">${nurse.experience}</div>
      </div>
      <div class="p-4 bg-white rounded-xl border border-violet-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Shift</div>
        <div class="text-lg font-bold text-slate-900">${nurse.shift}</div>
      </div>
      <div class="p-4 bg-white rounded-xl border border-violet-200">
        <div class="text-sm text-slate-600 mb-1 font-semibold">Certifications</div>
        <div class="text-lg font-bold text-slate-900">${nurse.certification}</div>
      </div>
    </div>
  `
  mahithaDetailsEl.appendChild(gridCard)

  const contactTitle = document.createElement("h3")
  contactTitle.className = "text-xl font-bold mb-5 text-slate-900"
  contactTitle.textContent = "Contact Information"
  mahithaDetailsEl.appendChild(contactTitle)

  const phoneCard = document.createElement("div")
  phoneCard.className = "p-5 bg-white border-2 border-slate-200 rounded-3xl mb-3"
  phoneCard.innerHTML = `
    <div class="flex items-center gap-4">
      <div class="size-12 rounded-xl bg-violet-100 flex items-center justify-center">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-6 text-violet-600">
          <path d="M22 16.92V19a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6A19.79 19.79 0 0 1 2 3.18 2 2 0 0 1 4 1h2.09a2 2 0 0 1 2 1.72 12.44 12.44 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L7.09 8.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.44 12.44 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
        </svg>
      </div>
      <div>
        <div class="text-sm text-slate-600 font-semibold">Phone</div>
        <div class="font-bold text-slate-900">${nurse.phone}</div>
      </div>
    </div>
  `
  mahithaDetailsEl.appendChild(phoneCard)

  const emailCard = document.createElement("div")
  emailCard.className = "p-5 bg-white border-2 border-slate-200 rounded-3xl"
  emailCard.innerHTML = `
    <div class="flex items-center gap-4">
      <div class="size-12 rounded-xl bg-violet-100 flex items-center justify-center">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-6 text-violet-600">
          <path d="m4 4 8 8 8-8"></path>
          <path d="M2 6v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6"></path>
        </svg>
      </div>
      <div>
        <div class="text-sm text-slate-600 font-semibold">Email</div>
        <div class="font-bold text-slate-900">${nurse.email}</div>
      </div>
    </div>
  `
  mahithaDetailsEl.appendChild(emailCard)
}

document.addEventListener("DOMContentLoaded", () => {
  if (!mahithaListEl || !mahithaDetailsEl) return
  renderMahithaList()
  renderMahithaDetails()
})

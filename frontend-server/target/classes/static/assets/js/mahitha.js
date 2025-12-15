const mahithaListEl = document.getElementById("mahitha-nurses")
const mahithaDetailsEl = document.getElementById("mahitha-details")

const mahithaState = {
  nurses: [],
  selectedId: null,
  selectedNurse: null,
  loading: false,
  error: null,
  detailLoading: false,
  detailError: null,
}

function mahithaInitials(name = "") {
  return name
    .split(" ")
    .filter(Boolean)
    .map((part) => part[0])
    .join("")
    .toUpperCase()
}

function renderMahithaList() {
  if (!mahithaListEl) return
  mahithaListEl.innerHTML = ""

  if (mahithaState.loading) {
    mahithaListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">Loading nurses...</div>`
    return
  }

  if (mahithaState.error) {
    mahithaListEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600 font-semibold">${mahithaState.error}</div>`
    return
  }

  if (!mahithaState.nurses.length) {
    mahithaListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">No nurses found.</div>`
    return
  }

  mahithaState.nurses.forEach((nurse) => {
    const btn = document.createElement("button")
    btn.type = "button"
    btn.className = "w-full text-left"
    const active = nurse.employeeId === mahithaState.selectedId
    btn.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        active ? "border-violet-500 bg-violet-50 shadow-md" : "border-slate-200 bg-white hover:border-violet-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${nurse.name || "Unnamed Nurse"}</div>
        <div class="text-sm text-slate-600">${nurse.position || "No role"}</div>
      </div>
    `
    btn.addEventListener("click", () => selectMahithaNurse(nurse.employeeId))
    mahithaListEl.appendChild(btn)
  })
}

function renderMahithaDetails() {
  if (!mahithaDetailsEl) return
  mahithaDetailsEl.innerHTML = ""

  if (mahithaState.detailLoading) {
    mahithaDetailsEl.innerHTML = `<div class="p-16 text-center border-2 border-dashed border-slate-200 rounded-3xl bg-white">Loading nurse details...</div>`
    return
  }

  if (mahithaState.detailError) {
    mahithaDetailsEl.innerHTML = `<div class="p-16 text-center border-2 border-rose-200 rounded-3xl bg-rose-50 text-rose-600 font-semibold">${mahithaState.detailError}</div>`
    return
  }

  if (!mahithaState.selectedNurse) {
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
        <div class="text-slate-600 max-w-sm mx-auto">Fetch live nurse profiles and contact information.</div>
      </div>
    `
    return
  }

  const nurse = mahithaState.selectedNurse
  mahithaDetailsEl.innerHTML = `
    <div class="flex items-center gap-5 mb-6">
      <div class="size-20 rounded-full bg-gradient-to-br from-violet-500 to-purple-500 flex items-center justify-center text-white text-2xl font-bold shadow-lg">
        ${mahithaInitials(nurse.name || "Nurse")}
      </div>
      <div class="flex-1">
        <h2 class="text-3xl font-black text-slate-900">${nurse.name || "Unnamed Nurse"}</h2>
        <div class="text-violet-600 font-bold text-lg">${nurse.position || "No role"}</div>
      </div>
    </div>
    <div class="p-6 bg-gradient-to-br from-violet-50 to-purple-50 border-2 border-violet-200 rounded-3xl mb-6">
      <div class="grid md:grid-cols-2 gap-4">
        <div class="p-4 bg-white rounded-xl border border-violet-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Employee ID</div>
          <div class="text-lg font-bold text-slate-900">${nurse.employeeId}</div>
        </div>
        <div class="p-4 bg-white rounded-xl border border-violet-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Registered</div>
          <div class="text-lg font-bold text-slate-900">${nurse.registered ? "Yes" : "No"}</div>
        </div>
        <div class="p-4 bg-white rounded-xl border border-violet-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">SSN</div>
          <div class="text-lg font-bold text-slate-900">${nurse.ssn || "N/A"}</div>
        </div>
        <div class="p-4 bg-white rounded-xl border border-violet-200">
          <div class="text-sm text-slate-600 mb-1 font-semibold">Department ID</div>
          <div class="text-lg font-bold text-slate-900">${nurse.departmentId || "Unassigned"}</div>
        </div>
      </div>
    </div>
  `
}

async function loadMahithaNurses() {
  if (!mahithaListEl) return
  mahithaState.loading = true
  mahithaState.error = null
  renderMahithaList()

  try {
    const nurses = await apiFetch("/nurses")
    mahithaState.nurses = nurses
    if (nurses.length) {
      selectMahithaNurse(nurses[0].employeeId)
    } else {
      mahithaState.selectedNurse = null
      renderMahithaDetails()
    }
  } catch (error) {
    mahithaState.error = error.message
    renderMahithaList()
  } finally {
    mahithaState.loading = false
    renderMahithaList()
  }
}

async function selectMahithaNurse(id) {
  mahithaState.selectedId = id
  mahithaState.detailLoading = true
  mahithaState.detailError = null
  mahithaState.selectedNurse = null
  renderMahithaList()
  renderMahithaDetails()

  try {
    const nurse = await apiFetch(`/nurses/${id}`)
    mahithaState.selectedNurse = nurse
  } catch (error) {
    mahithaState.detailError = error.message
  } finally {
    mahithaState.detailLoading = false
    renderMahithaList()
    renderMahithaDetails()
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (mahithaListEl && mahithaDetailsEl) {
    loadMahithaNurses()
  }
})

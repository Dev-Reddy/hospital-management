const samikshaState = {
  physicians: [],
  departments: [],
  selectedId: null,
  loading: false,
  error: null,
}

const samikshaListEl = document.getElementById("samiksha-physicians")
const samikshaDetailsEl = document.getElementById("samiksha-details")

function renderSamikshaList() {
  if (!samikshaListEl) return
  samikshaListEl.innerHTML = ""

  if (samikshaState.loading) {
    samikshaListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">Loading physicians...</div>`
    return
  }

  if (samikshaState.error) {
    samikshaListEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600 font-semibold">${samikshaState.error}</div>`
    return
  }

  if (!samikshaState.physicians.length) {
    samikshaListEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white">No physicians found.</div>`
    return
  }

  samikshaState.physicians.forEach((phys) => {
    const btn = document.createElement("button")
    btn.type = "button"
    btn.className = "w-full text-left"
    const active = phys.employeeId === samikshaState.selectedId
    btn.innerHTML = `
      <div class="p-5 cursor-pointer transition-all duration-200 border-2 rounded-2xl ${
        active ? "border-emerald-500 bg-emerald-50 shadow-md" : "border-slate-200 bg-white hover:border-emerald-300 hover:shadow-sm"
      }">
        <div class="font-bold text-slate-900 mb-1 text-lg">${phys.name || "Unnamed Physician"}</div>
        <div class="text-sm text-slate-600">${phys.position || "No position"}</div>
      </div>
    `
    btn.addEventListener("click", () => {
      samikshaState.selectedId = phys.employeeId
      renderSamikshaList()
      renderSamikshaDetails()
    })
    samikshaListEl.appendChild(btn)
  })
}

function renderSamikshaDetails() {
  if (!samikshaDetailsEl) return
  samikshaDetailsEl.innerHTML = ""

  if (!samikshaState.selectedId) {
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
        <div class="text-slate-600 max-w-sm mx-auto">Live department relationships will render here.</div>
      </div>
    `
    return
  }

  const physician = samikshaState.physicians.find((p) => p.employeeId === samikshaState.selectedId)
  if (!physician) {
    samikshaDetailsEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600">Physician data unavailable.</div>`
    return
  }

  const dept = samikshaState.departments.find((d) => d.departmentId === physician.departmentId)

  samikshaDetailsEl.innerHTML = `
    <div class="flex items-center gap-5 mb-6">
      <div class="size-20 rounded-2xl bg-gradient-to-br from-emerald-500 to-teal-500 flex items-center justify-center text-white shadow-lg">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-10">
          <path d="M3 21V7a4 4 0 0 1 4-4h10a4 4 0 0 1 4 4v14"></path>
          <path d="M7 3v18"></path>
          <path d="M17 3v18"></path>
        </svg>
      </div>
      <div class="flex-1">
        <div class="text-sm text-emerald-600 mb-1 font-bold uppercase tracking-wider">Physician Details</div>
        <h2 class="text-3xl font-black text-slate-900">${physician.name || "Unnamed Physician"}</h2>
        <p class="text-sm text-slate-600">${physician.position || "No role"} • Employee ID: ${physician.employeeId}</p>
      </div>
    </div>
    <div class="p-6 bg-gradient-to-br from-emerald-50 to-teal-50 border-2 border-emerald-200 rounded-3xl">
      <h3 class="text-xl font-bold mb-5 text-slate-900">Department Affiliation</h3>
      ${
        dept
          ? `
        <div class="space-y-4">
          <div class="p-4 bg-white rounded-xl border border-emerald-200">
            <div class="text-sm text-slate-600 mb-1 font-semibold">Department</div>
            <div class="text-2xl font-bold text-slate-900">${dept.name || dept.depName || "Unnamed Department"}</div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div class="p-4 bg-white rounded-xl border border-emerald-200">
              <div class="text-sm text-slate-600 mb-1 font-semibold">Department Head</div>
              <div class="font-bold text-slate-900">${dept.head?.name || dept.headPhysicianName || "Not assigned"}</div>
            </div>
            <div class="p-4 bg-white rounded-xl border border-emerald-200">
              <div class="text-sm text-slate-600 mb-1 font-semibold">Department ID</div>
              <div class="font-bold text-slate-900">${dept.departmentId}</div>
            </div>
            <div class="p-4 bg-white rounded-xl border border-emerald-200">
              <div class="text-sm text-slate-600 mb-1 font-semibold">Department Number</div>
              <div class="font-bold text-slate-900">${dept.depNo || "N/A"}</div>
            </div>
          </div>
        </div>
      `
          : `<div class="text-slate-600">No department is linked to this physician yet.</div>`
      }
    </div>
  `
}

async function loadSamikshaData() {
  if (!samikshaListEl) return
  samikshaState.loading = true
  samikshaState.error = null
  renderSamikshaList()

  try {
    const [physicians, departments] = await Promise.all([apiFetch("/physicians"), apiFetch("/departments")])
    samikshaState.physicians = physicians
    samikshaState.departments = departments
    if (physicians.length) {
      samikshaState.selectedId = physicians[0].employeeId
    }
  } catch (error) {
    samikshaState.error = error.message
  } finally {
    samikshaState.loading = false
    renderSamikshaList()
    renderSamikshaDetails()
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (samikshaListEl && samikshaDetailsEl) {
    loadSamikshaData()
  }
})

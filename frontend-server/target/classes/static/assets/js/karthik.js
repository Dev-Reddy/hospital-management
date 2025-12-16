const karthikGridEl = document.getElementById("karthik-grid")
const karthikModal = document.getElementById("karthik-modal")
const karthikModalContent = document.getElementById("karthik-modal-content")

const karthikState = {
  procedures: [],
  loading: false,
  error: null,
}

function renderKarthikGrid() {
  if (!karthikGridEl) return
  karthikGridEl.innerHTML = ""

  if (karthikState.loading) {
    karthikGridEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white col-span-full">Loading procedures...</div>`
    return
  }

  if (karthikState.error) {
    karthikGridEl.innerHTML = `<div class="p-6 text-center border-2 border-rose-200 rounded-2xl bg-rose-50 text-rose-600 font-semibold col-span-full">${karthikState.error}</div>`
    return
  }

  if (!karthikState.procedures.length) {
    karthikGridEl.innerHTML = `<div class="p-6 text-center border-2 border-dashed border-slate-200 rounded-2xl bg-white col-span-full">No procedures found.</div>`
    return
  }

  karthikState.procedures.forEach((proc) => {
    const card = document.createElement("button")
    card.type = "button"
    card.className =
      "p-6 border-2 border-slate-200 bg-white hover:border-sky-300 hover:shadow-xl transition-all duration-300 group rounded-3xl text-left"
    card.innerHTML = `
      <div class="flex items-start justify-between mb-4">
        <div class="size-14 rounded-xl bg-indigo-100 border border-indigo-200 flex items-center justify-center shadow-sm">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="size-7 text-indigo-600">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v12m6-6H6" />
          </svg>
        </div>
        <div class="px-3 py-1.5 rounded-lg bg-indigo-100 text-indigo-700 text-xs font-bold uppercase tracking-wider border border-indigo-200">
          Code ${proc.code}
        </div>
      </div>
      <h3 class="text-xl font-bold mb-2 text-slate-900 group-hover:text-sky-600 transition-colors">${proc.name || "Unnamed Procedure"}</h3>
      <p class="text-sm text-slate-600 mb-4 leading-relaxed font-medium">Cost: $${proc.cost ?? "N/A"}</p>
      <div class="text-sm text-slate-500 font-medium">Click to view details</div>
    `
    card.addEventListener("click", () => openKarthikModal(proc.code))
    karthikGridEl.appendChild(card)
  })
}

async function openKarthikModal(code) {
  if (!karthikModal || !karthikModalContent) return
  karthikModal.classList.remove("hidden")
  karthikModalContent.innerHTML = `<div class="p-8 text-center">Loading procedure ${code}...</div>`

  try {
    const procedure = await apiFetch(`/procedures/${code}`)
    karthikModalContent.innerHTML = `
      <div class="p-8 bg-white rounded-3xl border-2 border-slate-200 shadow-2xl max-w-xl w-full">
        <div class="flex justify-between items-center mb-6">
          <div>
            <p class="text-sm text-slate-500 font-semibold">Procedure Code</p>
            <h3 class="text-3xl font-black text-slate-900">${procedure.code}</h3>
          </div>
          <button id="karthik-modal-close" class="size-10 rounded-full border border-slate-200 hover:bg-slate-50 flex items-center justify-center text-slate-500">
            ✕
          </button>
        </div>
        <h2 class="text-2xl font-bold text-slate-900 mb-4">${procedure.name || "Unnamed Procedure"}</h2>
        <p class="text-lg text-slate-600 mb-6">Cost: $${procedure.cost ?? "N/A"}</p>
        <p class="text-sm text-slate-500">Data loaded from backend at ${new Date().toLocaleTimeString()}.</p>
      </div>
    `
    document.getElementById("karthik-modal-close")?.addEventListener("click", closeKarthikModal)
  } catch (error) {
    karthikModalContent.innerHTML = `<div class="p-8 text-center text-rose-600 font-semibold">Failed to load procedure: ${error.message}</div>`
  }
}

function closeKarthikModal() {
  karthikModal?.classList.add("hidden")
}

async function loadKarthikProcedures() {
  if (!karthikGridEl) return
  karthikState.loading = true
  karthikState.error = null
  renderKarthikGrid()

  try {
    const procedures = await apiFetch("/procedures")
    karthikState.procedures = procedures
  } catch (error) {
    karthikState.error = error.message
  } finally {
    karthikState.loading = false
    renderKarthikGrid()
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (karthikGridEl) {
    loadKarthikProcedures()
    document.getElementById("karthik-modal-backdrop")?.addEventListener("click", (event) => {
      if (event.target.id === "karthik-modal-backdrop") {
        closeKarthikModal()
      }
    })
  }
})

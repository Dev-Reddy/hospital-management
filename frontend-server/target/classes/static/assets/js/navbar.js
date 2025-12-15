const ACTIVE_CLASSES = [
  "bg-gradient-to-r",
  "from-cyan-500",
  "to-blue-500",
  "text-white",
  "shadow-md",
]
const INACTIVE_CLASSES = ["text-slate-700", "hover:bg-slate-100", "hover:text-slate-900"]

document.addEventListener("DOMContentLoaded", () => {
  const route = document.body.dataset.route || "home"
  document.querySelectorAll("[data-nav-link]").forEach((link) => {
    const linkRoute = link.dataset.route
    if (linkRoute === route) {
      link.classList.add(...ACTIVE_CLASSES)
      link.classList.remove(...INACTIVE_CLASSES)
    } else {
      link.classList.remove(...ACTIVE_CLASSES)
      link.classList.add(...INACTIVE_CLASSES)
    }
  })
})

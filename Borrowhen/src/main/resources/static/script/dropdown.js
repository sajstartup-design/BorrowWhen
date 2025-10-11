document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll("select.dropdown").forEach((select) => createCustomDropdown(select));
});

function createCustomDropdown(originalSelect) {
  const options = Array.from(originalSelect.options);
  const wrapper = document.createElement("div");
  wrapper.className = "relative w-full";

  const selected = document.createElement("div");
  selected.className =
    "border border-gray-300 rounded-md h-8 px-2 py-1 text-sm cursor-pointer bg-white flex items-center justify-between";
  selected.textContent = options[0].textContent;

  const menu = document.createElement("div");
  menu.className =
    "absolute z-10 hidden top-full left-0 w-full bg-white border border-gray-300 rounded-md mt-1 shadow";

  const search = document.createElement("input");
  search.type = "text";
  search.placeholder = "Search...";
  search.className =
    "w-full border-b border-gray-200 px-2 py-1 text-sm outline-none focus:ring-1 focus:ring-indigo-500";
  menu.appendChild(search);

  const optionsContainer = document.createElement("div");
  menu.appendChild(optionsContainer);

  options.forEach((option) => {
    const item = document.createElement("div");
    item.textContent = option.textContent;
    item.dataset.value = option.value;
    item.className =
      "px-2 py-1 text-sm cursor-pointer hover:bg-gray-100 text-gray-700";

    item.addEventListener("click", () => {
      selected.textContent = item.textContent;
      originalSelect.value = item.dataset.value;
      menu.classList.add("hidden");

      // ✅ TRIGGER THE CHANGE EVENT
      const changeEvent = new Event("change", { bubbles: true });
      originalSelect.dispatchEvent(changeEvent);
    });

    optionsContainer.appendChild(item);
  });

  selected.addEventListener("click", () => {
    menu.classList.toggle("hidden");
    search.focus();
  });

  document.addEventListener("click", (e) => {
    if (!wrapper.contains(e.target)) menu.classList.add("hidden");
  });

  search.addEventListener("input", () => {
    const query = search.value.toLowerCase();
    optionsContainer.querySelectorAll("div").forEach((item) => {
      item.style.display = item.textContent.toLowerCase().includes(query)
        ? "block"
        : "none";
    });
  });

  originalSelect.style.display = "none";
  wrapper.appendChild(selected);
  wrapper.appendChild(menu);
  originalSelect.parentNode.insertBefore(wrapper, originalSelect.nextSibling);
}

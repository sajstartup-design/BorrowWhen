createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');
	const search = document.querySelector('.search');

    // Load first page
    loadInventories(0);

    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
			createLoadingScreenBody();
            let currentPage = Number(inputPage.value);
            loadInventories(currentPage); 
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
			createLoadingScreenBody();
            let currentPage = Number(inputPage.value);
            loadInventories(currentPage - 2); 
        });
    }

    if (inputPage) {
        inputPage.addEventListener('change', () => {
			createLoadingScreenBody();
            let newPage = Number(inputPage.value);
            if (newPage < 1) newPage = 1;
            inputPage.value = newPage;
            loadInventories(newPage - 1);
        });
    }
	
	if (search) {
	    let typingTimer; 
	    const delay = 500; 

	    search.addEventListener('input', function () {
	        clearTimeout(typingTimer); 

	        const currentPage = 0;
	        const searchValue = this.value;

	        typingTimer = setTimeout(() => {
				createLoadingScreenBody();
	            loadInventories(currentPage, searchValue);
	        }, delay);
	    });
	}

});


async function loadInventories(page = 0, search = "") {
  try {
    const params = new URLSearchParams({ page, search });
    const url = `/api/inventory?${params.toString()}`;
    const response = await fetch(url);
    const data = await response.json();

    const itemList = document.getElementById("itemList");
    itemList.innerHTML = "";

    const fragment = document.createDocumentFragment();

    if (data.inventories) {
      data.inventories.forEach((inventory) => {
        const isAvailable = inventory.availableQty > 0;
        const statusColor = isAvailable
          ? "text-green-700 bg-green-100"
          : "text-yellow-700 bg-yellow-100";
        const statusText = isAvailable ? "Available" : "Borrowed";

        // Borrow button
        const borrowBtn = isAvailable
          ? `
            <button
              class="px-3 py-1.5 bg-blue-500 text-white text-sm rounded-md hover:bg-blue-600 transition"
              data-toggle="modal"
              data-target="#borrowModal"
              data-id="${inventory.encryptedId}"
              data-name="${inventory.itemName}"
              data-price="${inventory.price}"
              data-qty="${inventory.availableQty}">
              Borrow
            </button>`
          : `
            <button
              class="px-3 py-1.5 bg-gray-300 text-gray-700 text-sm rounded-md cursor-not-allowed"
              disabled>
              Borrowed
            </button>`;

        // Card
        const card = document.createElement("div");
        card.className =
          "bg-gray-50 border border-gray-200 rounded-xl shadow-sm hover:shadow-md transition p-4";

        card.innerHTML = `
          <h2 class="text-gray-800 text-lg font-semibold mb-1">${inventory.itemName}</h2>

          <!-- Ratings -->
          <div class="flex mb-2 text-yellow-400">
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-regular fa-star text-gray-300"></i>
          </div>

          <p class="text-sm text-gray-600">Qty Left: ${inventory.availableQty}</p>
          <p class="text-sm text-gray-600 mb-2">Lender: ${inventory.owner}</p>

          <div class="flex justify-between items-center mt-3">
            <span class="px-2 py-0.5 text-xs font-semibold ${statusColor} rounded-full">
              ${statusText}
            </span>
            ${borrowBtn}
          </div>
        `;

        fragment.appendChild(card);
      });

      itemList.appendChild(fragment);

      // ✅ Add this line to reattach modal event listeners
      updateBtnsModal();
    }

    removeLoadingScreenBody();
  } catch (error) {
    console.error("Error fetching inventories:", error);
  }
}





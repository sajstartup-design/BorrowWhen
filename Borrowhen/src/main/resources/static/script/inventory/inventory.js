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
      const params = new URLSearchParams({
         page,
         search
      });
      const url = `/api/inventory?${params.toString()}`;
      const response = await fetch(url);
      const data = await response.json();

      const itemList = document.getElementById("itemList");
      itemList.innerHTML = "";

      const fragment = document.createDocumentFragment();

      if (data.inventories) {
         data.inventories.forEach((inventory) => {
            const isAvailable = inventory.availableQty > 0;
            const statusColor = isAvailable ?
               "text-green-700 bg-green-100" :
               "text-yellow-700 bg-yellow-100";
            const statusText = isAvailable ? "Available" : "Borrowed";

            // Borrow button
            const borrowBtn = isAvailable ?
               `
            <button
              class="h-8 px-2 py-1 bg-blue-500 text-white text-sm rounded-md hover:bg-blue-600 transition"
              data-toggle="modal"
              data-target="#borrowModal"
              data-id="${inventory.encryptedId}"
              data-name="${inventory.itemName}"
              data-price="${inventory.price}"
              data-qty="${inventory.availableQty}">
              Borrow
            </button>` :
               `
            <button
              class="h-8 px-2 py-1 bg-gray-300 text-gray-700 text-sm rounded-md cursor-not-allowed"
              disabled>
              Borrowed
            </button>`;

            // Card
            const card = document.createElement("div");
            card.className =
               "bg-gray-50 border border-gray-200 rounded-xl shadow-sm hover:shadow-md transition p-4";

            card.innerHTML = `
				  <div class="flex flex-col justify-between h-full">
				    <!-- Header -->
				    <div>
				      <h2 class="text-gray-800 text-base font-semibold mb-1">${inventory.itemName}</h2>
				
				      <!-- Rating -->
				      <div class="flex items-center gap-1 mb-3 text-yellow-400 text-xs">
				        <i class="fa-solid fa-star"></i>
				        <i class="fa-solid fa-star"></i>
				        <i class="fa-solid fa-star"></i>
				        <i class="fa-solid fa-star"></i>
				        <i class="fa-regular fa-star text-gray-300"></i>
				      </div>
				
				      <!-- Details -->
				      <div class="space-y-1 text-sm">
				        <p class="flex items-center gap-2 text-gray-500">
				          <i class="fa-solid fa-cubes text-gray-400"></i>
				          <span>Qty Left:</span>
				          <span class="font-medium text-gray-700">${inventory.availableQty}</span>
				        </p>
				        <p class="flex items-center gap-2 text-gray-500">
				          <i class="fa-solid fa-user text-gray-400"></i>
				          <span>Lender:</span>
				          <span class="font-medium text-gray-700">${inventory.owner}</span>
				        </p>
				      </div>
				    </div>
				
				    <!-- Footer -->
				    <div class="flex items-center justify-between mt-4 pt-3 border-t border-gray-100">
				      <span class="px-3 py-1 text-xs font-medium rounded-full ${statusColor} bg-opacity-10">
				        ${statusText}
				      </span>
				      ${borrowBtn}
				    </div>
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
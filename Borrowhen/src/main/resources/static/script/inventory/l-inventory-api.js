createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {

   const prevBtn = document.querySelector('.prev-btn');
   const nextBtn = document.querySelector('.next-btn');
   const inputPage = document.querySelector('.input-page');
   const pageBtns = document.querySelectorAll('.page-btn');
const endBtn = document.querySelector('.end-btn');
   const search = document.querySelector('.search');

   // Load first page
   loadInventories(0);
   
   if(pageBtns){
		pageBtns.forEach(btn => btn.addEventListener('click', function(){
			createLoadingScreenBody();
			const searchValue = search.value;
            loadInventories(Number(this.textContent.trim()) - 1, searchValue); 
		}));
	}
	
	if (endBtn) {
	  endBtn.addEventListener('click', function() {  // <-- regular function
	    createLoadingScreenBody();
	    const searchValue = search ? search.value : '';
	    console.log(this.textContent.trim());
	    loadInventories(Number(this.textContent.trim()) - 1, searchValue); 
	  });
	}

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


async function loadInventories(page = 0,
   search = ""
) {
   try {

      const params = new URLSearchParams({
         page,
         search
      });

      const url = `/api/lender/inventory?${params.toString()}`;
      const response = await fetch(url);

      const data = await response.json();

      updatePagination(data.pagination);

      const tableBody = document.getElementById("table-body");
      tableBody.innerHTML = '';

      const fragment = document.createDocumentFragment();

      console.log(data);

      data.inventories.forEach(inventory => {
         const row = document.createElement("tr");
         row.className = "text-gray-500 border-b border-gray-300";
         row.setAttribute("data-id", inventory.encryptedId);

         row.innerHTML = `
			  <td class="py-4 px-2 text-xs align-middle">
			    <div class="flex items-center justify-center">
			      <input type="checkbox" class="w-3 h-3 accent-indigo-500 rounded row-select-checkbox">
			    </div>
			  </td>
			  <td class="px-2 text-xs align-middle">${inventory.itemName}</td>
			  <td class="px-2 text-xs align-middle">₱${inventory.price}</td>
			  <td class="px-2 text-xs align-middle">${inventory.totalQty} pcs</td>
			  <td class="px-2 text-xs align-middle">${inventory.availableQty} pcs</td>
			  <td class="px-2 text-sm align-middle">
			    <div class="flex items-center gap-2 text-gray-500">

			      <!-- View Button -->
			      <div class="tooltip-wrapper">
			        <a
			          href="#"
			          class="view-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-blue-100 hover:bg-blue-200 transition shadow-sm"
			          data-id="${inventory.encryptedId}"
			          aria-label="View"
			        >
			          <img src="/images/view.png" alt="View" class="h-3 w-3" />
			        </a>
			        <span class="tooltip-text">View Item</span>
			      </div>

			      <!-- Edit Button -->
			      <div class="tooltip-wrapper">
			        <a
			          href="#"
			          class="edit-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-yellow-100 hover:bg-yellow-200 transition shadow-sm"
			          data-id="${inventory.encryptedId}"
			          aria-label="Edit"
			        >
			          <img src="/images/edit.png" alt="Edit" class="h-3 w-3" />
			        </a>
			        <span class="tooltip-text">Edit Item</span>
			      </div>

			      <!-- Delete Button -->
			      <div class="tooltip-wrapper">
			        <a
			          href="#"
			          class="delete-record border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-red-100 hover:bg-red-200 transition shadow-sm"
			          data-id="${inventory.encryptedId}"
			          aria-label="Delete"
			        >
			          <img src="/images/delete.png" alt="Delete" class="h-3 w-3" />
			        </a>
			        <span class="tooltip-text">Delete Item</span>
			      </div>

			    </div>
			  </td>

			`;

         row.querySelector('.edit-btn').addEventListener('click', function () {
            const form = document.querySelector('#editForm');

            form.querySelector('#hiddenEncryptedId').value = this.getAttribute('data-id');

            form.submit();
         });

         row.querySelector('.view-btn').addEventListener('click', function (e) {

            const encryptedId = this.getAttribute('data-id');

            window.location.href = "/lender/inventory/details?encryptedId=" + encryptedId;
         });


         fragment.appendChild(row);
      });

      tableBody.appendChild(fragment);

      document.querySelector(".input-page").value = data.pagination.page + 1;

      removeLoadingScreenBody();

   } catch (error) {
      console.error("Error fetching inventories:", error);
   }
}
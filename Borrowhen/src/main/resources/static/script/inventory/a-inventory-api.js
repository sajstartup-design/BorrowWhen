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


async function loadInventories(page = 0, search = "") {
  try {
    const params = new URLSearchParams({ page, search });
    const url = `/api/admin/inventory?${params.toString()}`;
    const response = await fetch(url);
    const data = await response.json();

    console.log(data);
    updatePagination(data.pagination);

    const tableBody = document.getElementById("table-body");
    tableBody.innerHTML = "";

    const fragment = document.createDocumentFragment();

    if (data.inventories && data.inventories.length > 0) {
      data.inventories.forEach((inventory) => {
        const row = document.createElement("tr");
        row.className =
          "border-b border-gray-200 hover:bg-gray-50 transition text-gray-600 text-xs";
        row.setAttribute("data-id", inventory.encryptedId);

        // Format date display
        const created = inventory.createdDate
          ? new Date(inventory.createdDate).toLocaleDateString()
          : "-";
        const updated = inventory.updatedDate
          ? new Date(inventory.updatedDate).toLocaleDateString()
          : "-";

        row.innerHTML = `
          <td class="py-3 px-2 text-center align-middle">
            <input type="checkbox" class="w-3 h-3 accent-blue-600 rounded row-select-checkbox" />
          </td>
		  <td class="px-4 align-middle">
		  			  <div class="flex items-center gap-3">
		  			    <div class="min-w-0">
		  			      <p class="text-sm font-semibold text-gray-500 truncate">${inventory.owner}</p>
		  			      <p class="text-xs text-gray-500 truncate">@${inventory.userId}</p>
		  			    </div>
		  			  </div>
		  			</td>
          <td class="py-3 px-2">${inventory.itemName}</td>
          <td class="py-3 px-2 font-semibold text-blue-600">₱${inventory.price}</td>
          <td class="py-3 px-2">${inventory.totalQty} pcs</td>
          <td class="py-3 px-2">${inventory.availableQty} pcs</td>
          <td class="py-3 px-2 text-gray-500">${created}</td>
          <td class="py-3 px-2 text-gray-500">${updated}</td>
         <td class="py-3 px-2 text-sm align-middle">
  <div class="flex items-center gap-2 text-gray-500">

    <!-- View Button -->
    <div class="tooltip-wrapper relative group">
      <a
        href="#"
        class="view-btn border border-gray-300 hover:bg-gray-200 flex items-center justify-center h-8 w-8 rounded-md bg-blue-100 hover:bg-blue-200 transition shadow-sm"
        data-id="${inventory.encryptedId}"
        aria-label="View"
      >
        <img src="/images/view.png" alt="View" class="h-3 w-3" />
      </a>
      <span
        class="tooltip-text absolute left-1/2 -translate-x-1/2 mt-1 px-2 py-1 text-xs text-white bg-gray-700 rounded opacity-0 group-hover:opacity-100 transition"
      >
        View Item
      </span>
    </div>

    <!-- Edit Button -->
    <div class="tooltip-wrapper relative group">
      <a
        href="#"
        class="edit-btn border border-gray-300 hover:bg-gray-200 flex items-center justify-center h-8 w-8 rounded-md bg-yellow-100 hover:bg-yellow-200 transition shadow-sm"
        data-id="${inventory.encryptedId}"
        aria-label="Edit"
      >
        <img src="/images/edit.png" alt="Edit" class="h-3 w-3" />
      </a>
      <span
        class="tooltip-text absolute left-1/2 -translate-x-1/2 mt-1 px-2 py-1 text-xs text-white bg-gray-700 rounded opacity-0 group-hover:opacity-100 transition"
      >
        Edit Item
      </span>
    </div>

    <!-- Delete Button -->
    <div class="tooltip-wrapper relative group">
      <a
        href="#"
        class="delete-record border border-gray-300 hover:bg-gray-200 flex items-center justify-center h-8 w-8 rounded-md bg-red-100 hover:bg-red-200 transition shadow-sm"
        data-id="${inventory.encryptedId}"
        aria-label="Delete"
      >
        <img src="/images/delete.png" alt="Delete" class="h-3 w-3" />
      </a>
      <span
        class="tooltip-text absolute left-1/2 -translate-x-1/2 mt-1 px-2 py-1 text-xs text-white bg-gray-700 rounded opacity-0 group-hover:opacity-100 transition"
      >
        Delete Item
      </span>
    </div>

  </div>
</td>


        `;

        // Attach event listeners
        row.querySelector(".edit-btn").addEventListener("click", function () {
          const form = document.querySelector("#editForm");
          form.querySelector("#hiddenEncryptedId").value =
            this.getAttribute("data-id");
          form.submit();
        });

        row.querySelector(".view-btn").addEventListener("click", function () {
          const encryptedId = this.getAttribute("data-id");
          window.location.href =
            "/admin/inventory/details?encryptedId=" + encryptedId;
        });

        fragment.appendChild(row);
      });

      tableBody.appendChild(fragment);
    } else {
      tableBody.innerHTML = `
        <tr>
          <td colspan="9" class="text-center py-6 text-gray-500 text-sm">
            No inventories found.
          </td>
        </tr>
      `;
    }

    document.querySelector(".input-page").value = data.pagination.page + 1;
    removeLoadingScreenBody();
  } catch (error) {
    console.error("Error fetching inventories:", error);
  }
}




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


async function loadInventories(page = 0, 
	search = ""
) {
    try {
		
		const params = new URLSearchParams({ page, search });
		
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
			    <!-- Checkbox -->
			    <td class="py-3 px-4 align-middle">
			      <input type="checkbox" class="w-5 h-5 accent-indigo-500 rounded row-select-checkbox">
			    </td>

			    <!-- Email -->
			    <td class="px-4 align-middle">${inventory.itemName}</td>

			    <!-- Created -->
			    <td class="px-4 align-middle">₱${inventory.price}</td>

			    <!-- Updated -->
			    <td class="px-4 align-middle">${inventory.totalQty} pcs</td>
				
				<!-- Updated -->
			    <td class="px-4 align-middle">${inventory.availableQty} pcs</td>

			    <!-- Actions -->
			    <td class="px-4 align-middle">
			      <div class="flex items-center gap-3 text-gray-500">
			        <!-- View -->
			        <button class="p-1 hover:text-gray-200 view-btn" data-id="${inventory.encryptedId}" aria-label="View">
			          <i class="fa-regular fa-eye text-[18px]"></i>
			        </button>

			        <!-- Edit -->
			        <button class="p-1 hover:text-gray-200 edit-btn" data-id="${inventory.encryptedId}" aria-label="Edit">
			          <i class="fa-regular fa-pen-to-square text-[18px]"></i>
			        </button>

			        <!-- Delete -->
			        <button class="p-1 hover:text-red-400 delete-record" data-id="${inventory.encryptedId}" aria-label="Delete">
			          <i class="fa-regular fa-trash-can text-[18px]"></i>
			        </button>
			      </div>
			    </td>
			  `;
			
			row.querySelector('.edit-btn').addEventListener('click', function(){
				const form = document.querySelector('#editForm');
				
				form.querySelector('#hiddenEncryptedId').value = this.getAttribute('data-id');
				
				form.submit();
			});
			
			row.querySelector('.view-btn').addEventListener('click', function(e) {
			   
				const encryptedId = this.getAttribute('data-id');

			    window.location.href="/lender/inventory/details?encryptedId=" + encryptedId;
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



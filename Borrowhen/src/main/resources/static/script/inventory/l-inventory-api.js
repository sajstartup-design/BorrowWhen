createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');

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

});


async function loadInventories(page = 0) {
    try {
        const response = await fetch(`/api/lender/inventory?page=${page}`);
        const data = await response.json();
		
        updatePagination(data.pagination);

        const tableBody = document.getElementById("table-body");
		tableBody.innerHTML = '';

        const fragment = document.createDocumentFragment();
        
        console.log(data);

        data.inventories.forEach(inventory => {
			const row = document.createElement("tr");
			row.classList.add("border-gray-200")
	        row.classList.add("border-b");
			row.setAttribute('data-id', inventory.encryptedId);
			
			row.innerHTML = `
				<td><input type="checkbox" class="checkbox checkbox-sm checkbox-primary row-select-checkbox" ></td>   
				<td>${inventory.itemName}</td>           
				<td>${inventory.price}</td>
				<td>${inventory.totalQty}</td>
              	<td>${inventory.availableQty}</td>
              	<td>
					<div class="flex items-center gap-2">
					  <button type="button" class="btn btn-circle btn-text btn-sm view-btn" data-id="${inventory.encryptedId}" title="View Details">
					    <span class="icon-[tabler--eye] size-5 text-base-content"></span>
					  </button>
	
					  <button type="button" class="btn btn-circle btn-text btn-sm edit-btn" data-id="${inventory.encryptedId}" title="Edit">
					    <span class="icon-[tabler--edit] size-5 text-base-content"></span>
					  </button>
	
					  <button type="button" class="btn btn-circle btn-text btn-sm delete-record" data-id="${inventory.encryptedId}" title="Delete">
					    <span class="icon-[tabler--trash] size-5 text-base-content"></span>
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



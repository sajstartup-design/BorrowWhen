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
		
		const url = `/api/inventory?${params.toString()}`;
		const response = await fetch(url);
        const data = await response.json();

        updatePagination(data.pagination);

        const tableBody = document.getElementById("items-body");
		tableBody.innerHTML = '';

        const fragment = document.createDocumentFragment();

		if(data.inventories){
			
	        data.inventories.forEach(inventory => {
				
				const item = document.createElement('div');
				item.classList.add("item-container");
				
				item.innerHTML = `<div class="item-details">
									<div class="ribbon green"><span>AVAILABLE</span></div>
									<span class="item-name">
									    ${inventory.itemName}
									</span>
									<span class="item-price">
										₱${inventory.price}
									</span>
									<span class="item-quantity">
										Quantity Left: ${inventory.availableQty}
									</span>
									<hr class="soft-gradient">
									<span class="lender-name">Lender: ${inventory.owner}</span>
								</div>
								<div class="item-btns">
									<button 
									  class="borrow-btn" 
									  data-toggle="modal" 
									  data-target="#borrowModal"
									  data-id="${inventory.encryptedId}"
									  data-name="${inventory.itemName}"
									  data-price="${inventory.price}"
									  data-qty="${inventory.availableQty}">
									  BORROW
									</button>
								</div>
								<div class="item-ratings">
									<span class="fa fa-star checked"></span>
									<span class="fa fa-star checked"></span>
									<span class="fa fa-star checked"></span>
									<span class="fa fa-star"></span>
									<span class="fa fa-star"></span>
								</div>`;
				
	            fragment.appendChild(item);
	        });
	
	        tableBody.appendChild(fragment);
			
			updateBtnsModal();
	
	        document.querySelector(".input-page").value = data.pagination.page + 1;
		}
		
		removeLoadingScreenBody();

    } catch (error) {
        console.error("Error fetching inventories:", error);
    }
}



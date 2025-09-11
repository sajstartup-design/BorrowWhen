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
        const response = await fetch(`/api/inventory?page=${page}`);
        const data = await response.json();
		
		console.log(data);

        updatePagination(data.pagination);

        const tableBody = document.getElementById("items-body");
		tableBody.innerHTML = '';

        const fragment = document.createDocumentFragment();

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
									Quantity Left: ${inventory.totalQty}
								</span>
								<hr class="soft-gradient">
								<span class="lender-name">Lender: ${inventory.owner}</span>
							</div>
							<div class="item-btns">
								<button class="borrow-btn" data-toggle="modal" data-target="#borrowModal">BORROW</button>
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
		
		removeLoadingScreenBody();

    } catch (error) {
        console.error("Error fetching inventories:", error);
    }
}



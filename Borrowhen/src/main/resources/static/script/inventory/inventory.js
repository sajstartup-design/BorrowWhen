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
			
            /*const row = document.createElement("div");
            row.classList.add("table-row");
			row.setAttribute('data-id', inventory.encryptedId);
			
            row.innerHTML = `
				<div class="table-cell">${inventory.owner}</div>
                <div class="table-cell">${inventory.itemName}</div>
                <div class="table-cell">₱${inventory.price}</div>
                <div class="table-cell">${inventory.totalQty}</div>
				<div class="table-cell">${inventory.totalQty}</div>
                <div class="table-cell">${inventory.createdDate}</div>
                <div class="table-cell">${inventory.updatedDate}</div>
                <div class="table-cell">
					<button class="edit-btn" data-id="${inventory.encryptedId}" type="submit"><img src="/images/edit.png"></button>           
                    <button><img src="/images/delete.png"></button>
                </div>
            `;
			
			row.querySelector('.edit-btn').addEventListener('click', function(){
				const form = document.querySelector('#editForm');
				
				form.querySelector('#hiddenEncryptedId').value = this.getAttribute('data-id');
				
				form.submit();
			});
			
			row.addEventListener('click', function(e) {
			   
			    if (e.target.closest('button') || e.target.closest('a')) {
			        return; 
			    }
				
				const encryptedId = this.getAttribute('data-id');

			    window.location.href="/admin/inventory/details?encryptedId=" + encryptedId;
			});*/


            fragment.appendChild(item);
        });

        tableBody.appendChild(fragment);

        document.querySelector(".input-page").value = data.pagination.page + 1;
		
		removeLoadingScreenBody();

    } catch (error) {
        console.error("Error fetching inventories:", error);
    }
}



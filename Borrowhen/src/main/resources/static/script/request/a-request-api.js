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
		
		const params = new URLSearchParams({ page });
				
		const url = `/api/admin/request?${params.toString()}`;
		
        const response = await fetch(url);
        const data = await response.json();
		
		console.log(data);
		
        updatePagination(data.pagination);

        const tableBody = document.getElementById("table-body");
		tableBody.innerHTML = '';

		const fragment = document.createDocumentFragment();

		data.requests.forEach(request => {
		    const row = document.createElement("div");
		    row.classList.add("table-row");
		    row.setAttribute('data-id', request.encryptedId);

		    // decide if buttons should be fake (disabled look)
		    const isFinalStatus = request.status?.toLowerCase() === "approved" || request.status?.toLowerCase() === "rejected";

		    row.innerHTML = `
		        <div class="table-cell">${request.borrower}</div>
		        <div class="table-cell">${request.lender}</div>
		        <div class="table-cell">${request.itemName}</div>
		        <div class="table-cell">₱${request.price}</div>
		        <div class="table-cell">${request.qty}</div>
		        <div class="table-cell">${request.dateToBorrow}</div>
		        <div class="table-cell">${request.dateToReturn}</div>
		        <div class="table-cell">
		          <span class="status ${request.status?.toLowerCase()}">
		            <span>${request.status}</span>
		          </span>
		        </div>
		        <div class="table-cell">${request.createdDate}</div>
		        <div class="table-cell">${request.updatedDate}</div>
		        <div class="table-cell">     
		          ${
		            isFinalStatus
		            ? `
		                <button class="fake-btn darker" disabled><img src="/images/approved.png"></button>
		                <button class="fake-btn darker" disabled><img src="/images/rejected.png"></button>
		              `
		            : `
		                <button 
		                  class="approve-btn darker" 
		                  data-toggle="modal" 
		                  data-target="#approveModal"
		                  data-id="${request.encryptedId}"
		                  data-item-name="${request.itemName}"
		                  data-price="${request.price}"
		                  data-borrower="${request.borrower}"
		                  data-date-to-borrow="${request.dateToBorrow}"
		                  data-date-to-return="${request.dateToReturn}"
		                  data-number-to-borrow="${request.qty}">
		                  <img src="/images/approved.png">
		                </button>
						<button 
		                  class="reject-btn darker" 
		                  data-toggle="modal" 
		                  data-target="#rejectModal"
		                  data-id="${request.encryptedId}"
		                  data-item-name="${request.itemName}"
		                  data-price="${request.price}"
		                  data-borrower="${request.borrower}"
		                  data-date-to-borrow="${request.dateToBorrow}"
		                  data-date-to-return="${request.dateToReturn}"
		                  data-number-to-borrow="${request.qty}">
		                  <img src="/images/rejected.png">
		                </button>
		              `
		          }
		          <button class="delete-btn darker" data-id="${request.encryptedId}">
		            <img src="/images/delete.png">
		          </button>
		        </div>
		    `;
					
			row.addEventListener('click', function(e) {
			   
			    if (e.target.closest('button') || e.target.closest('a')) {
			        return; 
			    }
				
				const encryptedId = this.getAttribute('data-id');

			    window.location.href="/admin/inventory/details?encryptedId=" + encryptedId;
			});


            fragment.appendChild(row);
        });

        tableBody.appendChild(fragment);
		
		updateBtnsModal();

        document.querySelector(".input-page").value = data.pagination.page + 1;
		
		removeLoadingScreenBody();

    } catch (error) {
        console.error("Error fetching inventories:", error);
    }
}



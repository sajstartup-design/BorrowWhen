createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');
	const search = document.querySelector('.search');

    // Load first page
    loadRequests(0);

    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
			createLoadingScreenBody();
			const searchValue = search.value;
            let currentPage = Number(inputPage.value);
            loadRequests(currentPage, searchValue); 
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
			createLoadingScreenBody();
			const searchValue = search.value;
            let currentPage = Number(inputPage.value);
            loadRequests(currentPage - 2, searchValue); 
        });
    }

    if (inputPage) {
        inputPage.addEventListener('change', () => {
			createLoadingScreenBody();
			const searchValue = search.value;
            let newPage = Number(inputPage.value);
            if (newPage < 1) newPage = 1;
            inputPage.value = newPage;
            loadRequests(newPage - 1, searchValue);
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
	            loadRequests(currentPage, searchValue);
	        }, delay);
	    });
	}

});

const buttons = {
    cancel: (request) => `
        <button class="delete-btn" 
                data-toggle="modal"
                data-target="#cancelModal"
                data-id="${request.encryptedId}"
                data-item-name="${request.itemName}"
                data-price="${request.price}"
                data-date-to-borrow="${request.dateToBorrow}"
                data-date-to-return="${request.dateToReturn}"
                data-number-to-borrow="${request.qty}">
            <img src="/images/cancelled.png">
        </button>
    `,
    received: (request) => `
        <button class="received-btn"
                data-toggle="modal"
                data-target="#confirmModal"
                data-id="${request.encryptedId}"
                data-item-name="${request.itemName}"
                data-price="${request.price}"
                data-date-to-borrow="${request.dateToBorrow}"
                data-date-to-return="${request.dateToReturn}"
                data-number-to-borrow="${request.qty}">
            <img src="/images/received-icon.png">
        </button>
    `,
    fake: (icon) => `<button class="fake-btn" disabled><img src="/images/${icon}.png"></button>`
};


async function loadRequests(page = 0,
	search = ""
) {
    try {
		
		const params = new URLSearchParams({ page, search });
				
		const url = `/api/request?${params.toString()}`;
		
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

            const status = request.status?.toLowerCase().replace(' ', '').trim();
            let actionButtons = '';

			if (status === 'pending') {
			    actionButtons = buttons.cancel(request) + buttons.fake('received-icon');
			} else if (status === 'pick-upready') {
			    actionButtons = buttons.fake('cancelled') + buttons.received(request);
			} else if (status === 'rejected' || 
				status === 'ongoing' || 
				status === 'approved' || 
				status === 'completed' ||
				status === 'paymentpending') {
			    actionButtons = buttons.fake('cancelled') + buttons.fake('received-icon');
			}
			
            row.innerHTML = `
                <div class="table-cell">${request.itemName}</div>
                <div class="table-cell">₱${request.price}</div>
                <div class="table-cell">${request.qty}</div>
                <div class="table-cell">${request.dateToBorrow}</div>
                <div class="table-cell">${request.dateToReturn}</div>
                <div class="table-cell">
                    <span class="status ${status}">
                        <span>${request.status}</span>
                    </span>
                </div>
                <div class="table-cell">
                    ${actionButtons}
                </div>
            `;

            row.addEventListener('click', function(e) {
                if (e.target.closest('button') || e.target.closest('a')) return;

                const encryptedId = this.getAttribute('data-id');
                window.location.href = "/admin/inventory/details?encryptedId=" + encryptedId;
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



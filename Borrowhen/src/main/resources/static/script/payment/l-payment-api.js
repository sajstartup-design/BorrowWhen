createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');
	const search = document.querySelector('.search');

    // Load first page
    loadPayments(0);

    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
			createLoadingScreenBody();
            let currentPage = Number(inputPage.value);
            loadPayments(currentPage); 
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
			createLoadingScreenBody();
            let currentPage = Number(inputPage.value);
            loadPayments(currentPage - 2); 
        });
    }

    if (inputPage) {
        inputPage.addEventListener('change', () => {
			createLoadingScreenBody();
            let newPage = Number(inputPage.value);
            if (newPage < 1) newPage = 1;
            inputPage.value = newPage;
            loadPayments(newPage - 1);
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
	            loadPayments(currentPage, searchValue);
	        }, delay);
	    });
	}

});


async function loadPayments(page = 0, search="") {
    try {
		
		const params = new URLSearchParams({ page, search });
						
		const url = `/api/lender/payment?${params.toString()}`;
		
        const response = await fetch(url);
        const data = await response.json();
		
		console.log(data);
		
        updatePagination(data.pagination);

        const tableBody = document.getElementById("table-body");
		tableBody.innerHTML = '';

        const fragment = document.createDocumentFragment();

        data.payments.forEach(payment => {
            const row = document.createElement("div");
            row.classList.add("table-row");
			row.setAttribute('data-id', payment.encryptedId);
			
			const status = payment.status?.toLowerCase().replace(' ', '').trim();
						
			// Default action cell
		    let actionCell = "";

		    if (payment.status?.toUpperCase() === "PAYMENT PENDING") {
		        actionCell = `<span>Awaiting Payment</span>`;
		    } else if (payment.status?.toUpperCase() === "PAID") {
		        actionCell = `<a class="button darker view-receipt" href="/payment/receipt?encryptedId=${payment.encryptedId}">VIEW RECEIPT</a>`;
		    }
			
            row.innerHTML = `
			<div class="table-cell">${payment.firstName} ${payment.familyName}</div>
				<div class="table-cell">${payment.emailAddress}</div>
                <div class="table-cell">${payment.itemName}</div>
                <div class="table-cell">₱${payment.price}</div>
                <div class="table-cell">${payment.qty} pcs</div>
				<div class="table-cell">₱${payment.totalAmount}</div>
				<div class="table-cell">${formatCheckoutDate(payment.dateCheckout)}</div>
				<div class="table-cell">${payment.paymentMethod}</div>
				<div class="table-cell">
		            <span class="status ${status}">
		                <span>${payment.status}</span>
		            </span>
		        </div>
                <div class="table-cell">
					${actionCell}
                </div>
            `;
			
            fragment.appendChild(row);
        });

        tableBody.appendChild(fragment);

        document.querySelector(".input-page").value = data.pagination.page + 1;
		
		document.querySelectorAll(".view-receipt").forEach(link => {
		    link.addEventListener("click", () => {
		        createLoadingScreenBody();
		    });
		});
		
		removeLoadingScreenBody();

    } catch (error) {
        console.error("Error fetching inventories:", error);
    }
}

function formatCheckoutDate(rawDate) {
  const date = new Date(rawDate);

  const options = {
    month: "short",   // Sep
    day: "numeric",   // 27
    year: "numeric",  // 2025
    hour: "numeric",  // 3
    hour12: true      // AM/PM format
  };

  // Format: "Sep 27, 2025, 3 PM"
  return date.toLocaleString("en-US", options).replace(",", "");
}




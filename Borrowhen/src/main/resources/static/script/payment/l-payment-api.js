createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    const inputPage = document.querySelector('.input-page');

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

});


async function loadPayments(page = 0) {
    try {
        const response = await fetch(`/api/lender/payment?page=${page}`);
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
			
            row.innerHTML = `
				<div class="table-cell">${payment.emailAddress}</div>
                <div class="table-cell">${payment.itemName}</div>
                <div class="table-cell">₱${payment.price}</div>
                <div class="table-cell">${payment.qty} pcs</div>
				<div class="table-cell">${payment.totalAmount}</div>
				<div class="table-cell">${formatCheckoutDate(payment.dateCheckout)}</div>
				<div class="table-cell">${payment.paymentMethod}</div>
                <div class="table-cell">
					<button>DOWNLOAD RECEIPT</button>
                </div>
            `;
			
            fragment.appendChild(row);
        });

        tableBody.appendChild(fragment);

        document.querySelector(".input-page").value = data.pagination.page + 1;
		
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




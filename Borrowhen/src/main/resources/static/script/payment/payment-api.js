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


async function loadPayments(page = 0, search = "") {
  try {
    const params = new URLSearchParams({ page, search });
    const url = `/api/payment?${params.toString()}`;

    const response = await fetch(url);
    const data = await response.json();

    updatePagination(data.pagination);
	
	console.log(data);

    const tableBody = document.getElementById("table-body");
    tableBody.innerHTML = "";

    const fragment = document.createDocumentFragment();

    if (data.payments && data.payments.length > 0) {
      data.payments.forEach((payment) => {
        const status = payment.status?.toLowerCase().replace(" ", "").trim();

        // Status color
        let statusColor = "";
        let statusTextColor = "";
        if (status === "paymentpending") {
          statusColor = "bg-pink-100";
	      statusTextColor = "text-pink-700";
        } else if (status === "paid") {
          statusColor = "bg-emerald-100";
	      statusTextColor = "text-emerald-700";
        } else {
          statusColor = "bg-gray-100";
          statusTextColor = "text-gray-700";
        }
		
        // Action button
        let actionCell = "";
        if (payment.status?.toUpperCase() === "PAYMENT PENDING") {
          actionCell = `
            <a href="/payment?encryptedId=${payment.encryptedBorrowRequestId}"
              class="px-3 py-1.5 text-xs font-medium rounded-md bg-blue-500 text-white hover:bg-indigo-600 transition">
              Pay Now
            </a>`;
        } else if (payment.status?.toUpperCase() === "PAID") {
          actionCell = `
            <a href="/payment/receipt?encryptedId=${payment.encryptedId}"
              class="px-3 py-1.5 text-xs font-medium rounded-md bg-gray-200 text-gray-700 hover:bg-gray-300 transition">
              View Receipt
            </a>`;
        } else {
          actionCell = `
            <span class="px-3 py-1.5 text-xs font-medium text-gray-400">N/A</span>`;
        }

        // Row
        const row = document.createElement("div");
        row.className =
          "table-row hover:bg-gray-50 transition border-b border-gray-100 text-gray-500 text-xs";
        row.setAttribute("data-id", payment.encryptedId);

        row.innerHTML = `
		<td class="py-2 px-2 text-xs align-middle text-gray-500">
						    <div class="flex items-center justify-center">
						      <input type="checkbox" class="w-3 h-3 accent-indigo-500 rounded row-select-checkbox">
						    </div>
						  </td>
          <div class="table-cell py-3 px-2">${payment.itemName}</div>
          <div class="table-cell py-3 px-2">₱${payment.price}</div>
          <div class="table-cell py-3 px-2">${payment.qty} pcs</div>
          <div class="table-cell py-3 px-2">₱${payment.totalAmount}</div>
          <div class="table-cell py-3 px-2">
            <span class="px-3 py-1 text-xs font-medium rounded-full ${statusColor} ${statusTextColor}">
              ${payment.status}
            </span>
          </div>
          <div class="table-cell py-3 px-2 flex items-center gap-2">
            ${actionCell}
          </div>
        `;

        fragment.appendChild(row);
      });

      tableBody.appendChild(fragment);
    } else {
      tableBody.innerHTML = `
        <div class="table-row text-center py-6 text-gray-500 text-sm">
          <div class="table-cell" colspan="6">No payments found.</div>
        </div>
      `;
    }

    document.querySelector(".input-page").value = data.pagination.page + 1;

    removeLoadingScreenBody();
  } catch (error) {
    console.error("Error fetching payments:", error);
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




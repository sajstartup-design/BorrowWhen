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

        search.addEventListener('input', function() {
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

// Helper functions to generate buttons
const buttons = {
    approve: (request) => `
    <div class="tooltip-wrapper">
      <button 
        class="approve-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-green-100 hover:bg-green-200 transition shadow-sm"
        data-toggle="modal" 
        data-target="#approveModal"
        data-id="${request.encryptedId}"
        data-item-name="${request.itemName}"
        data-price="${request.price}"
        data-date-to-borrow="${request.dateToBorrow}"
        data-date-to-return="${request.dateToReturn}"
        data-number-to-borrow="${request.qty}">
        <img src="/images/approved.png" alt="Approve" class="h-4 w-4" />
      </button>
      <span class="tooltip-text">Approve Request</span>
    </div>
  `,

    reject: (request) => `
    <div class="tooltip-wrapper">
      <button 
        class="reject-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-red-100 hover:bg-red-200 transition shadow-sm"
        data-toggle="modal" 
        data-target="#rejectModal"
        data-id="${request.encryptedId}"
        data-item-name="${request.itemName}"
        data-price="${request.price}"
        data-date-to-borrow="${request.dateToBorrow}"
        data-date-to-return="${request.dateToReturn}"
        data-number-to-borrow="${request.qty}">
        <img src="/images/rejected.png" alt="Reject" class="h-4 w-4" />
      </button>
      <span class="tooltip-text">Reject Request</span>
    </div>
  `,

    returnBtn: (request) => `
    <div class="tooltip-wrapper">
      <button 
        class="return-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-yellow-100 hover:bg-yellow-200 transition shadow-sm"
        data-toggle="modal" 
        data-target="#confirmModal"
        data-id="${request.encryptedId}"
        data-item-name="${request.itemName}"
        data-price="${request.price}"
        data-date-to-borrow="${request.dateToBorrow}"
        data-date-to-return="${request.dateToReturn}"
        data-number-to-borrow="${request.qty}">
        <img src="/images/return-box.png" alt="Return" class="h-4 w-4" />
      </button>
      <span class="tooltip-text">Confirm Return</span>
    </div>
  `,

    payBtn: (request) => `
    <div class="tooltip-wrapper">
      <button 
        class="pay-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-blue-100 hover:bg-blue-200 transition shadow-sm"
        data-toggle="modal" 
        data-target="#issuePaymentModal"
        data-id="${request.encryptedId}"
        data-item-name="${request.itemName}"
        data-price="${request.price}"
        data-date-to-borrow="${request.dateToBorrow}"
        data-date-to-return="${request.dateToReturn}"
        data-number-to-borrow="${request.qty}">
        <img src="/images/credit-cards.png" alt="Payment" class="h-4 w-4" />
      </button>
      <span class="tooltip-text">Issue Payment</span>
    </div>
  `,

    pickUpBtn: (request) => `
    <div class="tooltip-wrapper">
      <button 
        class="pick-up-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-indigo-100 hover:bg-indigo-200 transition shadow-sm"
        data-toggle="modal" 
        data-target="#pickUpModal"
        data-id="${request.encryptedId}"
        data-item-name="${request.itemName}"
        data-price="${request.price}"
        data-date-to-borrow="${request.dateToBorrow}"
        data-date-to-return="${request.dateToReturn}"
        data-number-to-borrow="${request.qty}">
        <img src="/images/location.png" alt="Pick Up" class="h-4 w-4" />
      </button>
      <span class="tooltip-text">Mark as Ready for Pick-Up</span>
    </div>
  `,

    fake: (icon) => `
    <div class="tooltip-wrapper">
      <button 
        class="fake-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-gray-100 opacity-60 cursor-not-allowed shadow-sm pointer-events-none">
        <img src="/images/${icon}.png" alt="${icon}" class="h-4 w-4" />
      </button>
      <span class="tooltip-text">Unavailable</span>
    </div>
  `
};



async function loadRequests(page = 0, search = "") {
    try {
        const params = new URLSearchParams({
            page,
            search
        });
        const url = `/api/lender/request?${params.toString()}`;

        const response = await fetch(url);
        const data = await response.json();

        console.log(data);

        updatePagination(data.pagination);

        const tableBody = document.getElementById("table-body");
        tableBody.innerHTML = "";

        const fragment = document.createDocumentFragment();

        if (data.requests && data.requests.length > 0) {
            data.requests.forEach((request) => {
                const row = document.createElement("tr");
                row.className = "hover:bg-gray-50 transition";
                row.setAttribute("data-id", request.encryptedId);

                const status = request.status?.toLowerCase().replace(" ", "").trim();

                // ✅ Tailwind Status colors
                let statusColor = "";
                let statusTextColor = "";
                if (status === "pending") {
                    statusColor = "bg-yellow-100";
                    statusTextColor = "text-yellow-700";
                } else if (status === "approved" || status === "ongoing" || status === "completed") {
                    statusColor = "bg-green-100";
                    statusTextColor = "text-green-700";
                } else if (status === "rejected") {
                    statusColor = "bg-red-100";
                    statusTextColor = "text-red-700";
                } else {
                    statusColor = "bg-gray-100";
                    statusTextColor = "text-gray-700";
                }

                // ✅ Action Buttons
                let actionButtons = "";
                if (status === "pending") {
                    actionButtons =
                        buttons.approve(request) +
                        buttons.reject(request) +
                        buttons.fake("location") +
                        buttons.fake("return-box") +
                        buttons.fake("credit-cards");
                } else if (status === "approved") {
                    actionButtons =
                        buttons.fake("approved") +
                        buttons.fake("rejected") +
                        buttons.pickUpBtn(request) +
                        buttons.fake("return-box") +
                        buttons.fake("credit-cards");
                } else if (status === "ongoing") {
                    actionButtons =
                        buttons.fake("approved") +
                        buttons.fake("rejected") +
                        buttons.fake("location") +
                        buttons.returnBtn(request) +
                        buttons.fake("credit-cards");
                } else if (status === "completed") {
                    actionButtons =
                        buttons.fake("approved") +
                        buttons.fake("rejected") +
                        buttons.fake("location") +
                        buttons.fake("return-box") +
                        buttons.payBtn(request);
                } else {
                    actionButtons =
                        buttons.fake("approved") +
                        buttons.fake("rejected") +
                        buttons.fake("location") +
                        buttons.fake("return-box") +
                        buttons.fake("credit-cards");
                }

                // ✅ Row content with Tailwind styling
                row.innerHTML = `
					<td class="py-2 px-2 text-xs align-middle text-gray-500">
									    <div class="flex items-center justify-center">
									      <input type="checkbox" class="w-3 h-3 accent-indigo-500 rounded row-select-checkbox">
									    </div>
									  </td>	
			          <td class="px-2 text-xs align-middle text-gray-500">${request.borrower}</td>
			          <td class="px-2 text-xs align-middle text-gray-500">${request.itemName}</td>
			          <td class="px-2 text-xs align-middle text-gray-500">₱${request.price}</td>
			          <td class="px-2 text-xs align-middle text-gray-500">${request.qty}</td>
			          <td class="px-2 text-xs align-middle text-gray-500">${request.dateToBorrow}</td>
			          <td class="px-2 text-xs align-middle text-gray-500">${request.dateToReturn}</td>
			          <td class="py-2 px-2 text-gray-500">
			            <span class="px-3 py-1 text-xs font-medium rounded-full ${statusColor} ${statusTextColor}">
			              ${request.status}
			            </span>
			          </td>
			          <td class="py-2 px-2 text-sm flex items-center gap-2 text-gray-500">
			            ${actionButtons}
			          </td>
			        `;

                // ✅ Click listener (ignore buttons)
                row.addEventListener("click", function(e) {
                    if (e.target.closest("button") || e.target.closest("a")) return;
                    const encryptedId = this.getAttribute("data-id");
                    window.location.href = "/lender/request/details?encryptedId=" + encryptedId;
                });

                fragment.appendChild(row);
            });

            tableBody.appendChild(fragment);
            updateBtnsModal();
        } else {
            tableBody.innerHTML = `
        <tr>
          <td colspan="8" class="text-center py-6 text-gray-500 text-sm">No requests found.</td>
        </tr>
      `;
        }

        document.querySelector(".input-page").value = data.pagination.page + 1;

        removeLoadingScreenBody();
    } catch (error) {
        console.error("Error fetching inventories:", error);
    }
}
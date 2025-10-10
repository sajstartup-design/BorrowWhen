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
const buttons = {
    cancel: (request) => `
    <div class="tooltip-wrapper">
      <a
        href="#"
        class="delete-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-red-100 hover:bg-red-200 transition shadow-sm"
        data-toggle="modal"
        data-target="#cancelModal"
        data-id="${request.encryptedId}"
        data-item-name="${request.itemName}"
        data-price="${request.price}"
        data-date-to-borrow="${request.dateToBorrow}"
        data-date-to-return="${request.dateToReturn}"
        data-number-to-borrow="${request.qty}">
        <img src="/images/cancelled.png" alt="Cancel" class="h-4 w-4" />
      </a>
      <span class="tooltip-text">Cancel Request</span>
    </div>
  `,

    received: (request) => `
    <div class="tooltip-wrapper">
      <a
        href="#"
        class="received-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-green-100 hover:bg-green-200 transition shadow-sm"
        data-toggle="modal"
        data-target="#confirmModal"
        data-id="${request.encryptedId}"
        data-item-name="${request.itemName}"
        data-price="${request.price}"
        data-date-to-borrow="${request.dateToBorrow}"
        data-date-to-return="${request.dateToReturn}"
        data-number-to-borrow="${request.qty}">
        <img src="/images/received-icon.png" alt="Received" class="h-4 w-4" />
      </a>
      <span class="tooltip-text">Mark as Received</span>
    </div>
  `,

    fake: (icon) => `
    <div class="tooltip-wrapper">
      <a
        href="#"
        class="fake-btn border border-gray-300 hover:bg-gray-200 shadow-md flex items-center justify-center h-8 w-8 rounded-md bg-gray-100 opacity-60 cursor-not-allowed shadow-sm pointer-events-none">
        <img src="/images/${icon}.png" alt="${icon}" class="h-4 w-4" />
      </a>
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
        const url = `/api/request?${params.toString()}`;
        const response = await fetch(url);
        const data = await response.json();

        const tableBody = document.getElementById("table-body");
        tableBody.innerHTML = "";

        const fragment = document.createDocumentFragment();

        if (data.requests && data.requests.length > 0) {
            data.requests.forEach((request) => {
                const status = request.status?.toLowerCase().replace(" ", "").trim();

                // Status style
                let statusColor = "";
                let statusTextColor = "";
                if (status === "pending") {
                    statusColor = "bg-yellow-100";
                    statusTextColor = "text-yellow-700";
                } else if (status === "approved" || status === "pick-upready" || status === "completed") {
                    statusColor = "bg-green-100";
                    statusTextColor = "text-green-700";
                } else if (status === "rejected") {
                    statusColor = "bg-red-100";
                    statusTextColor = "text-red-700";
                } else {
                    statusColor = "bg-gray-100";
                    statusTextColor = "text-gray-700";
                }

                // Action Buttons
                let actionButtons = "";
                if (status === "pending") {
                    actionButtons = buttons.cancel(request) + buttons.fake("received-icon");
                } else if (status === "pick-upready") {
                    actionButtons = buttons.fake("cancelled") + buttons.received(request);
                } else {
                    actionButtons = buttons.fake("cancelled") + buttons.fake("received-icon");
                }

                // Row
                const row = document.createElement("tr");
                row.className = "hover:bg-gray-50 transition";
                row.setAttribute("data-id", request.encryptedId);


                row.innerHTML = `
				  <td class="py-2 px-2 text-xs align-middle text-gray-500">
				    <div class="flex items-center justify-center">
				      <input type="checkbox" class="w-3 h-3 accent-indigo-500 rounded row-select-checkbox">
				    </div>
				  </td>
				  <td class="px-2 text-xs align-middle text-gray-500">${request.itemName}</td>
				  <td class="px-2 text-xs align-middle text-gray-500">₱${request.price}</td>
				  <td class="px-2 text-xs align-middle text-gray-500">${request.qty} pcs</td>
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


                // Click listener
                /*row.addEventListener("click", function (e) {
                   if (e.target.closest("button") || e.target.closest("a")) return;
                   const encryptedId = this.getAttribute("data-id");
                   window.location.href = "/admin/inventory/details?encryptedId=" + encryptedId;
                });*/

                fragment.appendChild(row);
            });

            tableBody.appendChild(fragment);
            updateBtnsModal();
        } else {
            tableBody.innerHTML = `
            <tr>
               <td colspan="7" class="text-center py-6 text-gray-500 text-sm">No requests found.</td>
            </tr>
         `;
        }

        removeLoadingScreenBody();
    } catch (error) {
        console.error("Error fetching requests:", error);
    }
}
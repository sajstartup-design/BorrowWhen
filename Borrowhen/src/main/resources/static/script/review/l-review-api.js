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
    const url = `/api/lender/reviews?${params.toString()}`;

    const response = await fetch(url);
    const data = await response.json();

    updatePagination(data.pagination);
    console.log(data);

    const tableBody = document.getElementById("table-body");
    tableBody.innerHTML = "";

    const fragment = document.createDocumentFragment();

	if (data.requests && data.requests.length > 0) {
	            data.requests.forEach((request) => {
	                const row = document.createElement("tr");
	                row.className = "hover:bg-gray-50 transition border-b border-gray-300";
	                row.setAttribute("data-id", request.encryptedId);


	                // ✅ Row content with Tailwind styling
	                row.innerHTML = `
						<td class="py-2 px-2 text-xs align-middle text-gray-500">
						  <div class="flex items-center justify-center">
						    <input type="checkbox" class="w-3 h-3 accent-indigo-500 rounded row-select-checkbox">
						  </div>
						</td>
						<td class="px-2 text-xs align-middle text-gray-500">
						  <div class="flex items-center gap-3">
						    <div class="min-w-0">
						      <p class="text-sm font-semibold text-gray-500 truncate">${request.borrower}</p>
						      <p class="text-xs text-gray-500 truncate">@${request.borrowerUserId}</p>
						    </div>
						  </div>
						</td>
						<td class="px-2 text-xs align-middle text-gray-500">${request.itemName}</td>
						<td class="px-2 text-xs align-middle text-gray-500">
						  ${request.rating === 0 ? '-' : request.rating}
						</td>
						<td class="px-2 text-xs align-middle text-gray-500">
						  ${!request.feedback || request.feedback.trim() === '' ? '-' : request.feedback}
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
	          <td colspan="9" class="text-center py-6 text-gray-500 text-sm">No reviews found.</td>
	        </tr>
	      `;
	        }

	        document.querySelector(".input-page").value = data.pagination.page + 1;

	        removeLoadingScreenBody();
	    } catch (error) {
	        console.error("Error fetching inventories:", error);
	    }
		
		// ===============================
		// CSV EXPORT FOR REQUESTS TABLE
		// ===============================

		const exportRequestsBtn = document.querySelector('button.bg-blue-200'); 
		if (exportRequestsBtn) {
		    exportRequestsBtn.addEventListener('click', () => {
		        const tableBody = document.getElementById("table-body");
		        if (!tableBody) return;

		        let csv = [];

		        // CSV Headers
		        const headers = [
		            "BORROWER",
		            "USERNAME",
		            "ITEM NAME",
		            "RATING",
		            "FEEDBACK"
		        ];
		        csv.push(headers.map(h => `"${h}"`).join(','));

		        // Extract table rows
		        tableBody.querySelectorAll("tr").forEach(row => {

		            const cells = row.querySelectorAll("td");
		            if (cells.length < 5) return; // match your table column count

		            // Borrower info (name + username)
		            const borrowerBlock = cells[1].innerText.split("\n").map(t => t.trim()).filter(Boolean);
		            const borrowerName = borrowerBlock[0] || "";
		            const borrowerUsername = borrowerBlock[1] ? borrowerBlock[1].replace("@","") : "";

		            // Format row data
		            const rowData = [
		                borrowerName,
		                borrowerUsername,
		                cells[2].innerText.trim(),        // item name
		                cells[3].innerText.trim(),        // rating
		                cells[4].innerText.trim()         // feedback
		            ].map(text => `"${text.replace(/"/g, '""')}"`);

		            csv.push(rowData.join(','));
		        });

		        // Download CSV
		        const blob = new Blob([csv.join('\n')], { type: 'text/csv;charset=utf-8;' });
		        const link = document.createElement('a');
		        link.href = URL.createObjectURL(blob);
		        link.download = 'lender-reviews.csv';
		        link.style.visibility = 'hidden';
		        document.body.appendChild(link);
		        link.click();
		        document.body.removeChild(link);
		    });
		}


	}



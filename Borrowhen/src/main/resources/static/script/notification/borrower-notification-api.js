createLoadingScreenBody();

document.addEventListener("DOMContentLoaded", () => {
    const prevBtn = document.querySelector(".prev-btn");
    const nextBtn = document.querySelector(".next-btn");
    const inputPage = document.querySelector(".input-page");
    const search = document.querySelector(".search");
    const dateInput = $(".select-date");
    const statusSelect = $("select.status-filter");
	const pageBtns = document.querySelectorAll('.page-btn');
	const endBtn = document.querySelector('.end-btn');
	
	if (pageBtns) {
        pageBtns.forEach(btn => btn.addEventListener('click', function() {
            createLoadingScreenBody();
			const { startDate, endDate, status } = getFilters();
			loadNotifications(this.textContent.trim() - 1, startDate, endDate, status);
        }));
    }
	
	if (endBtn) {
	        endBtn.addEventListener('click', function() { 
	            createLoadingScreenBody();
	            loadNotifications(this.textContent.trim() - 1, startDate, endDate, status);
	        });
	    }

    // Define ranges
    const today = moment().date();
    const ranges = {
        'Today': [moment(), moment()],
        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
        'This Month': [moment().startOf('month'), moment().endOf('month')],
        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
    };

    if(today <= 15){
        ranges['1st-15th'] = [moment().startOf('month'), moment().date(15)];
    } else {
        ranges['16th-EOM'] = [moment().date(16), moment().endOf('month')];
    }

    // Initialize daterangepicker
    dateInput.daterangepicker({
        opens: 'center',
        autoApply: true,
        autoUpdateInput: true,
        alwaysShowCalendars: true,
        applyButtonClasses: 'action-btn',
        cancelButtonClasses: 'action-btn',
        ranges: ranges,
        locale: {
            format: 'MM-DD-YYYY',
            applyLabel: "✔ CONFIRM",
            cancelLabel: "❌ CLOSE"
        },
        startDate: moment().startOf('month'),
        endDate: moment().endOf('month')
    });

    const picker = dateInput.data('daterangepicker');

    // Helper to get current start/end and status
    const getFilters = () => ({
        startDate: picker.startDate.format('YYYY-MM-DD'),
        endDate: picker.endDate.format('YYYY-MM-DD'),
        status: statusSelect.val() || "ALL"
    });

    // Load first page
    const { startDate, endDate, status } = getFilters();
    loadNotifications(0, startDate, endDate, status);

    // Pagination buttons
    if(nextBtn) nextBtn.addEventListener("click", () => {
        createLoadingScreenBody();
        const currentPage = Number(inputPage.value);
        const { startDate, endDate, status } = getFilters();
        loadNotifications(currentPage, startDate, endDate, status);
    });

    if(prevBtn) prevBtn.addEventListener("click", () => {
        createLoadingScreenBody();
        const currentPage = Number(inputPage.value);
        const { startDate, endDate, status } = getFilters();
        loadNotifications(currentPage - 2, startDate, endDate, status);
    });

    // Page input
    if(inputPage) inputPage.addEventListener("change", () => {
        createLoadingScreenBody();
        let newPage = Number(inputPage.value);
        if(newPage < 1) newPage = 1;
        inputPage.value = newPage;
        const { startDate, endDate, status } = getFilters();
        loadNotifications(newPage - 1, startDate, endDate, status);
    });

    // Search input
    if(search) {
        let typingTimer;
        const delay = 500;
        search.addEventListener("input", function () {
            clearTimeout(typingTimer);
            const searchValue = this.value;
            typingTimer = setTimeout(() => {
                createLoadingScreenBody();
                const { startDate, endDate, status } = getFilters();
                loadNotifications(0, startDate, endDate, status, searchValue);
            }, delay);
        });
    }

    // Status change
    statusSelect.on("change", () => {
        createLoadingScreenBody();
        const { startDate, endDate, status } = getFilters();
        loadNotifications(0, startDate, endDate, status);
    });

    // Date range change
    dateInput.on('apply.daterangepicker', function(ev, picker) {
        createLoadingScreenBody();
        const { startDate, endDate, status } = getFilters();
        loadNotifications(0, startDate, endDate, status);
    });
});
	
	
async function loadNotifications(page = 0, startDate = "", endDate = "", status = "", search = "") {
    try {
        const params = new URLSearchParams({ page, startDate, endDate, status, search });
        const url = `/api/notifications/borrower?${params.toString()}`;
        const response = await fetch(url);
        const data = await response.json();

        updatePagination(data.pagination);

        const tableBody = document.getElementById("table-body");
        tableBody.innerHTML = ""; // clear previous rows

        if (data.notifications && data.notifications.length) {
            data.notifications.forEach(notification => {
                const tr = document.createElement("tr");

                // Unread notifications get light blue, all have hover pointer + gray effect
                tr.className = `
                    border-b border-gray-100
                    ${!notification.isRead ? "bg-blue-50" : ""}
                    hover:bg-gray-100 cursor-pointer
                `;

                // Determine icon based on notification type
                let iconClass = "fa-info-circle";
                let colorClass = "text-gray-500";

                switch (notification.type) {
                    case "REQUEST_PENDING":
                        iconClass = "fa-hourglass-half"; colorClass = "text-yellow-500"; break;
                    case "REQUEST_APPROVED":
                        iconClass = "fa-check-circle"; colorClass = "text-green-500"; break;
                    case "REQUEST_REJECTED":
                        iconClass = "fa-times-circle"; colorClass = "text-red-500"; break;
                    case "NEW_ITEM":
                        iconClass = "fa-box"; colorClass = "text-blue-500"; break;
                    case "ITEM_RECEIVED":
                        iconClass = "fa-box-open"; colorClass = "text-indigo-500"; break;
                    case "REQUEST_PICKUP_READY":
                        iconClass = "fa-location-dot"; colorClass = "text-purple-500"; break;
                    case "REQUEST_PAYMENT_PENDING":
                        iconClass = "fa-credit-card"; colorClass = "text-pink-500"; break;
                    case "OVERDUE":
                        iconClass = "fa-exclamation-triangle"; colorClass = "text-orange-500"; break;
                }

                tr.innerHTML = `
                    <td class="px-4 py-2 flex items-start gap-3">
                        <i class="fa-solid ${iconClass} ${colorClass} mt-1"></i>
                        <div class="flex flex-col text-sm text-gray-700 leading-tight">
                            <span>${notification.message}</span>
                            <span class="text-[11px] text-gray-400 mt-0.5">${notification.dateAndTime}</span>
                        </div>
                    </td>
                `;

                tableBody.appendChild(tr);
            });
        } else {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td class="text-gray-400 text-sm py-2 px-3" colspan="2">No notifications found.</td>
            `;
            tableBody.appendChild(tr);
        }

        removeLoadingScreenBody();
    } catch (error) {
        console.error("Error loading notifications:", error);
        removeLoadingScreenBody();
    }
}



	
	$(document).ready(function() {
	    const dateInput = $(".select-date");
	    const statusSelect = $("select.status-filter");

	    // Define ranges
	    const today = moment().date();
	    const ranges = {
	        'Today': [moment(), moment()],
	        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
	        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
	        'This Month': [moment().startOf('month'), moment().endOf('month')],
	        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
	    };

	    if(today <= 15){
	        ranges['1st-15th'] = [moment().startOf('month'), moment().date(15)];
	    } else {
	        ranges['16th-EOM'] = [moment().date(16), moment().endOf('month')];
	    }

	    // Initialize daterangepicker
	    dateInput.daterangepicker({
	        opens: 'center',
	        autoApply: true,
	        autoUpdateInput: true,
	        alwaysShowCalendars: true,
	        applyButtonClasses: 'action-btn',
	        cancelButtonClasses: 'action-btn',
	        ranges: ranges,
	        locale: {
	            format: 'MM-DD-YYYY',
	            applyLabel: "✔ CONFIRM",
	            cancelLabel: "❌ CLOSE"
	        },
	        startDate: moment().startOf('month'),
	        endDate: moment().endOf('month')
	    });

	    // Now it's safe to read start and end date
	    const picker = dateInput.data('daterangepicker');
	    const startDate = picker.startDate.format('YYYY-MM-DD');
	    const endDate = picker.endDate.format('YYYY-MM-DD');
	    const status = statusSelect.val() || "ALL";

	    // Load first page with date range and status
	    loadNotifications(0, startDate, endDate, status);

	    // Listen to daterangepicker changes
	    dateInput.on('apply.daterangepicker', function(ev, picker) {
	        const newStart = picker.startDate.format('YYYY-MM-DD');
	        const newEnd = picker.endDate.format('YYYY-MM-DD');
	        const currentStatus = statusSelect.val() || "ALL";
	        createLoadingScreenBody();
	        loadNotifications(0, newStart, newEnd, currentStatus);
	    });

	    // Status change
	    statusSelect.on('change', function() {
	        const newStatus = $(this).val();
	        const currentStart = picker.startDate.format('YYYY-MM-DD');
	        const currentEnd = picker.endDate.format('YYYY-MM-DD');
	        createLoadingScreenBody();
	        loadNotifications(0, currentStart, currentEnd, newStatus);
	    });
	});

	 
	

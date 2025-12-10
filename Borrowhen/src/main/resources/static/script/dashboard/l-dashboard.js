document.addEventListener("DOMContentLoaded", async () => {
	
	createLoadingScreenBody();
	
  const url = "/lender/dashboard/retrieve";

  try {
    const response = await fetch(url);

    if (!response.ok) {
      console.error("❌ Failed to fetch dashboard data");
      return;
    }

    const webDto = await response.json();
    console.log("✅ Dashboard Data:", webDto);

    const { overdues, paymentPendings, ongoingRequests, notifications, lenderDashboardOverview, popularItems} = webDto;

    const overdueList = document.getElementById('overdue-list');
    const paymentPendingList = document.getElementById('payment-pending-list');
    const notificationsList = document.getElementById('notifications-list');
	
	overdueList.innerHTML = '';
	paymentPendingList.innerHTML = '';
	notificationsList.innerHTML = '';

    // 🟥 Overdues
    if (overdues && overdues.length > 0) {

      const frag = document.createDocumentFragment();
      overdues.forEach(overdue => {
        const div = document.createElement('div');
        div.className = 'flex justify-between items-center border-b border-gray-300 pb-2';
        div.innerHTML = `
          <span class="text-gray-700 text-sm">${overdue.itemName}</span>
          <span class="text-red-500 font-semibold text-sm">${overdue.dateTimeAgo}</span>
        `;
        frag.appendChild(div);
      });
      overdueList.appendChild(frag);
    } else {
      const noData = document.createElement('div');
      noData.classList.add('text-gray-500', 'text-sm', 'italic');
      noData.textContent = 'No Overdue Found';
      overdueList.appendChild(noData);
    }

    // 🟨 Payment Pendings
    if (paymentPendings && paymentPendings.length > 0) {

      const frag = document.createDocumentFragment();
      paymentPendings.forEach(pending => {
		const div = document.createElement('div');
		div.className = 'flex justify-between items-center border-b border-gray-300 pb-2';
		div.innerHTML = `
		  <span class="text-gray-700 text-sm">${pending.itemName} <span class="text-gray-500">(x${pending.qty})</span></span>
		  <span class="text-yellow-500 font-semibold text-sm">₱ ${pending.amount}</span>
		`;
        frag.appendChild(div);
      });
      paymentPendingList.appendChild(frag);
    } else {
      const noData = document.createElement('div');
      noData.classList.add('text-gray-500', 'text-sm', 'italic');
      noData.textContent = 'No Request Found';
      paymentPendingList.appendChild(noData);
    }

    // 🔔 Notifications
    if (notifications && notifications.length > 0) {
	
      const frag = document.createDocumentFragment();

      notifications.forEach(notification => {
        const li = document.createElement("li");
        li.className = "hover:bg-gray-100 flex items-start gap-3 border-b border-gray-100";

        let iconClass = "fa-info-circle";
        let colorClass = "text-gray-500";

        switch (notification.type) {
          case "REQUEST_PENDING": iconClass = "fa-hourglass-half"; colorClass = "text-yellow-500"; break;
          case "REQUEST_APPROVED": iconClass = "fa-check-circle"; colorClass = "text-green-500"; break;
          case "REQUEST_REJECTED": iconClass = "fa-times-circle"; colorClass = "text-red-500"; break;
          case "NEW_ITEM": iconClass = "fa-box"; colorClass = "text-blue-500"; break;
          case "ITEM_RECEIVED": iconClass = "fa-box-open"; colorClass = "text-indigo-500"; break;
          case "REQUEST_PICKUP_READY": iconClass = "fa-location-dot"; colorClass = "text-purple-500"; break;
          case "REQUEST_PAYMENT_PENDING": iconClass = "fa-credit-card"; colorClass = "text-pink-500"; break;
          case "OVERDUE": iconClass = "fa-exclamation-triangle"; colorClass = "text-orange-500"; break;
        }

        li.innerHTML = `
          <i class="fa-solid ${iconClass} ${colorClass} mt-1"></i>
          <div class="flex flex-col text-sm text-gray-700 leading-tight">
            <span>${notification.message}</span>
            <span class="text-[11px] text-gray-400 mt-0.5">${notification.dateAndTime}</span>
          </div>
        `;

        frag.appendChild(li);
      });

      notificationsList.appendChild(frag);
    } else {
      const noData = document.createElement('div');
      noData.classList.add('text-gray-500', 'text-sm', 'italic');
      noData.textContent = 'No Notifications';
      notificationsList.appendChild(noData);
    }
	
	if(lenderDashboardOverview){
		
		const totalItemsId = document.getElementById('totalItems')
		totalItemsId.innerHTML = lenderDashboardOverview.totalItems;
		
		const totalItemsQtyId = document.getElementById('totalItemsQty');
		totalItemsQtyId.innerHTML = lenderDashboardOverview.totalItemsQty;
		
		const totalItemsBorrowedQtyId = document.getElementById('totalItemsBorrowedQty');
		totalItemsBorrowedQtyId.innerHTML = lenderDashboardOverview.totalItemsBorrowedQty;
		
		const totalOngoingBorrowRequestId = document.getElementById('totalOngoingBorrowRequest');
		totalOngoingBorrowRequestId.innerHTML = lenderDashboardOverview.totalOngoingBorrowRequest;
		
		const totalRevenue = document.getElementById('totalRevenue');
		totalRevenue.innerHTML = `₱ ${lenderDashboardOverview.totalRevenue}`;
		
		const totalAvailableItemsQtyId = document.getElementById('totalAvailableItemsQty');
		totalAvailableItemsQtyId.innerHTML = lenderDashboardOverview.totalAvailableItemsQty;
		
		const totalLostItemsQtyId = document.getElementById('totalLostItemsQty');
		totalLostItemsQtyId.innerHTML = lenderDashboardOverview.totalLost;
		
		const totalDamagedItemsQtyId = document.getElementById('totalDamagedItemsQty');
		totalDamagedItemsQtyId.innerHTML = lenderDashboardOverview.totalDamaged;
	}
	
	if (popularItems && popularItems.length > 0) {
	    const popularItemsList = document.getElementById('popular-items-list');
	    popularItemsList.innerHTML = ''; // Clear existing content

	    popularItems.forEach(item => {
	        // Create the container div
	        const div = document.createElement('div');
	        div.className = 'flex justify-between border-b border-gray-300 pb-2';

	        // Create the inner HTML
	        div.innerHTML = `
	            <span>${item.itemName || 'Unnamed Item'}</span>
	            <span class="text-gray-500 font-medium">${item.totalLent || 0} times lent</span>
	        `;

	        // Append to the parent container
	        popularItemsList.appendChild(div);
	    });
	} else {
	    console.log('No popular items to display');
	}
	
	// 🟦 Ongoing Requests (NEW)
	const ongoingTableBody = document.getElementById("ongoing-requests-body");

	ongoingTableBody.innerHTML = ""; // clear first

	if (ongoingRequests && ongoingRequests.length > 0) {

	    const frag = document.createDocumentFragment();

	    ongoingRequests.forEach(req => {

	        const tr = document.createElement("tr");
	        tr.className = "hover:bg-gray-50";

	        tr.innerHTML = `
	            <td class="py-2 px-3">
	               <div class="flex flex-col">
	                  <span class="font-semibold text-gray-700">${req.borrower}</span>
	                  <span class="text-gray-400 text-[11px]">@${req.borrowerUserId}</span>
	               </div>
	            </td>

	            <td class="py-2 px-3">${req.itemName}</td>

	            <td class="py-2 px-3">₱${req.price}</td>

	            <td class="py-2 px-3">${req.qty} pcs</td>

	            <td class="py-2 px-3">
	                ${formatDashboardDate(req.dateToBorrow)}
	            </td>

	            <td class="py-2 px-3">
	                ${formatDashboardDate(req.dateToReturn)}
	            </td>
	        `;

	        frag.appendChild(tr);
	    });

	    ongoingTableBody.appendChild(frag);

	} else {
	    ongoingTableBody.innerHTML = `
	        <tr>
	          <td colspan="7" class="py-3 px-3 text-center text-gray-500">
	            No items currently borrowed.
	          </td>
	        </tr>
	    `;
	}


	removeLoadingScreenBody();
  } catch (error) {
    console.error("❌ Error fetching dashboard data:", error);
  }
});

function formatDashboardDate(date) {
    const d = new Date(date);
    return d.toLocaleDateString("en-US", {
        month: "short",
        day: "numeric",
        year: "numeric"
    });
}

document.addEventListener("DOMContentLoaded", async () => {
	
	createLoadingScreenBody();
	
  const url = "/dashboard/retrieve";

  try {
    const response = await fetch(url);

    if (!response.ok) {
      console.error("❌ Failed to fetch dashboard data");
      return;
    }

    const webDto = await response.json();
    console.log("✅ Dashboard Data:", webDto);

    const { overdues, paymentPendings, ongoingRequests, notifications, overview } = webDto;

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
	
	if(overview){
		const pendingId = document.getElementById('pending');
		pendingId.innerHTML = overview.totalPending;
		
		const approvedId = document.getElementById('approved');
		approvedId.innerHTML = overview.totalApproved;
		
		const pickupreadyId = document.getElementById('pickupready');
		pickupreadyId.innerHTML = overview.totalPickupReady;
		
		const ongoingId = document.getElementById('ongoing');
		ongoingId.innerHTML = overview.totalOngoing;
		
		const completeId = document.getElementById('complete');
		completeId.innerHTML = overview.totalComplete;
		
		const paymentpendingId = document.getElementById('paymentpending');
		paymentpendingId.innerHTML = overview.totalPendingPayment;
		
		const paidId = document.getElementById('paid');
		paidId.innerHTML = overview.totalPaid;
		
		const rejectedId = document.getElementById('rejected');
		rejectedId.innerHTML = overview.totalRejected;
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
		                  <span class="font-semibold text-gray-700">${req.lender}</span>
		                  <span class="text-gray-400 text-[11px]">@${req.lenderUserId}</span>
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
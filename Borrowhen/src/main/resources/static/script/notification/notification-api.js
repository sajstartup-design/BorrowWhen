let openedModals = new WeakSet();

document.addEventListener("DOMContentLoaded", async () => {
  const notificationIcon = document.querySelector('.notification-icon');
  
  // Load notification modal initially (no need to click)
  await updateNotificationModal(notificationIcon, true);
  
  // Also update count
  await updateNotificationCount();
});

// 🔹 Fetch and display notification count
async function updateNotificationCount() {
  try {
    const res = await fetch(`/api/notifications/count`);
    const data = await res.json();

    const notifBtn = document.querySelector('#notifBtn');
    const notifBadge = notifBtn?.querySelector('span');
    const notificationIcon = notifBtn?.querySelector('.notification-icon');

    if (!notifBtn || !notifBadge || !notificationIcon) return;

    const count = data.notificationCount ?? 0;
    notificationIcon.dataset.count = count;

    if (count > 0) {
      notifBadge.textContent = count;
      notifBadge.classList.remove('hidden');
    } else {
      notifBadge.textContent = "";
      notifBadge.classList.add('hidden');
    }

    // Reset modal cache
    openedModals = new WeakSet();
  } catch (error) {
    console.error("Error fetching notification count:", error);
  }
}

// 🔹 Build Notification Modal
async function updateNotificationModal(triggerElement, forceRefresh = false) {
  try {
    const notificationsList = document.querySelector(".notifications-list");
    if (!notificationsList) return;

    // Skip reload if already opened and not forced
    if (triggerElement && openedModals.has(triggerElement) && !forceRefresh) return;

    // Fetch notifications
    const response = await fetch(`/api/notifications`);
    const data = await response.json();

    notificationsList.innerHTML = ""; // Clear previous

    if (!data.notifications || data.notifications.length === 0) {
      notificationsList.innerHTML = `
        <li class="px-4 py-3 text-gray-500 text-center text-sm">No new notifications</li>
      `;
      return;
    }

    const frag = document.createDocumentFragment();

    data.notifications.forEach((notification) => {
      const li = document.createElement("li");
      li.className = "px-4 py-2 hover:bg-gray-100 flex items-start gap-3 border-b border-gray-100";

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

    if (triggerElement) openedModals.add(triggerElement);
  } catch (error) {
    console.error("Error fetching notifications:", error);
  }
}
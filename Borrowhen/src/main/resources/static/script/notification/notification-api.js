const openedModals = new WeakSet();

// 🔹 Run this once on page load
document.addEventListener("DOMContentLoaded", async () => {
    try {
        const countUrl = `/api/notifications/count`;
        const res = await fetch(countUrl);
        const data = await res.json();
		
		console.log(data);

        const notificationIcon = document.querySelector('.notification-icon');
        if (notificationIcon) {
            notificationIcon.dataset.count = data.notificationCount;
        }
    } catch (error) {
        console.error("Error fetching inventory count:", error);
    }
});

async function updateNotificationModal(e) {
    try {
        if (!openedModals.has(e)) {
            const notificationIcon = document.querySelector('.notification-icon');
            const notificationsList = document.querySelector(".notifications-list");

            // only load notifications if count > 0
            if (notificationIcon && parseInt(notificationIcon.dataset.count, 10) > 0) {
                const url = `/api/notifications`;
                const response = await fetch(url);
                const data = await response.json();

                if (data.notifications && notificationsList) {
                    notificationsList.innerHTML = ""; // clear old list
                    const frag = document.createDocumentFragment();

                    data.notifications.forEach(notification => {
                        const container = document.createElement("div");
                        container.classList.add("notification");

                        // pick image based on type
                        let imgSrc = "/images/logo.png";
                        switch (notification.type) {
                            case "REQUESTED_PENDING":
                                imgSrc = "/images/pending.png";
                                break;
                            case "REQUESTED_APPROVED":
                                imgSrc = "/images/approve.png";
                                break;
                            case "REQUESTED_REJECTED":
                                imgSrc = "/images/reject.png";
                                break;
                        }

                        container.innerHTML = `
                            <img src="${imgSrc}" alt="${notification.type}">
                            <div class="details">
                                <span>${notification.message}</span>
                                <span>${notification.createdDate || ""}</span>
                            </div>
                        `;

                        frag.appendChild(container);
                    });

                    notificationsList.appendChild(frag);
                }
            }

            openedModals.add(e);
        }
    } catch (error) {
        console.error("Error fetching notifications:", error);
    }
}

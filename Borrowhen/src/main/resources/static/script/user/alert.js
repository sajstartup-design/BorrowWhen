
function showNotification(containerId, closeId) {
    const notifContainer = document.getElementById(containerId);
    const notifClose = document.getElementById(closeId);

    if (!notifContainer || !notifClose) return;

    // Show the notification
    notifContainer.classList.add('jb-notif-show');

    // Auto-hide after 5 seconds
    setTimeout(() => {
        notifContainer.classList.remove('jb-notif-show');
        setTimeout(() => notifContainer.remove(), 300);
    }, 5000);

    // Hide on click
    notifClose.addEventListener('click', () => {
        notifContainer.classList.remove('jb-notif-show');
        setTimeout(() => notifContainer.remove(), 300);
    });
}

// Call for success notification if present
/*[[${isSuccess}]]*/ 
if (/*[[${isSuccess}]]*/) {
    showNotification('jbNotifContainerSuccess', 'jbNotifCloseSuccess');
}

// Call for error notification if present
/*[[${isError}]]*/ 
if (/*[[${isError}]]*/) {
    showNotification('jbNotifContainerError', 'jbNotifCloseError');
}

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/borrow-when-websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
	
	stompClient.subscribe('/user/queue/lender/notifications', (notification) => {
        const data = notification.body;
		
		// Add pulsating effect
	    const notifIcon = document.querySelector('.notification-icon');
		updateNotificationCount();
		
		
		if(notifIcon){
			notifIcon.classList.add('pulsating-bell', 'bell-ringing');
			updateNotificationModal(notifIcon);
			 
		}

    });
	
	stompClient.subscribe('/user/queue/new-item/notifications', (notification) => {
		
		// Add pulsating effect
	    const notifIcon = document.querySelector('.notification-icon');
		updateNotificationCount();
		
		if(notifIcon){
			notifIcon.classList.add('pulsating-bell', 'bell-ringing');
			updateNotificationModal(notifIcon);
			 
		}

    });
	
	stompClient.subscribe('/user/queue/borrower/notifications', (notification) => {
        const data = notification.body;
		
		// Add pulsating effect
	    const notifIcon = document.querySelector('.notification-icon');
		updateNotificationCount();
		
		if(notifIcon){
			notifIcon.classList.add('pulsating-bell', 'bell-ringing');
			updateNotificationModal(notifIcon);
		}

    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function connect() {
    stompClient.activate();
}

connect();
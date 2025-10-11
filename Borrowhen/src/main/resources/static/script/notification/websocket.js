const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/borrow-when-websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
	
	stompClient.subscribe('/user/queue/lender/notifications', () => {

		// Add pulsating effect
	    const notifIcon = document.querySelector('.notification-icon');
		updateNotificationCount();
		
		
		if(notifIcon){
			notifIcon.classList.add('pulsating-bell', 'bell-ringing');
			updateNotificationModal(null, true);
			 
		}

    });
	
	stompClient.subscribe('/user/queue/new-item/notifications', () => {
		
		// Add pulsating effect
	    const notifIcon = document.querySelector('.notification-icon');
		updateNotificationCount();
		
		if(notifIcon){
			notifIcon.classList.add('pulsating-bell', 'bell-ringing');
			updateNotificationModal(null, true);
			 
		}

    });
	
	stompClient.subscribe('/user/queue/borrower/notifications', () => {

		// Add pulsating effect
	    const notifIcon = document.querySelector('.notification-icon');
		updateNotificationCount();
		
		if(notifIcon){
			notifIcon.classList.add('pulsating-bell', 'bell-ringing');
			updateNotificationModal(null, true);
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
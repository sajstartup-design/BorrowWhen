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
			let bellContainer = notifIcon.querySelector('.bell-container');
			bellContainer.classList.add('pulsating-circle');
			let bellIcon = notifIcon.querySelector('.bell-icon');
			bellIcon.classList.add('bell');
			
			 
		}

    });
	
	stompClient.subscribe('/user/queue/borrower/notifications', (notification) => {
        const data = notification.body;
		
		// Add pulsating effect
	    const notifIcon = document.querySelector('.notification-icon');
		updateNotificationCount();
		
		if(notifIcon){
			let bellContainer = notifIcon.querySelector('.bell-container');
			bellContainer.classList.add('pulsating-circle');
			let bellIcon = notifIcon.querySelector('.bell-icon');
			bellIcon.classList.add('bell');
			
			 
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
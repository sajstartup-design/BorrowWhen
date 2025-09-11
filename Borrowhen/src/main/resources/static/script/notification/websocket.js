const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/borrow-when-websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
	
	stompClient.subscribe('/user/queue/lender/notifications', (notification) => {
        const data = notification.body;
        console.log("Private lender notification:", data);

        updateNotificationModal(null, true);
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
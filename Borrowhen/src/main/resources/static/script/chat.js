
let chats = new Map();


document.addEventListener("DOMContentLoaded", () => {
	const search = document.querySelector('.search');
	
	if (search) {
	    let typingTimer; 
	    const delay = 500; 

	    search.addEventListener('input', function () {
	        clearTimeout(typingTimer); 
	        
	        const value = this.value;
	        
	        typingTimer = setTimeout(() => {
				searchUsers(value);
	        }, delay);
	    });
	}
	
	const sendBtn = document.querySelector('.send-btn');
	
	if(sendBtn){
		sendBtn.addEventListener('click', async function(){
			
			const inputMessage = document.querySelector('.input-message');
			
			const value = inputMessage.value;
			
			if(inputMessage.value === '') return;
			
			const encryptedUserId = sendBtn.dataset.encryptedUserId;
			
			await sendMessage(value, encryptedUserId);
		});
	}
	
	const checkBtn = document.querySelector('.check-btn');
	
	if(checkBtn){
		checkBtn.addEventListener('click', async function(){
			
			console.log(chats);
		});
	}
	
	getAllChats();
});

async function searchUsers(search = "") {
    const params = new URLSearchParams({ search });

    fetch(`/chat/users?${params.toString()}`, {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            console.error('Error fetching users');
        } else {
            return response.json();
        }
    })
    .then(data => {
        if (data) {
            console.log("Users:", data);
            displaySearchUsers(data.users);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

async function saveConversation(encryptedUserId, firstName, familyName){
	const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    fetch("/chat/new-conversation", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': csrfToken 
        },
        body: new URLSearchParams({ encryptedUserId }),
        credentials: 'same-origin'
    })
    .then(response => {
        if (!response.ok) {
            console.error('Error sending message');
            return;
        }

        return response.json();
    })
    .then(data => {
		console.log(data);
		
		const chat = {
			encryptedConversationId : data.encryptedConversationId,
			firstName : firstName,
			familyName : familyName,
			lastMessage : 'New Conversation',
			lastMessageDate : null
		}
		
		chats.set(data.chat.encryptedConversationId, chat);
		
		displayChats([chat], true, true);
		
		
	})
    .catch(error => {
        console.error('Error:', error);
    });
}















async function getAllChats(){
	
    fetch(`/chat/chats`, {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            console.error('Error fetching chats');
        } else {
            return response.json();
        }
    })
    .then(data => {
        if (data) {
            console.log("Chats:", data);
			displayChats(data.chats);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
    
}


async function sendMessage(message, encryptedUserId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    fetch("/chat/send-message", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': csrfToken 
        },
        body: new URLSearchParams({ message, encryptedUserId }),
        credentials: 'same-origin'
    })
    .then(response => {
        if (!response.ok) {
            console.error('Error sending message');
            return;
        }
        
        const newMessage = {
			message : message,
			isSender : true
		}
        
        displayMessages([newMessage]);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function displayMessages(messages) {
    const messageList = document.querySelector('.message-list');
    const fragments = document.createDocumentFragment();

    for (let message of messages) {
        const messageContainer = document.createElement('div');

        if (message.isSender) {
            messageContainer.classList.add('your-message');
            messageContainer.innerHTML = `
                <div class="message">
                    <span>${message.message}</span>
                </div>`;
        } else {
            messageContainer.classList.add('other-message');
            messageContainer.innerHTML = `
                <div class="message">
                    <span>${message.message}</span>
                </div>`;
        }

        fragments.appendChild(messageContainer);
    }

    messageList.appendChild(fragments);
}

function displayChats(chats, isNewConversation, onlyAppend = false){
	
	const chatsContent = document.querySelector('.chats-content');
	
	if(!onlyAppend){
		chatsContent.innerHTML = '';
	}
	
	const fragments = document.createDocumentFragment();
	
	let initial = true;
	
	const searching = document.createElement('div');
	searching.classList.add('searching');
	searching.innerHTML = 'Conversations';

	for(let chat of chats){
		
		if(initial){
			fragments.append(searching);
			
			const hr = document.createElement('hr');
			hr.classList.add('soft-gradient');
			fragments.append(hr);
			initial = false;
		}
		
		let userContainer = document.createElement('div');
		userContainer.classList.add("chat-box-title");
		userContainer.classList.add("chat");
		userContainer.setAttribute('data-encrypted-id', chats.encryptedConversationId);
		
		userContainer.innerHTML = `<div class="pic">
								<img src="/images/logo.png">
							</div>
							<div class="name">
								<span>${chat.firstName} ${chat.familyName}</span>
								<span>${isNewConversation || chat.isNew ? 'New Conversation' : chat.lastMessage}</span>
							</div>
							<span class="dot green" role="img" aria-label="green circle"></span>`;
							
		userContainer.addEventListener('click', function(){
			
			const chatContent = document.querySelector('.chat-content');
			
			const name = chatContent.querySelector('.name span:first-child');
			const count = chatContent.querySelector('.name span:last-child');
			
			name.textContent = chat.firstName + ' ' + chat.familyName;
			count.textContent = isNewConversation || chat.isNew ? 'New Conversation' : chat.lastMessage;
			
			const sendBtn = document.querySelector('.send-btn');
			sendBtn.setAttribute('data-encrypted-user-id', chat.encryptedConversationId);
		});
							
		fragments.append(userContainer);
		
		const hr = document.createElement('hr');
		hr.classList.add('soft-gradient');
		fragments.append(hr);
							
	}
	
	chatsContent.append(fragments);
}



function displaySearchUsers(users){
	
	const chatsContent = document.querySelector('.chats-content');
	chatsContent.innerHTML = '';
	
	const fragments = document.createDocumentFragment();
	
	let initial = true;

	const searching = document.createElement('div');
	searching.classList.add('searching');
	searching.innerHTML = 'searching...';
	
	for(let user of users){
		
		if(initial){
			fragments.append(searching);
			
			const hr = document.createElement('hr');
			hr.classList.add('soft-gradient');
			fragments.append(hr);
			initial = false;
		}
		
		let userContainer = document.createElement('div');
		userContainer.classList.add("chat-box-title");
		userContainer.classList.add("chat");
		
		userContainer.innerHTML = `<div class="pic">
								<img src="/images/logo.png">
							</div>
							<div class="name">
								<span>${user.firstName + ' ' + user.familyName}</span>
								<span>Hello I am Julius</span>
							</div>
							<span class="dot green" role="img" aria-label="green circle"></span>`;
							
		userContainer.addEventListener('click', function(){
			
			const chatContent = document.querySelector('.chat-content');
			
			const name = chatContent.querySelector('.name span:first-child');
			const count = chatContent.querySelector('.name span:last-child');
			
			name.textContent = user.firstName + ' ' + user.familyName;
			count.textContent = 'New Conversation';
			
			const sendBtn = document.querySelector('.send-btn');
			sendBtn.setAttribute('data-encrypted-user-id', user.encryptedId);
			
			saveConversation(user.encryptedId, user.firstName, user.familyName);
		});
							
		fragments.append(userContainer);
		
		const hr = document.createElement('hr');
		hr.classList.add('soft-gradient');
		fragments.append(hr);
	}
	
	chatsContent.append(fragments);
}


function updateSendButton(){
	const sendBtn = document.querySelector('.send-btn');
	sendBtn.setAttribute('data-encrypted-user-id', user.encryptedId);
}
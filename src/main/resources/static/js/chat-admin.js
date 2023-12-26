const chatArea = document.querySelector('#chat-messages');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');

let stompClient = null;
let username = null;
let fullname = null;
let selectedUserId = null;

fetch('/check-login')
    .then(response => response.json())
    .then(data => {
        if (data.authenticated) {
            username = data.username;
            fullname = data.name;
            console.log('Đã đăng nhập với tên người dùng:', username);
        } else {
            console.log('Người dùng chưa đăng nhập.');
        }
    })
    .catch(error => console.error('Lỗi khi gọi API:', error));

function onConnected(event) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe(`/user/${username}/queue/messages`, onMessageReceived);
        stompClient.subscribe(`/user/public`, onMessageReceived);

        findAndDisplayConnectedUsers().then();
    });
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.username !== username);

    let connectedUsersHtml = '';
    connectedUsers.forEach(user => {
        connectedUsersHtml += `
            <div class="chat_list" id="${user.username}" onclick="userItemClick(event)">
                <div class="chat_people">
                    <div class="chat_img">
                        <img src="https://ptetutorials.com/images/user-profile.png" alt="${user.username}">
                    </div>
                    <div class="chat_ib">
                        <h5>${user.fullname}</h5>
                        <p class="nbr-msg hidden">Đang hoạt động</p>
                    </div>
                </div>
            </div>`;
    });

    document.getElementById('connectedUsers').innerHTML = connectedUsersHtml;
}

function userItemClick(event) {
    document.querySelectorAll('.chat_list').forEach(item => {
        item.classList.remove('active_chat');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active_chat');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = 'Đang hoạt động';
}
function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Tháng trong JavaScript bắt đầu từ 0

    return `${hours}:${minutes} - ${day} Tháng ${month}`;
}
function displayMessage(senderId, content, timestamp) {
    const formattedTimestamp = formatTimestamp(timestamp);
    const messageHtml = senderId === username ?
        `<div class="outgoing_msg mt-1">
            <div class="sent_msg">
                <p>${content}</p>
                <span class="time_date_popup">${formattedTimestamp}</span>
            </div>
        </div>` :
        `<div class="incoming_msg mb-1">
            <div class="received_msg">
                    <p>${content}</p>
                    <span class="time_date_popup">${formattedTimestamp}</span>
            </div>
        </div>`;

    chatArea.innerHTML += messageHtml;
}


async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${username}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content, chat.timestamp);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

function sendMessage(event) {
    event.preventDefault();
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: username,
            recipientId: selectedUserId,
            content: messageContent,
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(username, messageContent);
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
}

async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active_chat');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active_chat')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

$(document).ready(function () {
    onConnected();
});

messageForm.addEventListener('submit', sendMessage, true);

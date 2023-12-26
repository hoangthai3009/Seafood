$(function() {
    const index = 0;
    $("#chat-submit").click(function(e) {
        //e.preventDefault();
        var msg = $("#chat-input").val();
        if(msg.trim() === ''){
            return false;
        }
        // generate_message(msg, 'self', index); // Truyền index vào generate_message
        // const buttons = [
        //     {
        //         name: 'Existing User',
        //         value: 'existing'
        //     },
        //     {
        //         name: 'New User',
        //         value: 'new'
        //     }
        // ];
        // setTimeout(function() {
        //     generate_message(msg, 'user', index); // Truyền index vào generate_message
        // }, 1000)
    });

    function generate_message(msg, type, index) {
        index++;
        let str = `
        <div id="cm-msg-${index}" class="chat-msg ${type}">
            <span class="msg-avatar">
                <img src="https://ptetutorials.com/images/user-profile.png">
            </span>
            <div class="cm-msg-text">
                ${msg}
            </div>
        </div>
    `;
        $(".chat-logs").append(str);
        $(`#cm-msg-${index}`).hide().fadeIn(300); // Sử dụng template literals để nhúng giá trị
        if (type === 'self') {
            $("#chat-input").val('');
        }
        $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
    }

    $(document).delegate(".chat-btn", "click", function() {
        const value = $(this).attr("chat-value");
        const name = $(this).html();
        $("#chat-input").attr("disabled", false);
        generate_message(name, 'self', index); // Truyền index vào generate_message
    })

    $("#chat-circle").click(function() {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    })

    $(".chat-box-toggle").click(function() {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    })

})


const chatArea = document.querySelector('#chat-messages');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#chat-input');

let stompClient = null;
let username = null;
let selectedUserId = 'admin';

fetch('/check-login')
    .then(response => response.json())
    .then(data => {
        if (data.authenticated) {
            username = data.username;
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
        fetchAndDisplayUserChat().then();

        stompClient.subscribe(`/user/${username}/queue/messages`, onMessageReceived);
        stompClient.subscribe(`/user/public`, onMessageReceived);
    });
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    const message = document.createElement('div');

    if (senderId === username) {
        messageContainer.classList.add('chat-msg', 'self');
    } else {
        messageContainer.classList.add('chat-msg', 'user');
    }
    message.classList.add('cm-msg-text');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

function sendMessage(event) {
    if (username != null) {
        const messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            const chatMessage = {
                senderId: username,
                recipientId: selectedUserId,
                content: messageInput.value.trim(),
                timestamp: new Date()
            };
            stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
            displayMessage(username, messageInput.value.trim());
            messageInput.value = '';
        }
        chatArea.scrollTop = chatArea.scrollHeight;
        event.preventDefault();
    } else {
        alert('Vui lòng đăng nhập!');
    }
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${username}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

async function onMessageReceived(payload) {
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);

    displayMessage(message.senderId, message.content);
    chatArea.scrollTop = chatArea.scrollHeight;

    document.querySelector(`#${selectedUserId}`).classList.add('active_chat');

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

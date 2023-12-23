$(function() {
    const index = 0;
    $("#chat-submit").click(function(e) {
        e.preventDefault();
        var msg = $("#chat-input").val();
        if(msg.trim() === ''){
            return false;
        }
        generate_message(msg, 'self', index); // Truyền index vào generate_message
        const buttons = [
            {
                name: 'Existing User',
                value: 'existing'
            },
            {
                name: 'New User',
                value: 'new'
            }
        ];
        setTimeout(function() {
            generate_message(msg, 'user', index); // Truyền index vào generate_message
        }, 1000)

    })

    function generate_message(msg, type, index) {
        index++;
        let str = `
        <div id="cm-msg-${index}" class="chat-msg ${type}">
            <span class="msg-avatar">
                <img src="https://image.crisp.im/avatar/operator/196af8cc-f6ad-4ef7-afd1-c45d5231387c/240/?1483361727745">
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

    function generate_button_message(msg, buttons, index) {
        index++;
        const btn_obj = buttons
            .map(function (button) {
                return `
                <li class="button">
                    <a href="javascript:;" class="btn btn-primary chat-btn" chat-value="${button.value}">
                        ${button.name}
                    </a>
                </li>`;
            })
            .join('');

        let str = `
        <div id="cm-msg-${index}" class="chat-msg user">
            <span class="msg-avatar">
                <img src="https://image.crisp.im/avatar/operator/196af8cc-f6ad-4ef7-afd1-c45d5231387c/240/?1483361727745">
            </span>
            <div class="cm-msg-text">
                ${msg}
            </div>
            <div class="cm-msg-button">
                <ul>
                    ${btn_obj}
                </ul>
            </div>
        </div>
    `;

        $(".chat-logs").append(str);
        $(`#cm-msg-${index}`).hide().fadeIn(300); // Sử dụng template literals để nhúng giá trị
        $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
        $("#chat-input").attr("disabled", true);
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

// js login và register api
function login() {
    var usernameOrEmail = document.getElementById("usernameOrEmail").value;
    var password = document.getElementById("login-password").value;

    var loginData = {
        usernameOrEmail: usernameOrEmail,
        password: password
    };

    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/';
            } else {
                document.getElementById("error").innerText = "Sai thông tin đăng nhập";
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}
function register() {
    var username = document.getElementById("username").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var fullname = document.getElementById("fullname").value;
    var phone = document.getElementById("phone").value;

    var signUpData = {
        username: username,
        email: email,
        password: password,
        fullname: fullname,
        phone: phone
    };

    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(signUpData),
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/login';
            } else {
                return response.json();
            }
        })
        .then(data => {
            if (data && data.message) {
                document.getElementById("error").innerText = data.message;
            } else {
                document.getElementById("error").innerText = "Sai thông tin đăng nhập";
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}
document.addEventListener('DOMContentLoaded', function () {
    // Thêm sự kiện cho trường input
    var inputFields = document.querySelectorAll('.form-inner form .field input');
    inputFields.forEach(function (input) {
        input.addEventListener('keypress', function (event) {
            console.log("Adding event listener...");
            input.addEventListener('keypress', function (event) {
                console.log("Keypress event:", event);
                if (event.key === 'Enter') {
                    console.log("Enter key pressed!");
                    if (input.id === 'usernameOrEmail' || input.id === 'login-password') {
                        login();
                    } else if (input.id === 'username' && input.id === 'password' && input.id === 'confirmPassword' ) {
                        validatePassword();
                    }
                }
            });
        });
    });
});
function validatePassword() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    var errorDiv = document.getElementById("passwordMatchError");

    if (password !== confirmPassword) {
        errorDiv.style.display = "block";
    } else {
        // Passwords match, you can proceed with your registration logic.
        errorDiv.style.display = "none";
        register();
        // Add your logic here to handle the registration.
    }
}

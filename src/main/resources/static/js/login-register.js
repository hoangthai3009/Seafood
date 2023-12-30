// js login và register api
function login() {
    const usernameOrEmail = document.getElementById("usernameOrEmail").value;
    const password = document.getElementById("login-password").value;
    const loginData = {
        usernameOrEmail: usernameOrEmail,
        password: password
    };

    // Initialize error messages as empty strings
    let authErrorMessage = "";
    let usernameOrEmailErrorMessage = "";
    let loginPasswordErrorMessage = "";

    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
    })
        .then(response => {
            if (response.ok)
                return response.json();
            else
                return response.json().then(err => Promise.reject(err));
        })
        .then(data => {
            if (data && data.roles && data.roles.includes('ROLE_ADMIN')) {
                window.location.href = '/admin';
                alert('Chào mừng! Bạn đã đăng nhập với quyền ADMIN')
            } else {
                window.location.href = '/';
            }
        })
        .catch(error => {
            if (error.error)
                authErrorMessage = error.error;
            if (error.usernameOrEmail)
                usernameOrEmailErrorMessage = error.usernameOrEmail;
            if (error.password)
                loginPasswordErrorMessage = error.password;
        })
        .finally(() => {
            // Set error messages in DOM after promise completion
            document.getElementById("authError").innerText = authErrorMessage;
            document.getElementById("usernameOrEmailError").innerText = usernameOrEmailErrorMessage;
            document.getElementById("loginPasswordError").innerText = loginPasswordErrorMessage;
        });

    clearLoginErrorMessages();
}


function register() {
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const fullname = document.getElementById("fullname").value;
    const phone = document.getElementById("phone").value;

    const signUpData = {
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
            if (data) {
                clearErrorMessages();
                    if (data.errorUsername) {
                        document.getElementById("usernameError").innerText = data.errorUsername;
                    }
                    if (data.errorEmail) {
                        document.getElementById("emailError").innerText = data.errorEmail;
                    }
                for (const field in data) {
                    const errorField = field + "Error";
                    if (document.getElementById(errorField)) {
                        document.getElementById(errorField).innerText = data[field];
                    }
                }
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}
function clearLoginErrorMessages() {
    document.getElementById("usernameOrEmailError").innerText = "";
    document.getElementById("loginPasswordError").innerText = "";
    document.getElementById("authError").innerText = "";
}function clearErrorMessages() {
    document.getElementById("usernameError").innerText = "";
    document.getElementById("emailError").innerText = "";
    document.getElementById("passwordError").innerText = "";
    document.getElementById("fullnameError").innerText = "";
    document.getElementById("phoneError").innerText = "";
}

document.addEventListener('DOMContentLoaded', function () {
    // Thêm sự kiện cho trường input
    const inputFields = document.querySelectorAll('.form-inner form .field input');
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
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const errorDiv = document.getElementById("passwordMatchError");

    if (password !== confirmPassword) {
        errorDiv.style.display = "block";
    } else {
        errorDiv.style.display = "none";
        register();
    }
}

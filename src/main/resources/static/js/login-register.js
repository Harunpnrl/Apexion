function toggleAuth(mode) {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const tabLogin = document.getElementById('tab-login');
    const tabRegister = document.getElementById('tab-register');

    if (mode === 'login') {
        loginForm.classList.remove('hidden');
        registerForm.classList.add('hidden');
        tabLogin.classList.replace('border-transparent', 'border-primary-container');
        tabLogin.classList.replace('text-secondary', 'text-white');
        tabRegister.classList.replace('border-primary-container', 'border-transparent');
        tabRegister.classList.replace('text-white', 'text-secondary');
    } else {
        loginForm.classList.add('hidden');
        registerForm.classList.remove('hidden');
        tabRegister.classList.replace('border-transparent', 'border-primary-container');
        tabRegister.classList.replace('text-secondary', 'text-white');
        tabLogin.classList.replace('border-primary-container', 'border-transparent');
        tabLogin.classList.replace('text-white', 'text-secondary');
    }
}

const inputs = document.querySelectorAll('.form-input');
inputs.forEach(input => {
    input.addEventListener('focus', () => {
        input.parentElement.classList.add('scale-[1.01]');
    });
    input.addEventListener('blur', () => {
        input.parentElement.classList.remove('scale-[1.01]');
    });
});

function showNotification(message, isError = false) {
    const notification = document.createElement('div');
    const bgColor = isError ? 'bg-error' : 'bg-primary-container';
    notification.className = `fixed top-6 left-1/2 transform -translate-x-1/2 px-6 py-3 rounded-lg shadow-xl text-white font-label-md tracking-wider z-50 transition-opacity duration-300 ${bgColor}`;
    notification.textContent = message;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.classList.add('opacity-0');
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

const BASE_URL = 'http://localhost:8080/api/v1/auth';

// --- REGISTER ---
document.getElementById('register-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const formInputs = this.querySelectorAll('input');
    const firstName = formInputs[0].value.trim();
    const lastName = formInputs[1].value.trim();
    const email = formInputs[2].value.trim();
    const password = formInputs[3].value.trim();

    const registerData = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password
    };

    fetch(`${BASE_URL}/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(registerData)
    })
        .then(async response => {
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.error || "Registration failed! Email might be in use.");
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('apexion_token', data.token);
            showNotification("Registration successful! Welcome to Apexion.");
            setTimeout(() => {
                window.location.href = 'Home.html';
            }, 1000);
        })
        .catch(error => {
            console.error("Register Error:", error);
            showNotification(error.message, true);
        });
});

// --- LOGIN ---
document.getElementById('login-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const formInputs = this.querySelectorAll('input');
    const email = formInputs[0].value.trim();
    const password = formInputs[1].value.trim();

    const loginData = {
        email: email,
        password: password
    };

    fetch(`${BASE_URL}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(async response => {
            if (response.status === 403 || response.status === 401) {
                throw new Error("Invalid email or password!");
            }
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.error || "An error occurred during login.");
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('apexion_token', data.token);
            showNotification("Login successful!");
            setTimeout(() => {
                window.location.href = 'Home.html';
            }, 500);
        })
        .catch(error => {
            console.error("Login Error:", error);
            showNotification(error.message, true);
        });
});
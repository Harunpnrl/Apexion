const API_BASE_URL = 'http://localhost:8080/api/v1';

function escapeHtml(value) {
    return String(value ?? '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

function logout() {
    localStorage.removeItem('apexion_token');
    window.location.href = 'LoginRegister.html';
}

async function fetchWithAuth(endpoint, options = {}) {
    const token = localStorage.getItem('apexion_token');

    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers: headers
    });

    if (response.status === 401 || response.status === 403) {
        logout();
        throw new Error("Your session has expired. Please log in again.");
    }

    return response;
}

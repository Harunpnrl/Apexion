document.addEventListener("DOMContentLoaded", async () => {
    await loadComponent('site-header', '../components/header.html');
    await loadComponent('site-footer', '../components/footer.html');

    updateHeaderAuthUI();
});

async function loadComponent(elementId, filePath) {
    const container = document.getElementById(elementId);
    if (!container) return;
    try {
        const response = await fetch(filePath);
        if (response.ok) {
            container.innerHTML = await response.text();
            container.classList.add('loaded');
        }
    } catch (error) {
        console.error(`Error loading ${filePath}:`, error);
        container.classList.add('loaded');
    }
}

function updateHeaderAuthUI() {
    const authContainer = document.getElementById('auth-container');
    if (!authContainer) return;

    const token = localStorage.getItem('apexion_token');

    if (token) {
        authContainer.innerHTML = `
            <div onclick="window.location.href='UserProfile.html'" class="w-10 h-10 rounded-full bg-surface-variant overflow-hidden border border-surface-variant cursor-pointer hover:border-primary-container transition-colors shrink-0" title="My Profile">
                <img alt="User profile" class="w-full h-full object-cover" src="../assets/images/davidlaid.jpg">
            </div>
            <button onclick="logoutUser()" class="text-on-surface-variant hover:text-primary-container transition-colors flex items-center" title="Logout">
                <span class="material-symbols-outlined text-[24px]">logout</span>
            </button>
        `;
    } else {
        authContainer.innerHTML = `
              <a class="inline-flex items-center justify-center gap-2 px-6 py-3 bg-primary-container hover:bg-primary-hover active:scale-95 transition-all text-white font-label text-sm font-semibold uppercase tracking-wider rounded-lg shadow-[0_0_15px_rgba(204,0,0,0.4)] group cursor-pointer" href="LoginRegister.html">
                        <span>LOGIN</span>
                    </a>
        `;
    }
}

window.logoutUser = function () {
    localStorage.removeItem('apexion_token');
    window.location.href = 'Home.html';
};
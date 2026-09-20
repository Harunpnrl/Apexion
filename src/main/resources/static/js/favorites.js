async function toggleFavorite(event, exerciseId, btnElement) {
    event.stopPropagation();
    const token = localStorage.getItem('apexion_token');

    if (!token) {
        if (typeof showNotification === 'function') {
            showNotification("Please log in to save favorite exercises.", true);
        } else {
            alert("Please log in to save favorite exercises.");
        }

        return;
    }

    const icon = btnElement.querySelector('.favorite-icon');
    const isFavorited = userFavorites.has(exerciseId);
    const authHeaders = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };

    try {
        if (isFavorited) {
            const response = await fetch(`http://localhost:8080/api/v1/users/me/favorites/${exerciseId}`, {
                method: 'DELETE',
                headers: authHeaders
            });

            if (response.ok) {
                userFavorites.delete(exerciseId);
                icon.classList.remove('fill', 'text-primary-container');
                icon.classList.add('text-on-surface-variant');
            } else if (response.status === 403 || response.status === 401) {
                console.error("Unauthorized. Token may be expired.");
                localStorage.removeItem('apexion_token');
                window.location.href = 'LoginRegister.html';
            } else {
                console.error("Failed to remove from favorites.");
            }

        } else {
            const response = await fetch(`http://localhost:8080/api/v1/users/me/favorites/${exerciseId}`, {
                method: 'POST',
                headers: authHeaders
            });

            if (response.ok) {
                userFavorites.add(exerciseId);
                icon.classList.add('fill', 'text-primary-container');
                icon.classList.remove('text-on-surface-variant');
            } else if (response.status === 403 || response.status === 401) {
                console.error("Unauthorized. Token may be expired.");
                localStorage.removeItem('apexion_token');
                window.location.href = 'LoginRegister.html';
            } else {
                console.error("Failed to add to favorites.");
            }
        }
    } catch (err) {
        console.error("Server error during favorite toggle:", err);
    }
}
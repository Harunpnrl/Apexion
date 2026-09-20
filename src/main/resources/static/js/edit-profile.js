document.addEventListener("DOMContentLoaded", async () => {
    const loadingState = document.getElementById('edit-profile-loading');
    const form = document.getElementById('edit-profile-form');

    const token = localStorage.getItem('apexion_token');
    if (!token) {
        window.location.href = 'LoginRegister.html';
        return;
    }

    const authHeaders = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };

    let hasExistingFitnessProfile = false;

    try {
        const [userRes, profileRes] = await Promise.all([
            fetch(`http://localhost:8080/api/v1/users/me`, { headers: authHeaders }),
            fetch(`http://localhost:8080/api/v1/profiles/me`, { headers: authHeaders }).catch(() => ({ ok: false }))
        ]);

        if (userRes.status === 403 || userRes.status === 401) {
            localStorage.removeItem('apexion_token');
            window.location.href = 'LoginRegister.html';
            return;
        }

        if (userRes.ok) {
            const userData = await userRes.json();

            document.getElementById('firstName').value = userData.firstName || '';
            document.getElementById('lastName').value = userData.lastName || '';
            document.getElementById('email').value = userData.email || '';
            document.getElementById('phoneNumber').value = userData.phoneNumber || '';

            if (userData.birthDate) {
                document.getElementById('birthdate').value = userData.birthDate;
            }

            if (userData.gender) {
                const genderInputs = Array.from(document.querySelectorAll('input[name="gender"]'));
                const matchedInput = genderInputs.find(r => r.value.toUpperCase() === userData.gender.toUpperCase());

                if (matchedInput) {
                    matchedInput.checked = true;
                }
            }
        }

        if (profileRes.ok) {
            const profileData = await profileRes.json();
            hasExistingFitnessProfile = true;

            document.getElementById('heightCm').value = profileData.heightCm || '';
            document.getElementById('weightKg').value = profileData.weightKg || '';

            if (profileData.fitnessGoal) {
                document.getElementById('fitnessGoal').value = profileData.fitnessGoal.toUpperCase();
            }
            if (profileData.activityLevel) {
                document.getElementById('activityLevel').value = profileData.activityLevel.toUpperCase();
            }
        }

        loadingState.classList.add('hidden');
        form.classList.remove('hidden');
        setTimeout(() => form.classList.remove('opacity-0'), 50);

    } catch (error) {
        console.error("API Error loading initial data:", error);
        loadingState.innerHTML = '<p class="text-error font-bold text-center">Failed to load your data.<br>Please ensure the server is running.</p>';
    }

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const saveBtn = document.getElementById('save-btn');
        const originalBtnHtml = saveBtn.innerHTML;

        // Visual feedback
        saveBtn.disabled = true;
        saveBtn.innerHTML = '<span class="material-symbols-outlined text-[18px] animate-spin">sync</span> Saving...';

        const formData = new FormData(form);
        const rawGender = formData.get('gender');
        const formattedGender = rawGender ? rawGender.toUpperCase() : null;
        const birthDateVal = formData.get('birthDate');

        const userUpdateData = {
            firstName: formData.get('firstName')?.trim() || null,
            lastName: formData.get('lastName')?.trim() || null,
            phoneNumber: formData.get('phoneNumber')?.trim() || null,
            gender: formattedGender,
            birthDate: birthDateVal ? birthDateVal : null
        };

        const profileUpdateData = {
            heightCm: parseFloat(formData.get('heightCm')),
            weightKg: parseFloat(formData.get('weightKg')),
            fitnessGoal: formData.get('fitnessGoal')?.toUpperCase() || null,
            activityLevel: formData.get('activityLevel')?.toUpperCase() || null
        };

        try {
            const userUpdateRes = await fetch(`http://localhost:8080/api/v1/users/me`, {
                method: 'PUT',
                headers: authHeaders,
                body: JSON.stringify(userUpdateData)
            });

            if (!userUpdateRes.ok) {
                const errorData = await userUpdateRes.json().catch(() => ({}));
                throw new Error(errorData.error || `Failed to update personal info (HTTP ${userUpdateRes.status})`);
            }

            const profileMethod = hasExistingFitnessProfile ? 'PUT' : 'POST';
            const profileUpdateRes = await fetch(`http://localhost:8080/api/v1/profiles/me`, {
                method: profileMethod,
                headers: authHeaders,
                body: JSON.stringify(profileUpdateData)
            });

            if (!profileUpdateRes.ok) {
                const errorData = await profileUpdateRes.json().catch(() => ({}));
                throw new Error(errorData.error || `Failed to update fitness profile (HTTP ${profileUpdateRes.status})`);
            }

            if (typeof showNotification === 'function') {
                showNotification("Profile updated successfully!");
            }

            // Redirect back to profile page
            setTimeout(() => {
                window.location.href = 'UserProfile.html';
            }, 1000);

        } catch (error) {
            console.error("Form Submit Error:", error);
            if (typeof showNotification === 'function') {
                showNotification(error.message || "Failed to save changes.", true);
            }
            saveBtn.disabled = false;
            saveBtn.innerHTML = originalBtnHtml;
        }
    });
});
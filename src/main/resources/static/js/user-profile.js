document.addEventListener("DOMContentLoaded", async () => {
    const loadingState = document.getElementById('profile-loading');
    const contentState = document.getElementById('profile-content');

    const token = localStorage.getItem('apexion_token');
    if (!token) {
        window.location.href = 'LoginRegister.html';
        return;
    }

    const authHeaders = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };

    try {
        const userRes = await fetch(`http://localhost:8080/api/v1/users/me`, {headers: authHeaders});

        if (userRes.status === 403 || userRes.status === 401) {
            localStorage.removeItem('apexion_token');
            window.location.href = 'LoginRegister.html';
            return;
        }

        if (!userRes.ok) throw new Error("Failed to load core user data.");

        const userData = await userRes.json();
        populatePersonalInfo(userData);

        try {
            const favoritesRes = await fetch(`http://localhost:8080/api/v1/users/me/favorites`, {headers: authHeaders});
            if (favoritesRes.ok) {
                const favoritesData = await favoritesRes.json();
                renderFavoriteExercises(favoritesData);
            } else {
                renderFavoriteExercises([]);
            }
        } catch (favError) {
            console.error("Favorites API Error:", favError);
            renderFavoriteExercises([]);
        }

        try {
            const profileRes = await fetch(`http://localhost:8080/api/v1/profiles/me`, {headers: authHeaders});
            if (profileRes.ok) {
                const profileData = await profileRes.json();
                populateFitnessProfile(profileData);
            } else {
                showEmptyFitnessProfile();
            }
        } catch (profileError) {
            console.error("Fitness Profile API Error:", profileError);
            showEmptyFitnessProfile();
        }

        try {
            console.log("Fetching workouts...");
            const workoutsRes = await fetch(`http://localhost:8080/api/v1/workouts/my-workouts`, {headers: authHeaders});

            if (workoutsRes.ok) {
                const workoutsData = await workoutsRes.json();
                console.log("Workouts loaded:", workoutsData);
                renderWorkouts(workoutsData, authHeaders);
            } else {
                console.warn("Workouts endpoint returned status:", workoutsRes.status);
                renderWorkouts([], authHeaders);
            }
        } catch (workoutError) {
            console.error("Workouts API Connection Error:", workoutError);
            renderWorkouts([], authHeaders);
        }

        loadingState.classList.add('hidden');
        contentState.classList.remove('hidden');

        setTimeout(() => {
            contentState.classList.remove('opacity-0');
        }, 50);

    } catch (error) {
        console.error("Critical API Connection Error:", error);
        loadingState.innerHTML = '<p class="text-error font-bold text-center">Failed to load profile data.<br>Please ensure you are logged in and the server is running.</p>';
    }
});

function showEmptyFitnessProfile() {
    const dataSection = document.getElementById('fitness-profile-data');
    const emptySection = document.getElementById('fitness-profile-empty');
    if (dataSection && emptySection) {
        dataSection.classList.add('hidden');
        emptySection.classList.remove('hidden');
        emptySection.classList.add('flex');
    }
}

function populatePersonalInfo(user) {
    document.getElementById('user-name').textContent = user.firstName || '--';
    document.getElementById('user-surname').textContent = user.lastName || '--';
    document.getElementById('user-email').textContent = user.email || '--';
    document.getElementById('user-phoneNumber').textContent = user.phoneNumber || 'Not provided';
    document.getElementById('user-gender').textContent = user.gender || 'Not specified';

    if (user.birthDate) {
        const date = new Date(user.birthDate);
        const options = {year: 'numeric', month: 'long', day: 'numeric'};
        document.getElementById('user-birthdate').textContent = date.toLocaleDateString('en-US', options);
    } else {
        document.getElementById('user-birthdate').textContent = '--';
    }
}

function populateFitnessProfile(profile) {
    document.getElementById('user-height').textContent = profile.heightCm || '--';
    document.getElementById('user-weight').textContent = profile.weightKg || '--';

    let goal = profile.fitnessGoal ? profile.fitnessGoal.replace(/_/g, ' ') : '--';
    let activity = profile.activityLevel ? profile.activityLevel.replace(/_/g, ' ') : '--';

    document.getElementById('user-fitnessGoal').textContent = formatEnumText(goal);
    document.getElementById('user-activityLevel').textContent = formatEnumText(activity);

    const dataSection = document.getElementById('fitness-profile-data');
    const emptySection = document.getElementById('fitness-profile-empty');
    if (dataSection && emptySection) {
        emptySection.classList.add('hidden');
        emptySection.classList.remove('flex');
        dataSection.classList.remove('hidden');
    }
}

function formatEnumText(text) {
    if (text === '--') return text;
    return text.toLowerCase().split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
}

function renderFavoriteExercises(exercises) {
    const container = document.getElementById('user-favoriteExercises');
    const badge = document.getElementById('favorites-count-badge');
    if (!container) return;

    if (badge) badge.textContent = `${exercises.length} Saved`;
    container.innerHTML = '';

    if (!exercises || exercises.length === 0) {
        container.innerHTML = `
            <div class="col-span-full flex flex-col items-center justify-center p-8 bg-[#131313] rounded-xl border border-[#262626] border-dashed text-center">
                <span class="material-symbols-outlined text-[32px] text-on-surface-variant mb-2">heart_broken</span>
                <p class="font-body text-sm text-on-surface-variant">You have no saved exercises.</p>
                <a href="ExerciseLibrary.html" class="mt-4 text-primary font-label text-xs uppercase hover:underline">Explore Library</a>
            </div>
        `;
        return;
    }

    exercises.forEach(ex => {
        const primaryMuscle = (ex.muscleGroups && ex.muscleGroups.length > 0) ? ex.muscleGroups[0] : (ex.muscle || 'Body');
        const card = document.createElement('article');
        card.className = "bg-[#1A1A1A] rounded-2xl border border-[#2A2A2A] overflow-hidden group hover:border-primary/40 hover:bg-[#202020] transition-all duration-300 flex flex-col cursor-pointer";
        card.onclick = () => window.location.href = `ExerciseTemplate.html?id=${ex.id}`;

        const fallbackImg = "https://placehold.co/400x400/1A1A1A/E31C25?text=No+Image";
        const imageSrc = (ex.thumbnailPath && ex.thumbnailPath !== "null" && ex.thumbnailPath !== "")
            ? `..${ex.thumbnailPath}`
            : fallbackImg;

        card.innerHTML = `
                <div class="relative h-44 w-full bg-[#111111] shrink-0 flex items-center justify-center border-b border-[#2A2A2A]/50 overflow-hidden">
                    <img src="${escapeHtml(imageSrc)}" onerror="this.src='${fallbackImg}'" alt="${escapeHtml(ex.name)}" class="max-w-[70%] max-h-[80%] object-contain group-hover:scale-110 transition-transform duration-500 mix-blend-luminosity hover:mix-blend-normal opacity-80 hover:opacity-100 z-10">
                    <span class="absolute top-3 right-3 z-30 w-8 h-8 rounded-full bg-black/70 backdrop-blur border border-[#2A2A2A] flex items-center justify-center text-primary-container" title="Favorited">
                        <span class="material-symbols-outlined text-[18px] fill">favorite</span>
                    </span>
                    <div class="absolute inset-0 bg-gradient-to-t from-background via-transparent to-transparent pointer-events-none z-20"></div>
                </div>
                <div class="p-5 flex flex-col flex-1 justify-between gap-4">
                    <div>
                        <h3 class="font-headline text-[20px] font-bold text-on-surface mb-1.5 leading-tight">${escapeHtml(ex.name)}</h3>
                        <p class="font-body text-[14px] text-on-surface-variant line-clamp-2 leading-relaxed">${escapeHtml(ex.description || '')}</p>
                    </div>
                    <div class="flex flex-wrap gap-2 pt-3 border-t border-[#2A2A2A]/60 mt-auto">
                        <span class="inline-flex items-center gap-1.5 bg-[#0F0F0F] border border-[#2A2A2A] rounded px-2.5 py-1 font-label text-[12px] text-on-surface uppercase tracking-wider">
                            <span class="material-symbols-outlined text-[14px] text-primary-container fill">fitness_center</span> ${escapeHtml(primaryMuscle)}
                        </span>
                        <span class="inline-flex items-center gap-1.5 bg-[#0F0F0F] border border-[#2A2A2A] rounded px-2.5 py-1 font-label text-[12px] text-on-surface uppercase tracking-wider">
                            ${escapeHtml(ex.difficulty || 'All Levels')}
                        </span>
                        <span class="inline-flex items-center gap-1.5 bg-[#0F0F0F] border border-[#2A2A2A] rounded px-2.5 py-1 font-label text-[12px] text-on-surface uppercase tracking-wider">
                            ${escapeHtml(ex.equipment || 'Standard')}
                        </span>
                    </div>
                </div>
            `;
        container.appendChild(card);
    });
}

function renderWorkouts(workouts, authHeaders) {
    const container = document.getElementById('user-workouts');
    const badge = document.getElementById('workouts-count-badge');
    if (!container) return;

    if (badge) badge.textContent = `${workouts ? workouts.length : 0} Routines Active`;
    container.innerHTML = '';

    if (!workouts || workouts.length === 0) {
        container.innerHTML = `
            <div class="col-span-full flex flex-col items-center justify-center p-8 bg-[#131313] rounded-xl border border-[#262626] border-dashed text-center">
                <span class="material-symbols-outlined text-[32px] text-on-surface-variant mb-2">assignment</span>
                <h3 class="font-headline text-[18px] text-on-surface mb-2">No Active Workouts</h3>
                <p class="font-body text-sm text-on-surface-variant mb-4">You haven't created any custom training regimens yet.</p>
                <button class="inline-flex items-center gap-2 bg-primary/20 text-primary border border-primary/50 px-4 py-2 rounded-lg text-xs font-label uppercase tracking-widest hover:bg-primary/30 transition-colors opacity-50 cursor-not-allowed" title="Coming soon">
                    Create Workout
                </button>
            </div>
        `;
        return;
    }

    workouts.forEach(workout => {
        const card = document.createElement('article');
        card.className = "bg-[#141414] border border-[#262626] rounded-2xl p-6 hover:border-primary/40 hover:bg-[#1a1a1a] transition-all duration-300 flex flex-col justify-between group relative";

        const exerciseCount = workout.workoutExercises ? workout.workoutExercises.length : 0;
        let exercisesHTML = '';

        if (exerciseCount > 0) {
            const previewExercises = workout.workoutExercises.slice(0, 10);

            exercisesHTML = previewExercises.map(we => `
                <div class="flex items-center justify-between text-xs font-body text-on-surface">
                    <span class="flex items-center gap-2 truncate pr-2">
                        <span class="w-1.5 h-1.5 rounded-full bg-primary shrink-0"></span>
                        <span class="truncate">${escapeHtml(we.exerciseName || 'Unknown Exercise')}</span>
                    </span>
                    <span class="text-on-surface-variant font-label shrink-0">${we.sets} Sets · ${we.reps} Reps</span>
                </div>
            `).join('');

            if (exerciseCount > 10) {
                exercisesHTML += `
                 <div class="flex items-center justify-center pt-2 mt-2 border-t border-[#222222]/50">
                     <span class="text-[10px] uppercase font-label text-on-surface-variant tracking-widest">+ ${exerciseCount - 10} more</span>
                 </div>`;
            }
        } else {
            exercisesHTML = `<div class="text-xs font-body text-on-surface-variant italic">No exercises added yet.</div>`;
        }

        card.innerHTML = `
            <button class="delete-workout-btn absolute -top-3 -right-3 z-20 w-8 h-8 rounded-full bg-[#131313] border border-[#262626] flex items-center justify-center text-on-surface-variant hover:text-error hover:border-error/50 hover:bg-error/10 transition-colors opacity-0 group-hover:opacity-100 shadow-md" data-id="${workout.id}" title="Delete Workout">
                <span class="material-symbols-outlined text-[16px]">close</span>
            </button>

            <div>
                <div class="flex items-center justify-between mb-4 gap-3">
                    <h3 class="font-headline text-[20px] font-bold text-on-surface line-clamp-1 truncate">${escapeHtml(workout.name)}</h3>
                    <span class="shrink-0 text-[11px] text-on-surface-variant font-label bg-[#0d0d0d] px-2.5 py-1 rounded-md border border-[#262626]">
                        ${exerciseCount} Exercises
                    </span>
                </div>

                <div class="space-y-2 border-t border-[#222222] pt-4 mb-2">
                    ${exercisesHTML}
                </div>
            </div>
        `;

        container.appendChild(card);
    });

    const deleteBtns = document.querySelectorAll('.delete-workout-btn');
    deleteBtns.forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.stopPropagation();
            const workoutId = btn.getAttribute('data-id');
            const workoutCard = btn.closest('article');

            if (confirm("Are you sure you want to delete this workout?")) {
                const originalContent = btn.innerHTML;
                btn.innerHTML = '<span class="material-symbols-outlined text-[16px] animate-spin">sync</span>';
                btn.disabled = true;

                try {
                    const deleteRes = await fetch(`http://localhost:8080/api/v1/workouts/${workoutId}`, {
                        method: 'DELETE',
                        headers: authHeaders
                    });

                    if (deleteRes.ok) {
                        workoutCard.classList.add('opacity-0', 'scale-95');
                        setTimeout(() => {
                            workoutCard.remove();
                            const newCount = document.querySelectorAll('#user-workouts article').length;
                            if (badge) badge.textContent = `${newCount} Routines Active`;

                            if (newCount === 0) {
                                renderWorkouts([], authHeaders);
                            }
                        }, 300);

                        if (typeof showNotification === 'function') {
                            showNotification("Workout deleted successfully!");
                        }
                    } else {
                        throw new Error(`Failed to delete workout. Status: ${deleteRes.status}`);
                    }
                } catch (err) {
                    console.error("Delete Workout Error:", err);
                    if (typeof showNotification === 'function') {
                        showNotification("Failed to delete workout.", true);
                    } else {
                        alert("Failed to delete workout.");
                    }
                    btn.innerHTML = originalContent;
                    btn.disabled = false;
                }
            }
        });
    });
}

let allExercises = [];
let workoutItems = [];
let currentFilter = 'All';

const muscleParentMap = {
    'Chest': ['Chest', 'Upper Chest', 'Lower Chest'],
    'Back': ['Back', 'Lats', 'Trapezius'],
    'Shoulders': ['Shoulders', 'Front Delts', 'Lateral Delts', 'Rear Delts'],
    'Arms': ['Arms', 'Biceps', 'Triceps', 'Forearms'],
    'Core': ['Core', 'Abs', 'Obliques'],
    'Legs': ['Legs', 'Glutes', 'Quads', 'Hip Flexors', 'Calves']
};

const libraryContainer = document.getElementById('exercise-library');
const workoutContainer = document.getElementById('workout-list');
const emptyState = document.getElementById('empty-state');
const filterButtons = document.querySelectorAll('.filter-btn');

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem('apexion_token');
    if (!token) {
        if (typeof showNotification === 'function') {
            showNotification("Please log in to create custom workouts.", true);
        } else {
            alert("Please log in to create custom workouts.");
        }
        setTimeout(() => window.location.href = 'LoginRegister.html', 1500);
        return;
    }

    fetch('http://localhost:8080/api/v1/exercises')
        .then(response => {
            if (!response.ok) throw new Error("Failed to load exercises.");
            return response.json();
        })
        .then(data => {
            allExercises = data;
            renderLibrary();
        })
        .catch(error => {
            console.error("Failed to Fetch Data :", error);
            if (libraryContainer) {
                libraryContainer.innerHTML = '<p class="text-error text-center py-4">Failed to load exercise library.</p>';
            }
        });
});

function renderLibrary() {
    if (!libraryContainer) return;
    libraryContainer.innerHTML = '';

    let filteredExercises = allExercises;
    if (currentFilter !== 'All') {
        const allowedMuscles = muscleParentMap[currentFilter] || [currentFilter];
        filteredExercises = allExercises.filter(ex =>
            ex.muscleGroups && ex.muscleGroups.some(m => allowedMuscles.includes(m))
        );
    }

    if (filteredExercises.length === 0) {
        libraryContainer.innerHTML = '<p class="text-on-surface-variant text-center py-4">No exercises found for this category.</p>';
        return;
    }

    filteredExercises.forEach(ex => {
        const primaryMuscle = ex.muscleGroups && ex.muscleGroups.length > 0 ? ex.muscleGroups[0] : 'N/A';

        const card = document.createElement('div');
        card.className = 'flex items-center justify-between p-3 bg-surface-container-low rounded-lg border border-surface-variant hover:border-primary-container/50 transition-colors group';
        card.innerHTML = `
            <div class="flex items-center gap-4">
                <div class="w-12 h-12 rounded bg-surface-container-highest flex items-center justify-center text-on-surface-variant shrink-0">
                    <span class="material-symbols-outlined text-[24px]">fitness_center</span>
                </div>
                <div>
                    <h4 class="font-body-md text-[16px] font-semibold text-on-surface leading-tight">${ex.name}</h4>
                    <p class="font-label-md text-[12px] text-on-surface-variant mt-1 uppercase tracking-wider">${primaryMuscle}</p>
                </div>
            </div>
            <button onclick="addExercise(${ex.id})" title="Add to Workout" class="w-10 h-10 rounded-full bg-surface-container-highest border border-surface-variant flex items-center justify-center text-primary-container hover:bg-primary-container hover:text-white transition-all shrink-0">
                <span class="material-symbols-outlined">add</span>
            </button>
        `;
        libraryContainer.appendChild(card);
    });
}

filterButtons.forEach(btn => {
    btn.addEventListener('click', (e) => {

        filterButtons.forEach(b => {
            b.classList.remove('bg-primary-container', 'text-white', 'border-primary-container');
            b.classList.add('bg-surface-variant', 'text-on-surface', 'border-surface-variant');
        });

        const targetBtn = e.currentTarget;
        targetBtn.classList.remove('bg-surface-variant', 'text-on-surface', 'border-surface-variant');
        targetBtn.classList.add('bg-primary-container', 'text-white', 'border-primary-container');

        currentFilter = targetBtn.getAttribute('data-filter');
        renderLibrary();
    });
});

function renderWorkout() {
    if (!workoutContainer) return;

    if (workoutItems.length === 0) {
        if (emptyState) emptyState.style.display = 'flex';
        Array.from(workoutContainer.children).forEach(child => {
            if (child.id !== 'empty-state') child.remove();
        });
        return;
    }

    if (emptyState) emptyState.style.display = 'none';

    Array.from(workoutContainer.children).forEach(child => {
        if (child.id !== 'empty-state') child.remove();
    });

    workoutItems.forEach((item, index) => {
        const el = document.createElement('div');
        el.className = 'mb-3 bg-surface-container-low rounded-lg border border-surface-variant p-4 flex flex-col gap-4 shadow-sm';
        el.innerHTML = `
            <div class="flex items-center justify-between border-b border-surface-variant pb-3">
                <div class="flex items-center gap-2">
                    <span class="text-on-surface-variant font-headline-lg text-[20px] w-6">${index + 1}.</span>
                    <h4 class="font-body-md text-[16px] font-semibold text-on-surface truncate max-w-[200px]" title="${item.name}">${item.name}</h4>
                </div>
                <div class="flex gap-1 shrink-0">
                    <button onclick="moveUp(${index})" title="Move Up" class="p-1 rounded hover:bg-surface-variant text-on-surface-variant transition-colors" ${index === 0 ? 'disabled style="opacity: 0.3; cursor: not-allowed;"' : ''}>
                        <span class="material-symbols-outlined text-[20px]">arrow_upward</span>
                    </button>
                    <button onclick="moveDown(${index})" title="Move Down" class="p-1 rounded hover:bg-surface-variant text-on-surface-variant transition-colors" ${index === workoutItems.length - 1 ? 'disabled style="opacity: 0.3; cursor: not-allowed;"' : ''}>
                        <span class="material-symbols-outlined text-[20px]">arrow_downward</span>
                    </button>
                    <button onclick="removeExercise(${index})" title="Remove Exercise" class="p-1 rounded hover:bg-error/20 text-error transition-colors ml-2">
                        <span class="material-symbols-outlined text-[20px]">close</span>
                    </button>
                </div>
            </div>
            <div class="grid grid-cols-3 gap-3">
                <div>
                    <label class="block font-label-md text-[12px] text-on-surface-variant mb-1 uppercase tracking-wider">Sets</label>
                    <input type="number" min="1" max="10" value="3" id="sets-${item.uid}" class="w-full bg-[#1A1A1A] border border-[#2A2A2A] rounded p-2 text-on-surface text-center font-body-md focus:border-primary-container focus:outline-none focus:ring-1 focus:ring-primary-container h-10 transition-colors">
                </div>
                <div>
                    <label class="block font-label-md text-[12px] text-on-surface-variant mb-1 uppercase tracking-wider">Reps</label>
                    <input type="number" min="1" max="100" value="10" id="reps-${item.uid}" class="w-full bg-[#1A1A1A] border border-[#2A2A2A] rounded p-2 text-on-surface text-center font-body-md focus:border-primary-container focus:outline-none focus:ring-1 focus:ring-primary-container h-10 transition-colors">
                </div>
                <div>
                    <label class="block font-label-md text-[12px] text-on-surface-variant mb-1 uppercase tracking-wider">Weight (kg)</label>
                    <input type="number" min="0" max="500" step="0.5" value="0" id="weight-${item.uid}" class="w-full bg-[#1A1A1A] border border-[#2A2A2A] rounded p-2 text-on-surface text-center font-body-md focus:border-primary-container focus:outline-none focus:ring-1 focus:ring-primary-container h-10 transition-colors">
                </div>
            </div>
        `;
        workoutContainer.appendChild(el);
    });
}

window.addExercise = (id) => {
    const ex = allExercises.find(e => e.id === id);
    if (ex) {
        workoutItems.push({...ex, uid: Date.now()});
        renderWorkout();
        setTimeout(() => {
            workoutContainer.scrollTop = workoutContainer.scrollHeight;
        }, 50);
    }
};

window.removeExercise = (index) => {
    workoutItems.splice(index, 1);
    renderWorkout();
};

window.moveUp = (index) => {
    if (index > 0) {
        const temp = workoutItems[index - 1];
        workoutItems[index - 1] = workoutItems[index];
        workoutItems[index] = temp;
        renderWorkout();
    }
};

window.moveDown = (index) => {
    if (index < workoutItems.length - 1) {
        const temp = workoutItems[index + 1];
        workoutItems[index + 1] = workoutItems[index];
        workoutItems[index] = temp;
        renderWorkout();
    }
};

window.saveWorkout = async () => {
    const workoutNameInput = document.getElementById('workout-name');
    if (!workoutNameInput) return;

    const workoutName = workoutNameInput.value.trim();
    const token = localStorage.getItem('apexion_token');

    const saveBtn = document.querySelector('button[onclick="saveWorkout()"]');
    const originalBtnHtml = saveBtn ? saveBtn.innerHTML : 'Save Workout';

    if (!token) {
        if (typeof showNotification === 'function') showNotification("Authentication error. Please log in again.", true);
        else alert("Authentication error. Please log in again.");
        return;
    }

    if (!workoutName) {
        if (typeof showNotification === 'function') showNotification("Please provide a name for your routine.", true);
        else alert("Please provide a name for your routine.");
        workoutNameInput.focus();
        return;
    }

    if (workoutItems.length === 0) {
        if (typeof showNotification === 'function') showNotification("You must add at least one exercise.", true);
        else alert("You must add at least one exercise.");
        return;
    }

    if (saveBtn) {
        saveBtn.disabled = true;
        saveBtn.innerHTML = '<span class="material-symbols-outlined text-[18px] animate-spin">sync</span> Saving...';
    }

    const payload = {
        name: workoutName,
        exercises: workoutItems.map((item, index) => {
            return {
                exerciseId: item.id,
                sortOrder: index,
                sets: parseInt(document.getElementById(`sets-${item.uid}`).value) || 3,
                reps: parseInt(document.getElementById(`reps-${item.uid}`).value) || 10,
                weight: parseFloat(document.getElementById(`weight-${item.uid}`).value) || 0
            }
        })
    };

    try {
        const response = await fetch('http://localhost:8080/api/v1/workouts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            if (typeof showNotification === 'function') {
                showNotification("Workout saved successfully!");
            } else {
                alert("Workout saved successfully!");
            }

            workoutItems = [];
            workoutNameInput.value = '';
            renderWorkout();

        } else if (response.status === 401 || response.status === 403) {
            throw new Error("Session expired. Please log in again.");
        } else {
            throw new Error(`Failed to save workout. (Status: ${response.status})`);
        }
    } catch (err) {
        console.error("Save Workout Error:", err);
        if (typeof showNotification === 'function') {
            showNotification(err.message, true);
        } else {
            alert(err.message);
        }
    } finally {
        if (saveBtn) {
            saveBtn.disabled = false;
            saveBtn.innerHTML = originalBtnHtml;
        }
    }
};

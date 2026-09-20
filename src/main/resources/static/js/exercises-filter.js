const FilterService = {
    muscleParentMap: {
        'Chest': ['Upper Chest', 'Lower Chest'],
        'Back': ['Lats', 'Trapezius'],
        'Shoulders': ['Front Delts', 'Lateral Delts', 'Rear Delts'],
        'Arms': ['Biceps', 'Triceps', 'Forearms'],
        'Core': ['Abs', 'Obliques'],
        'Legs': ['Glutes', 'Quads', 'Hip Flexors', 'Calves', 'Hamstrings']
    },

    activeFilters: {
        muscle: [],
        difficulty: [],
        equipment: []
    }
};

const UIManager = {
    showingContainer: document.getElementById('showing-container'),
    activePills: document.getElementById('active-pills'),
    checkboxes: document.querySelectorAll('.filter-checkbox'),
    clearBtn: document.getElementById('clear-filters-btn'),

    init() {
        if(!this.showingContainer || !this.activePills || !this.clearBtn) return;

        this.checkboxes.forEach(cb => {
            cb.addEventListener('change', () => this.handleFilterChange());
        });

        this.clearBtn.addEventListener('click', () => {
            this.checkboxes.forEach(cb => cb.checked = false);
            this.handleFilterChange();
        });
    },

    handleFilterChange() {
        FilterService.activeFilters = { muscle: [], difficulty: [], equipment: [] };

        const checkedBoxes = Array.from(this.checkboxes).filter(cb => cb.checked);
        checkedBoxes.forEach(cb => {
            const cat = cb.getAttribute('data-category');
            const label = cb.getAttribute('data-label');
            if (cat) FilterService.activeFilters[cat].push(label);
        });

        this.renderPills(checkedBoxes);
        applyFilters();
    },

    renderPills(checkedBoxes) {
        this.activePills.innerHTML = '';

        if (checkedBoxes.length === 0) {
            this.showingContainer.classList.add('hidden');
            this.showingContainer.classList.remove('flex');
            return;
        }

        this.showingContainer.classList.remove('hidden');
        this.showingContainer.classList.add('flex');

        checkedBoxes.forEach(cb => {
            const label = cb.getAttribute('data-label');
            const pill = document.createElement('div');
            pill.className = 'flex items-center gap-1.5 bg-surface-container-high border border-surface-variant rounded-full px-3 py-1';
            pill.innerHTML = `
                <span class="font-label-md text-[14px] text-on-surface uppercase">${label}</span>
                <button type="button" class="material-symbols-outlined text-[16px] text-on-surface-variant hover:text-primary transition-colors leading-none cursor-pointer">close</button>
            `;
            pill.querySelector('button').addEventListener('click', () => {
                cb.checked = false;
                this.handleFilterChange();
            });
            this.activePills.appendChild(pill);
        });
    }
};

window.applyFilters = function() {
    let expandedMuscles = [];

    FilterService.activeFilters.muscle.forEach(m => {
        expandedMuscles.push(m);
        if (FilterService.muscleParentMap[m]) expandedMuscles.push(...FilterService.muscleParentMap[m]);
    });

    const filteredData = exercisesData.filter(ex => {

        const diffMatch = FilterService.activeFilters.difficulty.length === 0 ||
            FilterService.activeFilters.difficulty.includes(ex.difficulty);

        const equipMatch = FilterService.activeFilters.equipment.length === 0 ||
            (ex.equipment && FilterService.activeFilters.equipment.some(eq => ex.equipment.toUpperCase().includes(eq.toUpperCase())));

        const muscleMatch = FilterService.activeFilters.muscle.length === 0 ||
            (ex.muscleGroups && ex.muscleGroups.some(m => expandedMuscles.includes(m)));

        return diffMatch && equipMatch && muscleMatch;
    });

    if(typeof renderExercises === "function") {
        renderExercises(filteredData);
    }
};

document.addEventListener("DOMContentLoaded", () => {
    UIManager.init();
});
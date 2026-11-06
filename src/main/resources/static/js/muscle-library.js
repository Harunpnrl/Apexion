document.addEventListener('DOMContentLoaded', async () => {
    const selectedMuscleDisplay = document.getElementById('selected-muscle-name');
    const selectedMusclePanel = document.getElementById('selected-muscle-panel');
    const statusPing = document.getElementById('status-ping');
    const statusDot = document.getElementById('status-dot');

    const svgContainer = document.getElementById('svg-container');
    const svgLoader = document.getElementById('svg-loader');
    const viewMap = document.getElementById('view-map');
    const viewDetails = document.getElementById('view-details');
    const btnBack = document.getElementById('btn-back-to-map');

    const detailMuscleName = document.getElementById('detail-muscle-name');
    const panelOverview = document.getElementById('panel-overview');
    const panelAnatomy = document.getElementById('panel-anatomy');
    const panelFunction = document.getElementById('panel-function');
    const panelExercises = document.getElementById('panel-exercises');
    const panelSynergists = document.getElementById('panel-synergists');
    const panelRecoveryTime = document.getElementById('panel-recovery-time');

    try {
        const response = await fetch('../assets/2d_svg_anatomy_simple.svg');
        if (response.ok) {
            const svgText = await response.text();
            svgLoader.classList.add('hidden');
            svgContainer.innerHTML = svgText;

            const svgElement = svgContainer.querySelector('svg');
            if (svgElement) {
                svgElement.classList.add('w-full', 'h-full', 'max-h-[500px]', 'drop-shadow-lg', 'transition-all');
            }
        } else {
            svgLoader.innerHTML = '<span class="text-error">Failed to load Anatomy graphic.</span>';
        }
    } catch (error) {
        console.error("SVG load error:", error);
        svgLoader.innerHTML = '<span class="text-error">Error loading graphic.</span>';
    }

    async function openMuscleDetails(muscleName, muscleId = '') {
        if (!muscleName) return;

        selectedMuscleDisplay.textContent = muscleName;
        selectedMuscleDisplay.classList.remove('text-on-surface');
        selectedMuscleDisplay.classList.add('text-primary');
        selectedMusclePanel.classList.add('border-primary/50', 'bg-[#1a0f0f]');
        statusDot.classList.remove('bg-primary-container');
        statusDot.classList.add('bg-primary');

        if (statusPing) {
            statusPing.classList.remove('hidden');
            statusPing.classList.remove('bg-primary-container');
            statusPing.classList.add('bg-primary');
            setTimeout(() => {
                statusPing.classList.add('hidden');
            }, 1200);
        }

        try {
            const response = await fetch(`http://localhost:8080/api/v1/muscle-info/${muscleName}`);

            if (!response.ok) {
                throw new Error("Muscle data could not found.");
            }

            const data = await response.json();

            detailMuscleName.textContent = data.muscleName || muscleName;
            panelOverview.textContent = data.overview || "Data could not found.";
            panelAnatomy.textContent = data.anatomy || "Data could not found.";
            panelFunction.textContent = data.muscleFunction || "Data could not found.";

            if(panelRecoveryTime) {
                panelRecoveryTime.textContent = data.recoveryWindow || "48 Hours";
            }

            const exercisesArray = data.exampleExercises ? data.exampleExercises.split(',').map(e => e.trim()) : [];
            panelExercises.innerHTML = exercisesArray.map(ex =>
                `<li class="flex items-center gap-3">
                    <span class="w-1.5 h-1.5 bg-primary rounded-full shrink-0"></span>
                    <span>${escapeHtml(ex)}</span>
                </li>`
            ).join('');

            const synergistsArray = data.synergistMuscles ? data.synergistMuscles.split(',').map(s => s.trim()) : [];
            panelSynergists.innerHTML = synergistsArray.map(syn =>
                `<span class="font-label-md text-label-md bg-surface-dim px-3 py-1.5 rounded-md text-on-surface border border-surface-variant shadow-sm">
                    ${escapeHtml(syn)}
                </span>`
            ).join('');

            viewMap.classList.add('opacity-0');

            setTimeout(() => {
                viewMap.classList.add('hidden');
                viewDetails.classList.remove('hidden');
                viewDetails.classList.add('flex');

                setTimeout(() => {
                    viewDetails.classList.remove('opacity-0');
                }, 50);
            }, 300);

        } catch (error) {
            console.error("API Data Fetch Error:", error);
            alert(`No information was found in the database for "${muscleName}" Please check your SQL data.`);

            const activeMuscles = svgContainer.querySelectorAll('svg g.active-muscle');
            activeMuscles.forEach(el => el.classList.remove('active-muscle'));
            selectedMuscleDisplay.textContent = "None";
            selectedMusclePanel.classList.remove('border-primary/50', 'bg-[#1a0f0f]');
        }
    }

    if (svgContainer) {
        svgContainer.addEventListener('click', (event) => {
            const group = event.target.closest('svg g:not([inkscape\\:label="none"])');
            if (!group) return;

            const activeMuscles = svgContainer.querySelectorAll('svg g.active-muscle');
            activeMuscles.forEach(el => {
                el.classList.remove('active-muscle');
            });

            group.classList.add('active-muscle');
            let rawMuscle = group.getAttribute('inkscape:label') || group.getAttribute('id');

            if (rawMuscle) {
                let cleanName = rawMuscle
                    .replace(/^(g[-_]|muscle[-_]|group[-_])/i, '')
                    .replace(/[-_]/g, ' ')
                    .trim();

                cleanName = cleanName.replace(/\b\w/g, char => char.toUpperCase());
                openMuscleDetails(cleanName, rawMuscle);
            }
        });
    }

    if (btnBack) {
        btnBack.addEventListener('click', () => {
            viewDetails.classList.add('opacity-0');

            const activeMuscles = svgContainer.querySelectorAll('svg g.active-muscle');
            activeMuscles.forEach(el => {
                el.classList.remove('active-muscle');
            });

            selectedMuscleDisplay.textContent = "None";
            selectedMuscleDisplay.classList.add('text-on-surface');
            selectedMuscleDisplay.classList.remove('text-primary');
            selectedMusclePanel.classList.remove('border-primary/50', 'bg-[#1a0f0f]');
            statusDot.classList.add('bg-primary-container');
            statusDot.classList.remove('bg-primary');

            setTimeout(() => {
                viewDetails.classList.add('hidden');
                viewMap.classList.remove('hidden');

                setTimeout(() => {
                    viewMap.classList.remove('opacity-0');
                }, 50);
            }, 300);
        });
    }
    window.openMuscleDetails = openMuscleDetails;
});


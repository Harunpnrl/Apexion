let exercisesData = [];
let userFavorites = new Set();

const grid = document.getElementById('exercise-grid');

document.addEventListener("DOMContentLoaded", async () => {
    try {
        grid.innerHTML = '<p class="text-on-surface-variant col-span-full py-8 text-center text-lg">Loading exercises...</p>';

        const token = localStorage.getItem('apexion_token');
        const fetchPromises = [fetch('http://localhost:8080/api/v1/exercises')];

        if (token) {
            fetchPromises.push(
                fetch(`http://localhost:8080/api/v1/users/me/favorites`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })
            );
        }

        const responses = await Promise.all(fetchPromises);
        const exercisesRes = responses[0];

        if (!exercisesRes.ok) throw new Error("Failed to fetch exercises.");
        exercisesData = await exercisesRes.json();

        if (responses.length > 1) {
            const favoritesRes = responses[1];
            if (favoritesRes.ok) {
                const favData = await favoritesRes.json();
                userFavorites = new Set(favData.map(ex => ex.id));
            } else {
                console.warn("Failed to fetch favorites, token might be expired.");
            }
        }

        if (typeof applyFilters === "function") {
            applyFilters();
        } else if (typeof window.app !== 'undefined' && typeof window.app.handleFilterChange === 'function') {
            // Fallback for the modular logic we wrote earlier
            window.app.handleFilterChange();
        } else {
            renderExercises(exercisesData);
        }

    } catch (error) {
        console.error("API Error:", error);
        grid.innerHTML = '<p class="text-error col-span-full py-8 text-center text-xl">Failed to load data. Is the server running?</p>';
    }
});

function renderExercises(data) {
    grid.innerHTML = '';

    if (!data || data.length === 0) {
        grid.innerHTML = '<p class="text-on-surface-variant col-span-full py-8 text-center text-lg">No exercises found.</p>';
        return;
    }

    data.forEach(ex => {
        const primaryMuscle = ex.muscleGroups && ex.muscleGroups.length > 0 ? ex.muscleGroups[0] : 'Body';
        const isFavorited = userFavorites.has(ex.id);
        const favClasses = isFavorited ? 'fill text-primary-container' : 'text-on-surface-variant';

        const cardHTML = `
        <article onclick="window.location.href='ExerciseTemplate.html?id=${ex.id}'" class="bg-[#1A1A1A] rounded-[16px] border border-[#2A2A2A] overflow-hidden group hover:border-[rgba(227,28,37,0.4)] hover:bg-[#222222] transition-all duration-300 flex flex-col cursor-pointer">
            <div class="relative h-48 w-full bg-surface-container-lowest shrink-0 flex items-center justify-center overflow-hidden">
            
                <img src="${escapeHtml(`..${ex.thumbnailPath || ''}`)}" onerror="this.src='https://placehold.co/400x400/1A1A1A/E31C25?text=No+Image'"
                     alt="${escapeHtml(ex.name)}"
                     class="max-w-[70%] max-h-[80%] object-contain group-hover:scale-110 transition-transform duration-500 mix-blend-luminosity hover:mix-blend-normal opacity-80 hover:opacity-100 z-10">
                     
                <button onclick="toggleFavorite(event, ${ex.id}, this)" class="absolute top-3 right-3 z-30 w-8 h-8 rounded-full bg-black/60 backdrop-blur border border-[#2A2A2A] flex items-center justify-center hover:text-primary-container hover:border-primary-container transition-colors">
                    <span class="material-symbols-outlined text-[18px] favorite-icon ${favClasses}">favorite</span>
                </button>

                <div class="absolute inset-0 bg-gradient-to-t from-background via-transparent to-transparent pointer-events-none z-20"></div>
            </div>

            <div class="p-5 flex flex-col flex-1 justify-between gap-4">
                <div>
                     <h3 class="font-headline-md text-[24px] font-semibold text-on-surface mb-2 leading-tight">${escapeHtml(ex.name)}</h3>
                     <p class="font-body-md text-[15px] text-on-surface-variant line-clamp-2">${escapeHtml(ex.description || 'No description available.')}</p>
                </div>
                <div class="flex flex-wrap gap-2 pt-4 border-t border-[#2A2A2A]/40 mt-auto">
                    <span class="inline-flex items-center gap-1 bg-[#0F0F0F] border border-[#2A2A2A] rounded px-2 py-1 font-label-md text-[14px] text-on-surface uppercase tracking-wider">
                        <span class="material-symbols-outlined text-[14px] text-primary-container fill">fitness_center</span> ${escapeHtml(primaryMuscle)}
                    </span>
                    
                    <span class="inline-flex items-center gap-1 bg-[#0F0F0F] border border-[#2A2A2A] rounded px-2 py-1 font-label-md text-[14px] text-on-surface uppercase tracking-wider">
                         ${escapeHtml(ex.difficulty || 'N/A')}
                    </span>
                    
                    <span class="inline-flex items-center gap-1 bg-[#0F0F0F] border border-[#2A2A2A] rounded px-2 py-1 font-label-md text-[14px] text-on-surface uppercase tracking-wider">
                         ${escapeHtml(ex.equipment || 'N/A')}
                    </span>
                </div>
            </div>
        </article>`;
        grid.innerHTML += cardHTML;
    });
}

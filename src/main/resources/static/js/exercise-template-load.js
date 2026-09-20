document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    const exerciseId = urlParams.get('id');

    if (!exerciseId) {
        document.querySelector('main').innerHTML = '<p class="text-error col-span-full py-8 text-center text-xl">Exercise ID not found. Please select an Exercise from the library.</p>';
        return;
    }

    fetch(`http://localhost:8080/api/v1/exercises/${exerciseId}`)
        .then(response => {
            if (!response.ok) throw new Error("HTTP error " + response.status);
            return response.json();
        })
        .then(data => {
            document.getElementById('exercise-name').textContent = data.name;
            document.getElementById('exercise-desc').textContent = data.description;

            const primaryMuscle = (data.muscleGroups && data.muscleGroups.length > 0) ? data.muscleGroups[0] : 'N/A';
            document.getElementById('exercise-muscle').textContent = primaryMuscle;

            document.getElementById('exercise-difficulty').textContent = data.difficulty || 'N/A';
            document.getElementById('exercise-equipment').textContent = data.equipment || 'N/A';

            if (data.animationPath) {
                document.getElementById('exercise-image').src = 'http://localhost:8080' + data.animationPath;
            }

            const instructionsContainer = document.getElementById('exercise-instructions');
            instructionsContainer.innerHTML = '';

            if (data.instructions) {
                const steps = data.instructions.split('.').map(step => step.trim()).filter(step => step.length > 0);
                steps.forEach(step => {
                    const li = document.createElement('li');
                    li.className = "pl-2";
                    li.textContent = step + ".";
                    instructionsContainer.appendChild(li);
                });
            } else {
                instructionsContainer.innerHTML = '<li class="pl-2">No instructions found.</li>';
            }
        })
        .catch(error => {
            console.error("Details could not be retrieved:", error);
            document.querySelector('main').innerHTML = '<p class="text-error col-span-full py-8 text-center text-xl">An error occurred while loading the data.</p>';
        });
});
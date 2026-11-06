document.addEventListener('DOMContentLoaded', () => {

    const form = document.getElementById('macro-form');
    const genderButtons = document.querySelectorAll('.gender-btn');
    const genderInput = document.getElementById('gender');
    const ageSlider = document.getElementById('age');
    const ageValue = document.getElementById('age-value');
    const emptyState = document.getElementById('empty-state');
    const resultData = document.getElementById('result-data');

    genderButtons.forEach(button => {
        button.addEventListener('click', () => {

            const selectedGender = button.dataset.gender;

            genderInput.value = selectedGender;
            genderButtons.forEach(btn => {
                btn.classList.remove('bg-primary-container', 'text-white', 'border-primary-container');
                btn.classList.add('bg-surface-container-lowest', 'text-on-surface-variant', 'border-surface-container-highest');
            });

            button.classList.remove('bg-surface-container-lowest', 'text-on-surface-variant', 'border-surface-container-highest');
            button.classList.add('bg-primary-container', 'text-white', 'border-primary-container');
        });
    });

    ageSlider.addEventListener('input', () => {
        ageValue.textContent = ageSlider.value;
    });

    form.addEventListener('submit', function (e) {
        e.preventDefault();


        const age = parseInt(ageSlider.value);
        const gender = genderInput.value;
        const weightKg = parseFloat(document.getElementById('weight').value);
        const heightCm = parseFloat(document.getElementById('height').value);
        const activityMulti = parseFloat(document.getElementById('activity').value);
        const goal = document.getElementById('goal').value;

        const validActivities = ['1.2', '1.375', '1.55', '1.725', '1.9'];
        const validGoals = ['cut', 'maintain', 'bulk'];
        const isValidGender = gender === 'male' || gender === 'female';
        const isValidAge = Number.isFinite(age) && age >= 15 && age <= 100;
        const isValidWeight = Number.isFinite(weightKg) && weightKg >= 20 && weightKg <= 300;
        const isValidHeight = Number.isFinite(heightCm) && heightCm >= 100 && heightCm <= 250;
        const isValidActivity = validActivities.includes(String(activityMulti));
        const isValidGoal = validGoals.includes(goal);

        if (!isValidGender || !isValidAge || !isValidWeight || !isValidHeight || !isValidActivity || !isValidGoal) {
            alert('Please enter valid values: age 15-100, weight 20-300 kg, height 100-250 cm, and select valid activity and goal options.');
            return;
        }

        let bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age);

        if (gender === 'male') {
            bmr += 5;
        } else {
            bmr -= 161;
        }

        const tdee = Math.round(bmr * activityMulti);

        let targetCals = tdee;
        let pPct;
        let cPct;
        let fPct;

        if (goal === 'cut') {
            targetCals = tdee - 500;
            pPct = 0.40;
            cPct = 0.30;
            fPct = 0.30;

        } else if (goal === 'bulk') {
            targetCals = tdee + 500;
            pPct = 0.30;
            cPct = 0.50;
            fPct = 0.20;

        } else {
            pPct = 0.30;
            cPct = 0.40;
            fPct = 0.30;
        }

        if (targetCals < 1200) {
            targetCals = 1200;
        }

        const pCals = Math.round(targetCals * pPct);
        const cCals = Math.round(targetCals * cPct);
        const fCals = Math.round(targetCals * fPct);

        const pGrams = Math.round(pCals / 4);
        const cGrams = Math.round(cCals / 4);
        const fGrams = Math.round(fCals / 9);


        emptyState.classList.add('hidden');
        resultData.classList.remove('hidden');
        resultData.classList.add('flex');

        const currentTarget = parseInt(document
            .getElementById('target-cals')
            .innerText
            .replace(/,/g, '')) || 0;

        animateValue('target-cals', currentTarget, targetCals, 1000);

        document.getElementById('tdee-text').innerText = `Maintenance TDEE: ${tdee.toLocaleString()} kcal`;

        document.getElementById('pct-protein').innerText = `${pPct * 100}%`;
        document.getElementById('pct-carbs').innerText = `${cPct * 100}%`;
        document.getElementById('pct-fats').innerText = `${fPct * 100}%`;

        animateValue('gram-protein', parseInt(document.getElementById('gram-protein').innerText) || 0, pGrams, 800);
        animateValue('gram-carbs', parseInt(document.getElementById('gram-carbs').innerText) || 0, cGrams, 800);
        animateValue('gram-fats', parseInt(document.getElementById('gram-fats').innerText) || 0, fGrams, 800);


        document.getElementById('cal-protein').innerText = pCals.toLocaleString();
        document.getElementById('cal-carbs').innerText = cCals.toLocaleString();
        document.getElementById('cal-fats').innerText = fCals.toLocaleString();

        const bars = document.querySelectorAll('.macro-bar-fill');

        if (bars.length >= 3) {
            bars[0].style.width = `${pPct * 100}%`;
            bars[1].style.width = `${cPct * 100}%`;
            bars[2].style.width = `${fPct * 100}%`;
        }

    });

    function animateValue(id, start, end, duration) {

        if (start === end) return;

        const range = end - start;
        let current = start;
        let increment = end > start ? 1 : -1;
        let stepTime = Math.abs(Math.floor(duration / range));


        if (stepTime < 10) {
            increment = Math.ceil(range / (duration / 10));
            stepTime = 10;
        }

        const obj = document.getElementById(id);

        const timer = setInterval(() => {
            current += increment;

            if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {

                current = end;

                clearInterval(timer);

            }
            obj.innerText = current.toLocaleString();
        }, stepTime);
    }
});

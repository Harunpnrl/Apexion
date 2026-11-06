document.addEventListener('DOMContentLoaded', () => {
    const calculateBtn = document.getElementById('calculate-btn');
    const genderButtons = document.querySelectorAll('.gender-btn');
    const genderInput = document.getElementById('gender');

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

    const ageSlider = document.getElementById('age');
    const ageValue = document.getElementById('age-value');

    ageSlider.addEventListener('input', () => {
        ageValue.textContent = ageSlider.value;
    });

    calculateBtn.addEventListener('click', () => {

        const gender = document.getElementById('gender').value;
        const age = parseFloat(document.getElementById('age').value);
        const weight = parseFloat(document.getElementById('weight').value);
        const height = parseFloat(document.getElementById('height').value);
        const activityFactor = parseFloat(document.getElementById('activity').value);

        const validActivities = ['1.2', '1.375', '1.55', '1.725', '1.9'];
        const isValidGender = gender === 'male' || gender === 'female';
        const isValidAge = Number.isFinite(age) && age >= 15 && age <= 100;
        const isValidWeight = Number.isFinite(weight) && weight >= 20 && weight <= 300;
        const isValidHeight = Number.isFinite(height) && height >= 100 && height <= 250;
        const isValidActivity = validActivities.includes(String(activityFactor));

        if (!isValidGender || !isValidAge || !isValidWeight || !isValidHeight || !isValidActivity) {
            alert('Please enter valid values: age 15-100, weight 20-300 kg, height 100-250 cm, and select an activity level.');
            return;
        }

        let bmr = (10 * weight) + (6.25 * height) - (5 * age);

        if (gender === 'male') {
            bmr += 5;
        } else {
            bmr -= 161;
        }

        const tdee = bmr * activityFactor;
        const cut = tdee - 500;
        const bulk = tdee + 500;

        const emptyState = document.getElementById('empty-state');
        const resultData = document.getElementById('result-data');

        if (emptyState && !emptyState.classList.contains('hidden')) {
            emptyState.classList.add('hidden');
            resultData.classList.remove('hidden');
            resultData.classList.add('flex');
        }

        document.getElementById('result-bmr').innerText = `Basal Metabolic Rate: ${Math.round(bmr).toLocaleString()} kcal`;

        animateValue('result-tdee', parseInt(document
            .getElementById('result-tdee')
            .innerText
            .replace(',', '')) || 0, Math.round(tdee), 800);

        animateValue('result-cut', parseInt(document
            .getElementById('result-cut')
            .innerText
            .replace(',', '')) || 0, Math.round(cut), 800);

        animateValue('result-bulk', parseInt(document
            .getElementById('result-bulk')
            .innerText
            .replace(',', '')) || 0, Math.round(bulk), 800);
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

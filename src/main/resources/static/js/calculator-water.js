document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('calculator-form');
    const weightInput = document.getElementById('weight');
    const activitySelect = document.getElementById('activity');

    const resultLiters = document.getElementById('result-liters');
    const resultMl = document.getElementById('result-ml');
    const progressBar = document.getElementById('progress-bar');

    const emptyState = document.getElementById('empty-state');
    const resultData = document.getElementById('result-data');

    const multipliers = {
        low: 30,
        moderate: 35,
        high: 40
    };

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const weight = parseFloat(weightInput.value);

        if (!Number.isFinite(weight) || weight < 20 || weight > 300) {
            alert('Please enter a valid weight between 20 and 300 kg.');
            return;
        }

        const activityLevel = activitySelect.value;
        const multiplier = multipliers[activityLevel];

        if (!Number.isFinite(multiplier)) {
            alert('Please select a valid activity level.');
            return;
        }

        const totalMl = weight * multiplier;
        const totalLiters = (totalMl / 1000).toFixed(1);

        if (emptyState && !emptyState.classList.contains('hidden')) {
            emptyState.classList.add('hidden');
            resultData.classList.remove('hidden');
            resultData.classList.add('flex');
        }

        animateValue("result-liters", parseFloat(resultLiters.textContent) || 0, parseFloat(totalLiters), 800, true);
        animateValue("result-ml", parseInt(resultMl.textContent.replace(/,/g, '')) || 0, Math.round(totalMl), 800, false);

        setTimeout(() => {
            progressBar.style.width = '100%';
        }, 100);
    });

    function animateValue(id, start, end, duration, isFloat) {
        if (start === end) return;
        var range = end - start;
        var current = start;
        var increment = end > start ? (isFloat ? 0.1 : 1) : (isFloat ? -0.1 : -1);
        var stepTime = Math.abs(Math.floor(duration / (isFloat ? range * 10 : range)));

        if (!isFloat && stepTime < 10) {
            increment = Math.ceil(range / (duration / 10));
            stepTime = 10;
        }

        var obj = document.getElementById(id);
        var timer = setInterval(function () {
            current += increment;

            if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {
                current = end;
                clearInterval(timer);
            }

            if (isFloat) {
                obj.innerHTML = current.toFixed(1);
            } else {
                obj.innerHTML = Math.round(current).toLocaleString();
            }
        }, stepTime);
    }
});

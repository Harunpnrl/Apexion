function calculateProtein() {
    const weightInput = document.getElementById('weight').value;
    const weight = parseFloat(weightInput);

    if (!Number.isFinite(weight) || weight < 20 || weight > 300) {
        alert("Please enter a valid weight between 20 and 300 kg.");
        return;
    }

    const selectedGoal = document.querySelector('input[name="goal"]:checked');
    const selectedActivity = document.querySelector('input[name="activity"]:checked');

    if (!selectedGoal || !selectedActivity) {
        alert("Please select a fitness goal and activity level.");
        return;
    }

    const goal = selectedGoal.value;
    const activity = selectedActivity.value;

    let multiplierLow = 1.6;
    let multiplierHigh = 2.2;

    if (goal === 'maintain') {
        multiplierLow = 1.2;
        multiplierHigh = 1.6;
    } else if (goal === 'lose') {
        multiplierLow = 1.8;
        multiplierHigh = 2.4;
    } else if (goal === 'build') {
        if (activity === 'sedentary') {
            multiplierLow = 1.4;
            multiplierHigh = 1.8;
        } else if (activity === 'very_active') {
            multiplierLow = 1.8;
            multiplierHigh = 2.4;
        } else {
            multiplierLow = 1.6;
            multiplierHigh = 2.2;
        }
    }

    const proteinLow = Math.round(weight * multiplierLow);
    const proteinHigh = Math.round(weight * multiplierHigh);
    const target = Math.round((proteinLow + proteinHigh) / 2);

    document.getElementById('empty-state').classList.add('hidden');
    document.getElementById('result-data').classList.remove('hidden');

    const startVal = parseInt(document.getElementById('result-value').innerText) || 0;
    animateValue("result-value", startVal, target, 800);

    document.getElementById('result-range').innerText = `Range: ${proteinLow}g - ${proteinHigh}g`;
}

function animateValue(id, start, end, duration) {
    if (start === end) return;
    var range = end - start;
    var current = start;
    var increment = end > start ? 1 : -1;
    var stepTime = Math.abs(Math.floor(duration / range));

    if (stepTime < 10) {
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
        obj.innerHTML = current;
    }, stepTime);
}

document.getElementById('bmiForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const weight = parseFloat(document.getElementById('weight').value);
    const heightCm = parseFloat(document.getElementById('height').value);

    const isValidWeight = Number.isFinite(weight) && weight >= 20 && weight <= 300;
    const isValidHeight = Number.isFinite(heightCm) && heightCm >= 100 && heightCm <= 250;

    if (!isValidWeight || !isValidHeight) {
        alert('Please enter a valid weight (20-300 kg) and height (100-250 cm).');
        return;
    }

    if (isValidWeight && isValidHeight) {
        const heightM = heightCm / 100;
        const bmi = weight / (heightM * heightM);

        let category = '';
        let explanation = '';

        if (bmi < 18.5) {
            category = 'Underweight';
            explanation = 'You are below the healthy weight range. Consider consulting a nutritionist for a bulking plan.';
        } else if (bmi < 25) {
            category = 'Normal';
            explanation = 'You are in a healthy weight range. Maintain your current training and nutrition regimen.';
        } else if (bmi < 30) {
            category = 'Overweight';
            explanation = 'You are slightly above the healthy weight range. Consider a slight caloric deficit or increased cardio.';
        } else {
            category = 'Obesity';
            explanation = 'You are in the obesity range. Consult with a fitness professional to develop a comprehensive fat-loss plan.';
        }

        document.getElementById('bmiValue').textContent = bmi.toFixed(1);
        document.getElementById('bmiCategory').textContent = category;
        document.getElementById('bmiExplanation').textContent = explanation;

        document.getElementById('emptyState').classList.add('hidden');
        document.getElementById('resultContent').classList.remove('hidden');
    }
});

USE apexion_db;

INSERT INTO exercise_muscle_groups (exercise_id, muscle_group_id)
SELECT e.id, m.id
FROM exercises e
JOIN muscle_groups m ON
    (e.slug = 'battle-ropes' AND m.name IN ('Front Delts', 'Lateral Delts', 'Lats')) OR
    (e.slug = 'behind-the-neck-press' AND m.name IN ('Front Delts', 'Lateral Delts')) OR
    (e.slug = 'bench-leg-pull-in' AND m.name = 'Abs') OR
    (e.slug = 'cossack-squat' AND m.name IN ('Glutes', 'Quads')) OR
    (e.slug = 'dumbbell-reverse-fly' AND m.name = 'Rear Delts') OR
    (e.slug = 'ez-bar-spider-curl' AND m.name = 'Biceps') OR
    (e.slug = 'front-squat' AND m.name IN ('Glutes', 'Quads')) OR
    (e.slug = 'incline-db-curl' AND m.name = 'Biceps') OR
    (e.slug = 'mountain-climbers' AND m.name IN ('Hip Flexors', 'Abs')) OR
    (e.slug = 'one-arm-kettlebell-row' AND m.name IN ('Lats', 'Trapezius')) OR
    (e.slug = 'plate-loaded-lateral-raise' AND m.name = 'Lateral Delts') OR
    (e.slug = 'pull-up' AND m.name = 'Lats') OR
    (e.slug = 'reverse-grip-lat-pulldown' AND m.name IN ('Biceps', 'Lats')) OR
    (e.slug = 'side-lying-lateral-raise' AND m.name = 'Lateral Delts') OR
    (e.slug = 'smith-machine-front-squat' AND m.name IN ('Glutes', 'Quads')) OR
    (e.slug = 'stability-ball-knee-tuck' AND m.name = 'Abs');

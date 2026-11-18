CREATE DATABASE IF NOT EXISTS apexion_db;
USE apexion_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    gender VARCHAR(50) DEFAULT 'MALE',
    birth_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE fitness_profiles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    height_cm DECIMAL(5, 2),
    weight_kg DECIMAL(5, 2),
    fitness_goal VARCHAR(50) NOT NULL DEFAULT 'GAIN_MUSCLE',
    activity_level VARCHAR(50) NOT NULL DEFAULT 'MODERATELY_ACTIVE',
    CONSTRAINT fk_fitness_profile_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE exercises (
    id INT AUTO_INCREMENT PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    instructions TEXT,
    equipment VARCHAR(50),
    difficulty VARCHAR(20),
    animation_path VARCHAR(255),
    thumbnail_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE muscle_groups (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    parent_id INT DEFAULT NULL,
    CONSTRAINT fk_muscle_group_parent
        FOREIGN KEY (parent_id) REFERENCES muscle_groups (id) ON DELETE CASCADE
);

CREATE TABLE exercise_muscle_groups (
    exercise_id INT NOT NULL,
    muscle_group_id INT NOT NULL,
    PRIMARY KEY (exercise_id, muscle_group_id),
    FOREIGN KEY (exercise_id) REFERENCES exercises (id) ON DELETE CASCADE,
    FOREIGN KEY (muscle_group_id) REFERENCES muscle_groups (id) ON DELETE CASCADE
);

CREATE TABLE favorite_exercises (
    user_id INT NOT NULL,
    exercise_id INT NOT NULL,
    PRIMARY KEY (user_id, exercise_id),
    CONSTRAINT fk_favorite_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_exercise
        FOREIGN KEY (exercise_id) REFERENCES exercises (id) ON DELETE CASCADE
);

CREATE TABLE workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE workout_exercises (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    exercise_id INT NOT NULL,
    sets INT DEFAULT 3,
    reps INT DEFAULT 10,
    weight DOUBLE DEFAULT 0,
    sort_order INT NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES workouts (id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercises (id) ON DELETE CASCADE
);

CREATE TABLE muscle_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    muscle_name VARCHAR(50) UNIQUE NOT NULL,
    overview TEXT NOT NULL,
    anatomy TEXT NOT NULL,
    muscle_function TEXT NOT NULL,
    example_exercises VARCHAR(255) NOT NULL,
    synergist_muscles VARCHAR(255) NOT NULL,
    recovery_window VARCHAR(50) NOT NULL
);

package com.hbpnrl.apexion.validation;

import com.hbpnrl.apexion.dto.request.AuthenticationRequest;
import com.hbpnrl.apexion.dto.request.WorkoutExerciseRequest;
import com.hbpnrl.apexion.dto.request.WorkoutRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void validWorkoutRequestShouldHaveNoViolations() {
        WorkoutRequest request = validWorkoutRequest();

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void workoutRequestShouldRejectBlankNameAndEmptyExercises() {
        WorkoutRequest request = new WorkoutRequest();
        request.setName("   ");
        request.setExercises(List.of());

        assertEquals(2, validator.validate(request).size());
    }

    @Test
    void workoutRequestShouldValidateNestedExercises() {
        WorkoutExerciseRequest exercise = new WorkoutExerciseRequest();
        exercise.setExerciseId(-1);
        exercise.setSortOrder(-1);
        exercise.setSets(0);
        exercise.setReps(101);
        exercise.setWeight(-1.0);

        WorkoutRequest request = new WorkoutRequest();
        request.setName("Invalid workout");
        request.setExercises(List.of(exercise));

        assertFalse(validator.validate(request).isEmpty());
        assertTrue(validator.validate(request).size() >= 5);
    }

    @Test
    void workoutRequestShouldRejectMoreThanFiftyExercises() {
        List<WorkoutExerciseRequest> exercises = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            exercises.add(validExercise());
        }

        WorkoutRequest request = new WorkoutRequest();
        request.setName("Too many exercises");
        request.setExercises(exercises);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void loginRequestShouldRejectBlankAndInvalidEmail() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("not-an-email");
        request.setPassword("   ");

        assertEquals(2, validator.validate(request).size());
    }

    private static WorkoutRequest validWorkoutRequest() {
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Push day");
        request.setExercises(List.of(validExercise()));
        return request;
    }

    private static WorkoutExerciseRequest validExercise() {
        WorkoutExerciseRequest exercise = new WorkoutExerciseRequest();
        exercise.setExerciseId(1);
        exercise.setSortOrder(0);
        exercise.setSets(3);
        exercise.setReps(10);
        exercise.setWeight(40.0);
        return exercise;
    }
}

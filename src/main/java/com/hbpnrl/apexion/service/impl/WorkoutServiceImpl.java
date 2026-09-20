package com.hbpnrl.apexion.service.impl;

import com.hbpnrl.apexion.dto.request.WorkoutRequest;
import com.hbpnrl.apexion.dto.request.WorkoutExerciseRequest;
import com.hbpnrl.apexion.dto.response.WorkoutExerciseResponse;
import com.hbpnrl.apexion.dto.response.WorkoutResponse;
import com.hbpnrl.apexion.entity.Exercise;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.entity.Workout;
import com.hbpnrl.apexion.entity.WorkoutExercise;
import com.hbpnrl.apexion.repository.ExerciseRepository;
import com.hbpnrl.apexion.repository.UserRepository;
import com.hbpnrl.apexion.repository.WorkoutRepository;
import com.hbpnrl.apexion.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Override
    public List<WorkoutResponse> getUserWorkouts(Integer userId) {
        List<Workout> workouts = workoutRepository.findAllByUserId(userId);
        return workouts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteWorkout(Integer workoutId, Integer userId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found with ID: " + workoutId));

        if (!workout.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own workouts.");
        }

        workoutRepository.delete(workout);
    }

    @Override
    @Transactional
    public void createWorkout(WorkoutRequest request, Integer authenticatedUserId) {

        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Workout workout = new Workout();
        workout.setUser(user);
        workout.setName(request.getName());

        List<WorkoutExercise> exercises = request.getExercises().stream()
                .map(exRequest -> mapToWorkoutExerciseEntity(exRequest, workout))
                .collect(Collectors.toList());

        workout.getWorkoutExercises().addAll(exercises);
        workoutRepository.save(workout);
    }

    private WorkoutExercise mapToWorkoutExerciseEntity(WorkoutExerciseRequest exRequest, Workout workout) {
        Exercise exercise = exerciseRepository.findById(exRequest.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found! ID: " + exRequest.getExerciseId()));

        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
        workoutExercise.setSets(exRequest.getSets());
        workoutExercise.setReps(exRequest.getReps());
        workoutExercise.setWeight(exRequest.getWeight());
        workoutExercise.setSortOrder(exRequest.getSortOrder());

        return workoutExercise;
    }

    private WorkoutResponse mapToResponse(Workout workout) {
        List<WorkoutExerciseResponse> exerciseResponses = workout.getWorkoutExercises().stream()
                .map(we -> WorkoutExerciseResponse.builder()
                        .id(we.getId())
                        .sets(we.getSets())
                        .reps(we.getReps())
                        .weight(we.getWeight())
                        .sortOrder(we.getSortOrder())
                        .exerciseName(we.getExercise().getName())
                        .build())
                .collect(Collectors.toList());

        return WorkoutResponse.builder()
                .id(workout.getId())
                .name(workout.getName())
                .description("Custom User Workout")
                .estimatedDurationMinutes(45)
                .createdAt(workout.getCreatedAt())
                .workoutExercises(exerciseResponses)
                .build();
    }
}
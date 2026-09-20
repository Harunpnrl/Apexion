package com.hbpnrl.apexion.service.impl;

import com.hbpnrl.apexion.dto.response.ExerciseResponse;
import com.hbpnrl.apexion.entity.Exercise;
import com.hbpnrl.apexion.entity.MuscleGroup;
import com.hbpnrl.apexion.repository.ExerciseRepository;
import com.hbpnrl.apexion.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Override
    public List<ExerciseResponse> getAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseResponse getExerciseById(Integer id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));

        return mapToResponse(exercise);
    }

    @Override
    public List<ExerciseResponse> getRandomExercises(int limit) {
        return exerciseRepository.findRandomExercises(limit)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ExerciseResponse mapToResponse(Exercise exercise) {
        List<String> muscleGroupNames = exercise.getMuscleGroups().stream()
                .map(MuscleGroup::getName)
                .collect(Collectors.toList());

        return ExerciseResponse.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .instructions(exercise.getInstructions())
                .difficulty(exercise.getDifficulty() != null ? exercise.getDifficulty().name() : null)
                .equipment(exercise.getEquipment())
                .animationPath(exercise.getAnimationPath())
                .thumbnailPath(exercise.getThumbnailPath())
                .muscleGroups(muscleGroupNames)
                .build();
    }
}
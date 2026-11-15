package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.entity.Exercise;
import com.hbpnrl.apexion.entity.MuscleGroup;
import com.hbpnrl.apexion.repository.ExerciseRepository;
import com.hbpnrl.apexion.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @Test
    void getAllExercisesShouldMapExerciseData() {
        Exercise exercise = exercise(1);
        when(exerciseRepository.findAll()).thenReturn(List.of(exercise));

        var result = exerciseService.getAllExercises();

        assertEquals(1, result.size());
        assertEquals("Bench Press", result.get(0).getName());
        assertEquals(List.of("Chest"), result.get(0).getMuscleGroups());
    }

    @Test
    void getExerciseByIdShouldReturnExercise() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise(1)));

        assertEquals("Bench Press", exerciseService.getExerciseById(1).getName());
    }

    @Test
    void getExerciseByIdShouldThrowWhenMissing() {
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.getExerciseById(99));
    }

    @Test
    void getRandomExercisesShouldUseRequestedLimit() {
        when(exerciseRepository.findRandomExercises(3)).thenReturn(List.of(exercise(1)));

        assertEquals(1, exerciseService.getRandomExercises(3).size());
    }

    private Exercise exercise(int id) {
        MuscleGroup group = new MuscleGroup();
        group.setName("Chest");
        Exercise exercise = new Exercise();
        exercise.setId(id);
        exercise.setName("Bench Press");
        exercise.setDescription("Press the bar");
        exercise.setMuscleGroups(Set.of(group));
        return exercise;
    }
}

package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.request.WorkoutExerciseRequest;
import com.hbpnrl.apexion.dto.request.WorkoutRequest;
import com.hbpnrl.apexion.entity.Exercise;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.entity.Workout;
import com.hbpnrl.apexion.repository.ExerciseRepository;
import com.hbpnrl.apexion.repository.UserRepository;
import com.hbpnrl.apexion.repository.WorkoutRepository;
import com.hbpnrl.apexion.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    private User user;
    private Exercise firstExercise;
    private Exercise secondExercise;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(7);

        firstExercise = new Exercise();
        firstExercise.setId(1);
        firstExercise.setName("Bench Press");

        secondExercise = new Exercise();
        secondExercise.setId(2);
        secondExercise.setName("Shoulder Press");
    }

    @Test
    void createWorkoutShouldUseAuthenticatedUserAndServerOwnedOrder() {
        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(firstExercise));
        when(exerciseRepository.findById(2)).thenReturn(Optional.of(secondExercise));

        WorkoutRequest request = new WorkoutRequest();
        request.setName("Push day");
        request.setExercises(List.of(
                exerciseRequest(1, 49),
                exerciseRequest(2, 0)
        ));

        workoutService.createWorkout(request, 7);

        ArgumentCaptor<Workout> workoutCaptor = ArgumentCaptor.forClass(Workout.class);
        verify(workoutRepository).save(workoutCaptor.capture());

        Workout savedWorkout = workoutCaptor.getValue();
        assertEquals(user, savedWorkout.getUser());
        assertEquals("Push day", savedWorkout.getName());
        assertEquals(2, savedWorkout.getWorkoutExercises().size());
        assertEquals(0, savedWorkout.getWorkoutExercises().get(0).getSortOrder());
        assertEquals(1, savedWorkout.getWorkoutExercises().get(1).getSortOrder());
    }

    @Test
    void createWorkoutShouldRejectUnknownExercise() {
        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        when(exerciseRepository.findById(999)).thenReturn(Optional.empty());

        WorkoutRequest request = new WorkoutRequest();
        request.setName("Invalid workout");
        request.setExercises(List.of(exerciseRequest(999, 0)));

        assertThrows(RuntimeException.class, () -> workoutService.createWorkout(request, 7));
        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void deleteWorkoutShouldAllowOnlyTheOwner() {
        Workout workout = new Workout();
        workout.setId(10);
        workout.setUser(user);
        when(workoutRepository.findById(10)).thenReturn(Optional.of(workout));

        workoutService.deleteWorkout(10, 7);

        verify(workoutRepository).delete(workout);
    }

    @Test
    void deleteWorkoutShouldRejectAnotherUsersWorkout() {
        Workout workout = new Workout();
        workout.setId(10);
        User anotherUser = new User();
        anotherUser.setId(8);
        workout.setUser(anotherUser);
        when(workoutRepository.findById(10)).thenReturn(Optional.of(workout));

        assertThrows(RuntimeException.class, () -> workoutService.deleteWorkout(10, 7));
        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    private WorkoutExerciseRequest exerciseRequest(int exerciseId, int sortOrder) {
        WorkoutExerciseRequest request = new WorkoutExerciseRequest();
        request.setExerciseId(exerciseId);
        request.setSortOrder(sortOrder);
        request.setSets(3);
        request.setReps(10);
        request.setWeight(40.0);
        return request;
    }
}

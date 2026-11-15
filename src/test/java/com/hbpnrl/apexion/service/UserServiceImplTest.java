package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.request.UserUpdateRequest;
import com.hbpnrl.apexion.entity.Exercise;
import com.hbpnrl.apexion.entity.MuscleGroup;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.repository.ExerciseRepository;
import com.hbpnrl.apexion.repository.UserRepository;
import com.hbpnrl.apexion.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserProfileShouldMapUserData() {
        User user = user();

        var response = userService.getUserProfile(user);

        assertEquals(7, response.getId());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("MALE", response.getGender());
    }

    @Test
    void updateUserProfileShouldSaveNewValues() {
        User user = user();
        UserUpdateRequest request = UserUpdateRequest.builder()
                .firstName("Jane")
                .lastName("Updated")
                .gender(User.Gender.FEMALE)
                .build();
        when(userRepository.save(user)).thenReturn(user);

        var response = userService.updateUserProfile(user, request);

        verify(userRepository).save(user);
        assertEquals("Jane", response.getFirstName());
        assertEquals("FEMALE", response.getGender());
    }

    @Test
    void favoriteOperationsShouldUpdateUserFavorites() {
        User user = user();
        Exercise exercise = exercise();
        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        when(exerciseRepository.findById(3)).thenReturn(Optional.of(exercise));

        userService.addExerciseToFavorites(7, 3);
        assertEquals(1, user.getFavoriteExercises().size());

        userService.removeExerciseFromFavorites(7, 3);
        assertEquals(0, user.getFavoriteExercises().size());
        verify(userRepository, org.mockito.Mockito.times(2)).save(user);
    }

    @Test
    void getFavoritesShouldMapExerciseAndMuscleGroups() {
        User user = user();
        user.getFavoriteExercises().add(exercise());
        when(userRepository.findById(7)).thenReturn(Optional.of(user));

        var favorites = userService.getFavoriteExercises(7);

        assertEquals(1, favorites.size());
        assertEquals("Bench Press", favorites.get(0).getName());
        assertEquals("Chest", favorites.get(0).getMuscleGroups().get(0));
    }

    @Test
    void favoriteOperationsShouldRejectMissingRecords() {
        when(userRepository.findById(7)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.addExerciseToFavorites(7, 3));
        assertThrows(RuntimeException.class, () -> userService.getFavoriteExercises(7));
    }

    private User user() {
        User user = new User();
        user.setId(7);
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setGender(User.Gender.MALE);
        return user;
    }

    private Exercise exercise() {
        MuscleGroup group = new MuscleGroup();
        group.setName("Chest");
        Exercise exercise = new Exercise();
        exercise.setId(3);
        exercise.setName("Bench Press");
        exercise.setMuscleGroups(Set.of(group));
        return exercise;
    }
}

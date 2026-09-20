package com.hbpnrl.apexion.service.impl;

import com.hbpnrl.apexion.dto.request.UserUpdateRequest;
import com.hbpnrl.apexion.dto.response.UserResponse;
import com.hbpnrl.apexion.dto.response.ExerciseResponse;
import com.hbpnrl.apexion.entity.Exercise;
import com.hbpnrl.apexion.entity.MuscleGroup;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.repository.UserRepository;
import com.hbpnrl.apexion.repository.ExerciseRepository;
import com.hbpnrl.apexion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Override
    public UserResponse getUserProfile(User currentUser) {
        return UserResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .phoneNumber(currentUser.getPhoneNumber())
                .gender(currentUser.getGender() != null ? currentUser.getGender().name() : null)
                .birthDate(currentUser.getBirthDate())
                .createdAt(currentUser.getCreatedAt())
                .hasFitnessProfile(currentUser.getFitnessProfile() != null)
                .build();
    }

    @Override
    public UserResponse updateUserProfile(User currentUser, UserUpdateRequest request) {
        currentUser.setFirstName(request.getFirstName());
        currentUser.setLastName(request.getLastName());
        currentUser.setPhoneNumber(request.getPhoneNumber());
        currentUser.setGender(request.getGender());
        currentUser.setBirthDate(request.getBirthDate());

        User updatedUser = userRepository.save(currentUser);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .email(updatedUser.getEmail())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .phoneNumber(updatedUser.getPhoneNumber())
                .gender(updatedUser.getGender() != null ? updatedUser.getGender().name() : null)
                .birthDate(updatedUser.getBirthDate())
                .createdAt(updatedUser.getCreatedAt())
                .hasFitnessProfile(updatedUser.getFitnessProfile() != null)
                .build();
    }

    @Override
    public void addExerciseToFavorites(Integer userId, Integer exerciseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new RuntimeException("Exercise not found!"));

        user.getFavoriteExercises().add(exercise);
        userRepository.save(user);
    }

    @Override
    public void removeExerciseFromFavorites(Integer userId, Integer exerciseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new RuntimeException("Exercise not found!"));

        user.getFavoriteExercises().remove(exercise);
        userRepository.save(user);
    }

    @Override
    public List<ExerciseResponse> getFavoriteExercises(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        return user.getFavoriteExercises().stream()
                .map(exercise -> {
                    List<String> muscleGroupNames = exercise.getMuscleGroups().stream()
                            .map(MuscleGroup::getName)
                            .collect(Collectors.toList());

                    return ExerciseResponse.builder()
                            .id(exercise.getId())
                            .name(exercise.getName())
                            .description(exercise.getDescription())
                            .difficulty(exercise.getDifficulty() != null ? exercise.getDifficulty().name() : null)
                            .equipment(exercise.getEquipment())
                            .animationPath(exercise.getAnimationPath())
                            .thumbnailPath(exercise.getThumbnailPath())
                            .muscleGroups(muscleGroupNames)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
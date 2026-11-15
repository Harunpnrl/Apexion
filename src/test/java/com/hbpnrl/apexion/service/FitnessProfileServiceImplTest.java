package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.request.FitnessProfileRequest;
import com.hbpnrl.apexion.entity.FitnessProfile;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.repository.FitnessProfileRepository;
import com.hbpnrl.apexion.repository.UserRepository;
import com.hbpnrl.apexion.service.impl.FitnessProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FitnessProfileServiceImplTest {

    @Mock
    private FitnessProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FitnessProfileServiceImpl profileService;

    @Test
    void createOrUpdateProfileShouldSaveAndMapProfile() {
        User user = user(7);
        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(7)).thenReturn(Optional.empty());
        when(profileRepository.save(any(FitnessProfile.class))).thenAnswer(invocation -> {
            FitnessProfile profile = invocation.getArgument(0);
            profile.setId(3);
            return profile;
        });

        FitnessProfileService service = profileService;
        var result = service.createOrUpdateProfile(7, request());

        assertEquals(3, result.getId());
        assertEquals(BigDecimal.valueOf(180.0), result.getHeightCm());
        assertEquals("GAIN_MUSCLE", result.getFitnessGoal());
    }

    @Test
    void getProfileShouldMapExistingProfile() {
        User user = user(7);
        FitnessProfile profile = profile(user);
        when(profileRepository.findByUserId(7)).thenReturn(Optional.of(profile));

        assertEquals("MODERATELY_ACTIVE", profileService.getProfileByUserId(7).getActivityLevel());
    }

    @Test
    void missingUserOrProfileShouldThrow() {
        when(userRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> profileService.createOrUpdateProfile(7, request()));

        when(profileRepository.findByUserId(8)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> profileService.getProfileByUserId(8));
    }

    private FitnessProfileRequest request() {
        FitnessProfileRequest request = new FitnessProfileRequest();
        request.setHeightCm(180.0);
        request.setWeightKg(80.0);
        request.setFitnessGoal(FitnessProfile.FitnessGoal.GAIN_MUSCLE);
        request.setActivityLevel(FitnessProfile.ActivityLevel.MODERATELY_ACTIVE);
        return request;
    }

    private User user(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private FitnessProfile profile(User user) {
        FitnessProfile profile = new FitnessProfile();
        profile.setId(3);
        profile.setUser(user);
        profile.setHeightCm(BigDecimal.valueOf(180));
        profile.setWeightKg(BigDecimal.valueOf(80));
        profile.setFitnessGoal(FitnessProfile.FitnessGoal.GAIN_MUSCLE);
        profile.setActivityLevel(FitnessProfile.ActivityLevel.MODERATELY_ACTIVE);
        return profile;
    }
}

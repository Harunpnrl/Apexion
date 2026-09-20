package com.hbpnrl.apexion.service.impl;

import com.hbpnrl.apexion.dto.response.MuscleInfoResponse;
import com.hbpnrl.apexion.entity.MuscleInfo;
import com.hbpnrl.apexion.repository.MuscleInfoRepository;
import com.hbpnrl.apexion.service.MuscleInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MuscleInfoServiceImpl implements MuscleInfoService {

    private final MuscleInfoRepository muscleInfoRepository;

    @Override
    public MuscleInfoResponse getMuscleInfoByName(String muscleName) {

        MuscleInfo muscleInfo = muscleInfoRepository.findByMuscleNameIgnoreCase(muscleName)
                .orElseThrow(() -> new RuntimeException("Muscle Data Could Not Found: " + muscleName));

        return MuscleInfoResponse.builder()
                .id(muscleInfo.getId())
                .muscleName(muscleInfo.getMuscleName())
                .overview(muscleInfo.getOverview())
                .anatomy(muscleInfo.getAnatomy())
                .muscleFunction(muscleInfo.getMuscleFunction())
                .exampleExercises(muscleInfo.getExampleExercises())
                .synergistMuscles(muscleInfo.getSynergistMuscles())
                .recoveryWindow(muscleInfo.getRecoveryWindow())
                .build();
    }
}
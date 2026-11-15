package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.entity.MuscleGroup;
import com.hbpnrl.apexion.entity.MuscleInfo;
import com.hbpnrl.apexion.repository.MuscleGroupRepository;
import com.hbpnrl.apexion.repository.MuscleInfoRepository;
import com.hbpnrl.apexion.service.impl.MuscleGroupServiceImpl;
import com.hbpnrl.apexion.service.impl.MuscleInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MuscleServiceImplTest {

    @Mock
    private MuscleGroupRepository muscleGroupRepository;

    @Mock
    private MuscleInfoRepository muscleInfoRepository;

    @InjectMocks
    private MuscleGroupServiceImpl muscleGroupService;

    @InjectMocks
    private MuscleInfoServiceImpl muscleInfoService;

    @Test
    void muscleGroupServiceShouldMapParentInformation() {
        MuscleGroup parent = new MuscleGroup();
        parent.setId(1);
        parent.setName("Upper Body");
        MuscleGroup child = new MuscleGroup();
        child.setId(2);
        child.setName("Chest");
        child.setParentGroup(parent);
        when(muscleGroupRepository.findAll()).thenReturn(List.of(child));

        var result = muscleGroupService.getAllMuscleGroups();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getParentId());
        assertEquals("Upper Body", result.get(0).getParentName());
    }

    @Test
    void primaryMuscleGroupsShouldUsePrimaryRepositoryQuery() {
        when(muscleGroupRepository.findByParentGroupIsNull()).thenReturn(List.of(new MuscleGroup()));

        assertEquals(1, muscleGroupService.getPrimaryMuscleGroups().size());
    }

    @Test
    void muscleInfoServiceShouldMapDetails() {
        MuscleInfo info = new MuscleInfo();
        info.setId(1);
        info.setMuscleName("Chest");
        info.setOverview("Overview");
        info.setAnatomy("Anatomy");
        info.setMuscleFunction("Function");
        info.setExampleExercises("Bench Press");
        info.setSynergistMuscles("Triceps");
        info.setRecoveryWindow("48 Hours");
        when(muscleInfoRepository.findByMuscleNameIgnoreCase("chest")).thenReturn(Optional.of(info));

        assertEquals("Chest", muscleInfoService.getMuscleInfoByName("chest").getMuscleName());
    }

    @Test
    void missingMuscleInfoShouldThrow() {
        when(muscleInfoRepository.findByMuscleNameIgnoreCase("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> muscleInfoService.getMuscleInfoByName("unknown"));
    }
}

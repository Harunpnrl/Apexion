package com.hbpnrl.apexion.service.impl;

import com.hbpnrl.apexion.dto.response.MuscleGroupResponse;
import com.hbpnrl.apexion.entity.MuscleGroup;
import com.hbpnrl.apexion.repository.MuscleGroupRepository;
import com.hbpnrl.apexion.service.MuscleGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MuscleGroupServiceImpl implements MuscleGroupService {

    private final MuscleGroupRepository muscleGroupRepository;

    @Override
    public List<MuscleGroupResponse> getAllMuscleGroups() {
        return muscleGroupRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MuscleGroupResponse> getPrimaryMuscleGroups() {
        return muscleGroupRepository.findByParentGroupIsNull()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MuscleGroupResponse mapToResponse(MuscleGroup muscleGroup) {
        return MuscleGroupResponse.builder()
                .id(muscleGroup.getId())
                .name(muscleGroup.getName())
                .parentId(muscleGroup.getParentGroup() != null ? muscleGroup.getParentGroup().getId() : null)
                .parentName(muscleGroup.getParentGroup() != null ? muscleGroup.getParentGroup().getName() : null)
                .build();
    }
}
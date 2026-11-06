package com.hbpnrl.apexion.controller;

import com.hbpnrl.apexion.dto.response.MuscleGroupResponse;
import com.hbpnrl.apexion.service.MuscleGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/muscle-groups")
@RequiredArgsConstructor
public class MuscleGroupController {

    private final MuscleGroupService muscleGroupService;

    // GET http://localhost:8080/api/v1/muscle-groups
    @GetMapping
    public ResponseEntity<List<MuscleGroupResponse>> getAllMuscleGroups() {
        return ResponseEntity.ok(muscleGroupService.getAllMuscleGroups());
    }

    // GET http://localhost:8080/api/v1/muscle-groups/primary
    @GetMapping("/primary")
    public ResponseEntity<List<MuscleGroupResponse>> getPrimaryMuscleGroups() {
        return ResponseEntity.ok(muscleGroupService.getPrimaryMuscleGroups());
    }
}

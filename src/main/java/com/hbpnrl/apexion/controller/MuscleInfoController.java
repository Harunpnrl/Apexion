package com.hbpnrl.apexion.controller;

import com.hbpnrl.apexion.dto.response.MuscleInfoResponse;
import com.hbpnrl.apexion.service.MuscleInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/muscle-info")
@RequiredArgsConstructor
public class MuscleInfoController {

    private final MuscleInfoService muscleInfoService;

    // GET http://localhost:8080/api/v1/muscle-info/{muscleName}
    @GetMapping("/{muscleName}")
    public ResponseEntity<MuscleInfoResponse> getMuscleInfo(@PathVariable String muscleName) {
        return ResponseEntity.ok(muscleInfoService.getMuscleInfoByName(muscleName));
    }
}

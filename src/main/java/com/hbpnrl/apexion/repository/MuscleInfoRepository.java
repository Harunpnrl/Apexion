package com.hbpnrl.apexion.repository;

import com.hbpnrl.apexion.entity.MuscleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MuscleInfoRepository extends JpaRepository<MuscleInfo, Integer> {
    Optional<MuscleInfo> findByMuscleNameIgnoreCase(String muscleName);
}
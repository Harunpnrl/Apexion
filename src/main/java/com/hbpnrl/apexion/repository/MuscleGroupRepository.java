package com.hbpnrl.apexion.repository;

import com.hbpnrl.apexion.entity.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Integer> {

    List<MuscleGroup> findByParentGroupIsNull();
}
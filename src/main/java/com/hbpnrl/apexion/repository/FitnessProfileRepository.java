package com.hbpnrl.apexion.repository;

import com.hbpnrl.apexion.entity.FitnessProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FitnessProfileRepository extends JpaRepository<FitnessProfile, Integer> {

    Optional<FitnessProfile> findByUserId(Integer userId);
}
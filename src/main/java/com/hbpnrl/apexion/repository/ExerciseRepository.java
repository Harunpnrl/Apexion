package com.hbpnrl.apexion.repository;

import com.hbpnrl.apexion.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    @Query(value = "SELECT * FROM exercises ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Exercise> findRandomExercises(@Param("limit") int limit);
}
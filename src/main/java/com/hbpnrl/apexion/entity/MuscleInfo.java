package com.hbpnrl.apexion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "muscle_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuscleInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "muscle_name", unique = true, nullable = false, length = 50)
    private String muscleName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String overview;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String anatomy;

    @Column(name = "muscle_function", columnDefinition = "TEXT", nullable = false)
    private String muscleFunction;

    @Column(name = "example_exercises", nullable = false)
    private String exampleExercises;

    @Column(name = "synergist_muscles", nullable = false)
    private String synergistMuscles;

    @Column(name = "recovery_window", nullable = false, length = 50)
    private String recoveryWindow;
}
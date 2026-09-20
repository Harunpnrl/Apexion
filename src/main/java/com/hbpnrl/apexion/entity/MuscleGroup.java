package com.hbpnrl.apexion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "muscle_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuscleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MuscleGroup parentGroup;

    @OneToMany(mappedBy = "parentGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<MuscleGroup> subGroups = new HashSet<>();

    @ManyToMany(mappedBy = "muscleGroups")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Exercise> exercises = new HashSet<>();

}
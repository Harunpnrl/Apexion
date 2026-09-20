package com.hbpnrl.apexion.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(length = 50)
    private String equipment;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Difficulty difficulty;

    @Column(name = "animation_path")
    private String animationPath;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "exercise_muscle_groups",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "muscle_group_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<MuscleGroup> muscleGroups = new HashSet<>();

    @ManyToMany(mappedBy = "favoriteExercises")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> favoritedByUsers = new HashSet<>();

    public enum Difficulty {
        Beginner,
        Intermediate,
        Advanced
    }
}
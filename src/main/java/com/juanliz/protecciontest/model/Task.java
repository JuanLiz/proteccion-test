package com.juanliz.protecciontest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;
    private LocalDateTime dueDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> assignedUsers = new HashSet<>();

}

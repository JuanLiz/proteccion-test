package com.juanliz.protecciontest.dto;

import com.juanliz.protecciontest.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.juanliz.protecciontest.model.Task}
 */
@Value
public class TaskCreateDto implements Serializable {
    @NotNull
    @NotBlank
    String name;
    String description;
    @NotNull
    TaskStatus status;
    LocalDate dueDate;
}
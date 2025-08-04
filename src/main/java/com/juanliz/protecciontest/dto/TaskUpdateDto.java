package com.juanliz.protecciontest.dto;

import com.juanliz.protecciontest.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.juanliz.protecciontest.model.Task}
 */
@Value
public class TaskUpdateDto implements Serializable {
    @NotNull
    int id;
    String name;
    String description;
    TaskStatus status;
    LocalDate dueDate;
}
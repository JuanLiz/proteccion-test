package com.juanliz.protecciontest.controller;

import com.juanliz.protecciontest.dto.TaskCreateDto;
import com.juanliz.protecciontest.dto.TaskUpdateDto;
import com.juanliz.protecciontest.model.Task;
import com.juanliz.protecciontest.model.TaskStatus;
import com.juanliz.protecciontest.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tareas",
        description = "Operaciones para la gestión de tareas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las tareas",
            description = "Devuelve una lista paginada de todas las tareas")
    public ResponseEntity<Page<Task>> getAllTasks(
            @Parameter(description = "Nombre de la tarea")
            @RequestParam(required = false) String name,
            @Parameter(description = "Estado de la tarea (PENDING, IN_PROGRESS, COMPLETED)")
            @RequestParam(required = false) String status,
            @Parameter(description = "ID del usuario asignado a la tarea")
            @RequestParam(required = false) String userId,
            @ParameterObject Pageable pageable
    ) {
        if (name != null) {
            return ResponseEntity.ok(taskService.getTasksByName(name, pageable));
        } else if (status != null) {
            return ResponseEntity.ok(taskService.getTasksByStatus(TaskStatus.valueOf(status), pageable));
        } else {
            return ResponseEntity.ok(taskService.getAllTasks(pageable));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarea por ID",
            description = "Devuelve una tarea específica por su ID")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "ID de la tarea a buscar")
            @PathVariable int id
    ) {
        Task task = taskService.getTaskById(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear nueva tarea",
            description = "Crea una nueva tarea")
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid TaskCreateDto task,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Task createdTask = taskService.createTask(task);
        URI url = uriComponentsBuilder.path(
                "/tasks/{id}"
        ).buildAndExpand(createdTask.getId()).toUri();
        return ResponseEntity.created(url).body(createdTask);

    }

    @PutMapping
    @Operation(summary = "Actualizar tarea",
            description = "Actualiza una tarea existente")
    public ResponseEntity<Task> updateTask(
            @RequestBody @Valid TaskUpdateDto task
    ) {
        Task updatedTask = taskService.updateTask(task);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tarea",
            description = "Elimina una tarea por su ID")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

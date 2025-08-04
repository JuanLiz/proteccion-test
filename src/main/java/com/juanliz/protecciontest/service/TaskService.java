package com.juanliz.protecciontest.service;

import com.juanliz.protecciontest.dto.TaskCreateDto;
import com.juanliz.protecciontest.dto.TaskMapper;
import com.juanliz.protecciontest.dto.TaskUpdateDto;
import com.juanliz.protecciontest.model.Task;
import com.juanliz.protecciontest.model.TaskStatus;
import com.juanliz.protecciontest.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task getTaskById(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Page<Task> getTasksByName(String name, Pageable pageable) {
        return taskRepository.findAllByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Task> getTasksByStatus(TaskStatus status, Pageable pageable) {
        return taskRepository.findAllByStatus(status, pageable);
    }

    public Task createTask(TaskCreateDto task) {
        return taskRepository.save(taskMapper.toEntity(task));
    }

    public Task updateTask(TaskUpdateDto task) {
        Task existingTask = taskRepository.findById(task.getId()).orElse(null);
        if (existingTask == null) return null;
        taskMapper.partialUpdate(task, existingTask);
        return taskRepository.save(existingTask);
    }

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }


}

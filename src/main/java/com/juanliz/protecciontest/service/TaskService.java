package com.juanliz.protecciontest.service;

import com.juanliz.protecciontest.dto.TaskCreateDto;
import com.juanliz.protecciontest.dto.TaskMapper;
import com.juanliz.protecciontest.dto.TaskUpdateDto;
import com.juanliz.protecciontest.model.Task;
import com.juanliz.protecciontest.model.TaskStatus;
import com.juanliz.protecciontest.model.User;
import com.juanliz.protecciontest.repository.TaskRepository;
import com.juanliz.protecciontest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }


    public Page<Task> getTasksForCurrentUser(String name, TaskStatus status, Pageable pageable) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (name != null && name.isBlank()) {
            name = null;
        }
        return taskRepository.findUserTasks(currentUser.getUsername(), currentUser, name, status, pageable);
    }

    public Task getTaskById(int id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        checkPermissionForTask(task);
        return task;
    }

    public Page<Task> getTasksByUserId(int userId, Pageable pageable) {
        return taskRepository.findAllByAssignedUsersId(userId, pageable);
    }

    public Task createTask(TaskCreateDto task) {
        return taskRepository.save(taskMapper.toEntity(task));
    }

    public Task updateTask(TaskUpdateDto taskDto) {
        Task existingTask = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskDto.getId()));
        checkPermissionForTask(existingTask);
        taskMapper.partialUpdate(taskDto, existingTask);
        return taskRepository.save(existingTask);
    }

    public void deleteTask(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        checkPermissionForTask(task);
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task assignUsersToTask(Integer taskId, List<Integer> userIds) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        checkPermissionForTask(task);

        List<User> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new EntityNotFoundException("One or more users not found.");
        }

        task.getAssignedUsers().clear();
        task.getAssignedUsers().addAll(users);

        return taskRepository.save(task);
    }

    @Transactional
    public Task removeUsersFromTask(Integer taskId, List<Integer> userIds) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        checkPermissionForTask(task);

        List<User> usersToRemove = userRepository.findAllById(userIds);
        usersToRemove.forEach(task.getAssignedUsers()::remove);

        return taskRepository.save(task);
    }


    private void checkPermissionForTask(Task task) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        boolean isCreator = task.getCreatedBy().equals(currentUser.getUsername());
        boolean isAssignee = task.getAssignedUsers().stream().anyMatch(user -> user.getId() == currentUser.getId());

        if (!isCreator && !isAssignee) {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }


}

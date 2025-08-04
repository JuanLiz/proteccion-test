package com.juanliz.protecciontest.repository;

import com.juanliz.protecciontest.model.Task;
import com.juanliz.protecciontest.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Page<Task> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Task> findAllByStatus(TaskStatus status, Pageable pageable);
}

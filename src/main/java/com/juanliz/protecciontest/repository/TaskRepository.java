package com.juanliz.protecciontest.repository;

import com.juanliz.protecciontest.model.Task;
import com.juanliz.protecciontest.model.TaskStatus;
import com.juanliz.protecciontest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t WHERE " +
            "(:name IS NULL OR lower(t.name) LIKE lower(concat('%', CAST(:name AS string), '%'))) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(t.createdBy = :username OR :user MEMBER OF t.assignedUsers)")
    Page<Task> findUserTasks(
            @Param("username") String username,
            @Param("user") User user,
            @Param("name") String name,
            @Param("status") TaskStatus status,
            Pageable pageable
    );

    Page<Task> findAllByAssignedUsersId(int userId, Pageable pageable);
}

package com.task.rest.taskmanagerrest.repository;

import com.task.rest.taskmanagerrest.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
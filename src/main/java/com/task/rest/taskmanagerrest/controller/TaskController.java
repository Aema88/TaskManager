package com.task.rest.taskmanagerrest.controller;

import com.task.rest.exception.InvalidStatusException;
import com.task.rest.taskmanagerrest.dto.TaskDto;
import com.task.rest.taskmanagerrest.model.TaskStatus;
import com.task.rest.taskmanagerrest.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.task.rest.taskmanagerrest.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto) {
        try {
            TaskStatus.valueOf(taskDto.getStatus().name().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException("Invalid status: " + taskDto.getStatus());
        }
        Task newTask = new Task(
                taskDto.getTitle(),
                taskDto.getDescription().orElse(null),
                taskDto.getDeadline().orElse(null),
                taskDto.getStatus()
        );
        taskService.createTask(newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        Optional<Task> taskOptional = taskService.getTaskById(id);

        if (taskOptional.isPresent()) {
            Task taskToUpdate = taskOptional.get();
            taskToUpdate.setTitle(taskDto.getTitle());
            taskToUpdate.setDescription(taskDto.getDescription().orElse(null));
            taskToUpdate.setDeadline(taskDto.getDeadline().orElse(null));
            taskToUpdate.setStatus(taskDto.getStatus());
            Task updatedTask = taskService.createTask(taskToUpdate);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

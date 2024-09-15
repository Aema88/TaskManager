package com.task.rest.taskmanagerrest.service;
import com.task.rest.taskmanagerrest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.task.rest.taskmanagerrest.model.Task;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }

}

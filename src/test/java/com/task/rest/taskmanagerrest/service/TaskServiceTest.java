package com.task.rest.taskmanagerrest.service;

import com.task.rest.taskmanagerrest.model.Task;
import com.task.rest.taskmanagerrest.model.TaskStatus;
import com.task.rest.taskmanagerrest.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.task.rest.taskmanagerrest.service.TaskServiceTest.TestResources.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_ShouldSaveTask() {
        Task task = buildTask();
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getId()).isEqualTo(taskId);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        Task task = buildTask();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.getTaskById(taskId);

        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getId()).isEqualTo(taskId);
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getAllTasks_ShouldReturnTaskList() {
        Task task = buildTask();
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> foundTasks = taskService.getAllTasks();

        assertThat(foundTasks).hasSize(1);
        assertThat(foundTasks).contains(task);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }

    static class TestResources {
        static final UUID taskId = UUID.fromString("929901aa-dabf-4ea3-b488-ffd9d89c2b6c");
        static final LocalDate taskDeadline = LocalDate.of(2024, 9, 11);
        static final TaskStatus taskStatus = TaskStatus.TODO;
        static final String taskTitle = "Task title";
        static final String taskDescription = "Task description";
        static Task buildTask(){
           Task task = new Task(taskTitle,taskDescription,taskDeadline,taskStatus);
           task.setId(taskId);
           return task;
        }
    }

}

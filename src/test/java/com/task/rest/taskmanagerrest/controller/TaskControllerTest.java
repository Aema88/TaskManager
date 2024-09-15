package com.task.rest.taskmanagerrest.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.rest.taskmanagerrest.dto.TaskDto;
import com.task.rest.taskmanagerrest.model.Task;
import com.task.rest.taskmanagerrest.model.TaskStatus;
import com.task.rest.taskmanagerrest.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.MediaType;
import static com.task.rest.taskmanagerrest.controller.TaskControllerTest.TestResources.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    private TaskService taskService;
    @InjectMocks
    private TaskController taskController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void getAllTasks_shouldReturnAllTasks() throws Exception {
        Task task1 = buildTask();
        Task task2 = buildUpdatedTask();
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("[1].title").value(task2.getTitle()));
    }

    @Test
    void getTaskById_ShouldReturnTaskIfExists() throws Exception {
        Task task = buildTask();
        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }
    @Test
    void createTask_shouldSaveTask() throws Exception {
        Task task = buildTask();
        TaskDto taskDto = buildTaskDto();
        when(taskService.createTask(Mockito.any(Task.class))).thenReturn(task);
        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));

    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        Task existingTask = buildTask();
        Task updatedTask = buildUpdatedTask();
        TaskDto taskDto = buildTaskDto();

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskService.createTask(Mockito.any(Task.class))).thenReturn(updatedTask);


        mockMvc.perform(put("/api/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedTask.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedTask.getDescription()));
    }
    @Test
    void updateTask_ShouldReturnNotFoundIfTaskDoesNotExist() throws Exception {
        TaskDto taskDto = buildTaskDto();

        when(taskService.getTaskById(taskId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isNotFound());
    }
    @Test
    void deleteTask_ShouldReturnNoContentIfTaskExists() throws Exception {
        Task existingTask = buildTask();

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(existingTask));
        Mockito.doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_ShouldReturnNotFoundIfTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(taskId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    static class TestResources {
        static final UUID taskId = UUID.fromString("929901aa-dabf-4ea3-b488-ffd9d89c2b6c");
        static final LocalDate taskDeadline = LocalDate.of(2024, 9, 11);
        static final TaskStatus taskStatus = TaskStatus.TODO;
        static final String taskTitle = "Task title";
        static final String taskTitleUpdated = "Task title Updated";
        static final String taskDescription = "Task description";
        static Task buildTask(){
            Task task = new Task(taskTitle,taskDescription,taskDeadline,taskStatus);
            task.setId(taskId);
            return task;
        }
        static Task buildUpdatedTask(){
            Task task = new Task(taskTitleUpdated,taskDescription,taskDeadline,taskStatus);
            task.setId(taskId);
            return task;
        }
        static TaskDto buildTaskDto(){
            return TaskDto.builder()
                    .title(taskTitle)
                    .description(Optional.of(taskDescription))
                    .deadline(Optional.of(taskDeadline))
                    .status(taskStatus)
                    .build();
        }

    }
}

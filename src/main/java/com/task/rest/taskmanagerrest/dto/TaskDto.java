package com.task.rest.taskmanagerrest.dto;

import com.task.rest.taskmanagerrest.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
@Builder
public class TaskDto {
    private String title;
    private Optional<String> description;
    private Optional<LocalDate> deadline;
    private TaskStatus status;
    public Optional<String> getDescription() {
        return description == null ? Optional.empty() : description;
    }

    public Optional<LocalDate> getDeadline() {
        return deadline == null ? Optional.empty() : deadline;
    }
}

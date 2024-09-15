package com.task.rest.taskmanagerrest.model;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    public Task(String title, String description, LocalDate deadline, TaskStatus status) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }


}

package org.roadmap.tasktrackerbackend.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class TaskDTO {

    private UUID uuid;

    private String title;

    private String text;

    private Instant finishedTime;
}

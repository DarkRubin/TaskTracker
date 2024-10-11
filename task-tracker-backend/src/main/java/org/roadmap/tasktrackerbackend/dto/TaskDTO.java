package org.roadmap.tasktrackerbackend.dto;

import java.time.Instant;
import java.util.UUID;

public record TaskDTO(UUID uuid, String title, String text, Instant doBefore, Instant finishedTime) {}

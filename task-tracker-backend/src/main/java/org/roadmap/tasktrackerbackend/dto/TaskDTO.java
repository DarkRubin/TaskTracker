package org.roadmap.tasktrackerbackend.dto;

import java.util.UUID;

public record TaskDTO(UUID uuid, String title, String text) {}

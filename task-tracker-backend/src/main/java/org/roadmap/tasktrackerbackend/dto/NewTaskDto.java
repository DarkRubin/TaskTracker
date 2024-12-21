package org.roadmap.tasktrackerbackend.dto;

import org.roadmap.tasktrackerbackend.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.Instant;

public record NewTaskDto(String title, String text, @AuthenticationPrincipal User owner, Instant doBefore) {
}

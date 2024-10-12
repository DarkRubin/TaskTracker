package org.roadmap.tasktrackeremailsender.dto;

public record MessageDto (String recipient, String subject, String text) {
}

package org.roadmap.tasktrackerbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotEmpty @Email(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$") String email,
        @NotEmpty @Size(min = 8, max = 32) String password
) {}

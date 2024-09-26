package org.roadmap.tasktrackerbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDTO {
    @NotNull
    @NotEmpty
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotNull
    @NotEmpty
    @Length(min = 8, max = 32)
    private String password;

    @NotNull
    @NotEmpty
    @Length(min = 8, max = 32)
    private String confirmPassword;
}

package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "User registration request")
public record UserRequest(

        @Schema(description = "Username", example = "michalich", minLength = 5, maxLength = 30)
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 5, max = 30, message = "Username size should be 5-30 symbols")
        @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9-_]+$", message = "Username must contain only letters, digits and /-/_")
        String username,

        @Schema(description = "Password", example = "mySecretPassword123", minLength = 5, maxLength = 50)
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 5, max = 50, message = "Password size should be 5-50 symbols")
        String password,

        @Schema(description = "Email address", example = "michalicj@gmail.com", maxLength = 50)
        @NotBlank(message = "Email cannot be empty")
        @Size(max = 50, message = "Email max size should be 50 symbols")
        @Email
        String email,

        @Schema(description = "First name", example = "Michail", maxLength = 50, nullable = true)
        @Size(max = 50)
        String firstname,

        @Schema(description = "Last name", example = "Michailovich", maxLength = 50, nullable = true)
        @Size(max = 50)
        String lastname
) {
}

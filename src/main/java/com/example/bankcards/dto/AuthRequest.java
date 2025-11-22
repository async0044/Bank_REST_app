package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    @Schema(description = "Username", example = "admin606050")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 30, message = "Username size should be 5-30 symbols")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9-_]+$", message = "Username must contain only letters, digits and /-/_")
    private String username;

    @Schema(description = "Password", example = "password606050")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, max = 50, message = "Password size should be 5-50 symbols")
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

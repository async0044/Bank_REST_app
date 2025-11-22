package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import com.example.bankcards.util.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "User data response")
public record UserResponse(
        @Schema(description = "UserID", example = "12")
        Long id,
        @Schema(description = "Username", example = "john_doe")
        String username,
        @Schema(description = "Email address", example = "john.doe@example.com")
        String email,
        @Schema(description = "First name", example = "John", nullable = true)
        String firstname,
        @Schema(description = "Last name", example = "Doe", nullable = true)
        String lastname,
        @Schema(description = "User roles")
        Set<Role> roles,
        @Schema(description = "User registration date", example = "2023-12-01T15:30:00")
        LocalDateTime addedOn,
        @Schema(description = "User's bank cards")
        Set<CardResponse> cards
) {
}

package com.example.bankcards.dto;

public record ValidationResponse(
        String errorType,
        String errorMessage,
        String where
) {
}

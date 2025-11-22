package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record TransferRequest(

        @Schema(description = "Source cardID", example = "4")
        @NotNull(message = "Source card ID cannot be null")
        @Positive(message = "Source card ID must be positive")
        Long sourceCardId,

        @Schema(description = "Target cardID", example = "5")
        @NotNull(message = "Target card ID cannot be null")
        @Positive(message = "Target card ID must be positive")
        Long targetCardId,

        @Schema(description = "Amount of cash", example = "5000.00")
        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        @DecimalMax(value = "1000000.00", message = "Amount cannot exceed 1,000,000")
        @Digits(integer = 7, fraction = 2, message = "Amount must have max 7 integer and 2 fraction digits")
        Double amount
) {
}

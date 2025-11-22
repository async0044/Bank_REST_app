package com.example.bankcards.dto;

import com.example.bankcards.util.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Bank card data")
public record CardResponse(
        @Schema(description = "CardID", example = "456")
        Long id,
        @Schema(description = "Card number", example = "4111-1111-1111-1111")
        String cardNumber,
        @Schema(description = "Card holder UserID", example = "12")
        Long ownerId,
        @Schema(description = "Card holder firstName", example = "JOHN")
        String firstname,
        @Schema(description = "Card holder lastName", example = "DOE")
        String lastname,
        @Schema(description = "Card added date", example = "2023-12-01T15:30:00")
        LocalDateTime addedOn,
        @Schema(description = "Card status", example = "ACTIVE, BLOCKED")
        CardStatus cardStatus,
        @Schema(description = "Card balance", example = "1500.00")
        Double balance
) {
}

package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransferResponse(
        @Schema(description = "Source card balance after transfer", example = "10000.00")
        Double sourceCardBalance,
        @Schema(description = "Target card balance after transfer", example = "10000.00")
        Double targetCardBalance
) {
}

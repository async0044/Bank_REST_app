package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.PageCardResponse;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/user")
@Tag(name = "User actions", description = "User actions section")
@SecurityRequirement(name = "Bearer JWT-token")
public class UserController {

    @Autowired
    CardService cardService;



    @Operation(summary = "Get cards in list", description = "Returns list of all cards for current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards list successfully retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CardResponse.class)))),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/showCardList")
    public ResponseEntity<List<CardResponse>> showCardsList() {
        return ResponseEntity.ok().body(cardService.findCardsAsList());
    }



    @Operation(summary = "Get cards with filtering", description = "Returns paginated cards with balance filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards successfully retrieved",
                    content = @Content(schema = @Schema(implementation = PageCardResponse.class))),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/showCardsByFilter")
    public ResponseEntity<PageCardResponse<CardResponse>> showCardsByFilter(
            @Parameter(description = "Page number (starts from 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (number of cards)", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Minimum card balance filter", example = "1000.00")
            @RequestParam(required = false) Double minBalance,
            @Parameter(description = "Maximum card balance filter", example = "10000.00")
            @RequestParam(required = false) Double maxBalance) {

        return ResponseEntity.ok().body(cardService.findCardsByCurrentUserId(page, size, minBalance, maxBalance));
    }




    @Operation(summary = "Request card blocking", description = "Change card status to \"BLOCKINGREQUEST\" by CardID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blocking request sent successfully",
                    content = @Content(schema = @Schema(implementation = CardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card ID"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/cardBlockingRequestByCardId")
    public ResponseEntity<CardResponse> cardBlockingRequestById(
            @Parameter(description = "CardID", required = true, example = "12")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id) {

        return ResponseEntity.ok().body(cardService.cardBlockingRequestById(id));
    }



    @Operation(summary = "Check card balance", description = "Returns card balance by CardID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance successfully retrieved",
                    content = @Content(schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card ID"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/checkBalanceByCardId")
    public ResponseEntity<Double> checkBalanceByCardId (
            @Parameter(description = "CardID", required = true, example = "12")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long cardId) {

        return ResponseEntity.ok().body(cardService.checkBalanceByCardId(cardId));
    }



    @Operation(summary = "Card-to-card transfer", description = "Performs money transfer between cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer completed successfully",
                    content = @Content(schema = @Schema(implementation = TransferResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "422", description = "Insufficient funds or transfer error")
    })
    @PostMapping("/cardToCardTransfer")
    public ResponseEntity<TransferResponse> cardToCardTransfer(
            @Parameter(description = "Transfer data", required = true)
            @Valid @RequestBody TransferRequest transferRequest) {

        return ResponseEntity.ok().body(cardService.cardToCardTransfer(transferRequest));
    }
}

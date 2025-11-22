package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/admin")
@Tag(name = "Admin actions", description = "Administrator section. Administrative operations for user and card management")
@SecurityRequirement(name = "Bearer JWT-token")
public class AdminController {


    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;



    @Operation(summary = "Add new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/addUser")
    public ResponseEntity<UserResponse> addUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.addUser(userRequest));
    }



    @Operation(summary = "Get user by ID", description = "Retrieves user information by user UserID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/getUserById")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id) {

        return ResponseEntity.ok().body(userService.getUserById(id));
    }




    @Operation(summary = "Get user by username", description = "Retrieves user information by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/getUserByUsername")
    public ResponseEntity<UserResponse> getUserByUsername(
            @Parameter(description = "Username (5-30 characters)", required = true, example = "john_doe")
            @RequestParam @NotBlank(message = "Username cannot be empty")
            @Size(min = 5, max = 30) String username) {

        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }




    @Operation(summary = "Get user by email", description = "Retrieves user information by email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid email format"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("getUserByEmail")
    public ResponseEntity<UserResponse>  getUserByEmail(
            @Parameter(description = "Email address", required = true, example = "user@example.com")
            @RequestParam @NotBlank(message = "Email cannot be empty")
            @Email String email) {

        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }




    @Operation(summary = "Get all users", description = "Retrieves list of all users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users list retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required")
    })
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    //TODO Make getAllUser with pagination


    @Operation(summary = "Add role to user", description = "Adds a specific role to user by UserID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role added successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or role"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/addRoleById")
    public ResponseEntity<UserResponse> addRoleById(
            @Parameter(description = "UserID", required = true, example = "3")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id,
            @Parameter(description = "Role", required = true, example = "ADMIN")
            @NotBlank(message = "Role cannot be empty")
            @RequestParam String role) {

        return ResponseEntity.ok().body(userService.addRoleById(id, role));
    }



    @Operation(summary = "Remove role from user", description = "Removes a specific role from user by UserID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role removed successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or role"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/removeRoleById")
    public ResponseEntity<UserResponse> removeRoleById(
            @Parameter(description = "UserID", required = true, example = "3")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id,
            @Parameter(description = "Role", required = true, example = "ADMIN")
            @NotBlank(message = "Role cannot be empty")
            @RequestParam String role) {

        return ResponseEntity.ok().body(userService.removeRoleById(id, role));
    }



    @Operation(summary = "Delete user by ID", description = "Deletes user from system by UserID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/deleteUserById")
    public ResponseEntity<UserResponse> deleteUserById(
            @Parameter(description = "UserID", required = true, example = "12")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id) {

        return ResponseEntity.ok().body(userService.deleteUserById(id));
    }



    @Operation(summary = "Create card for user", description = "Creates a new card for specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card created successfully",
                    content = @Content(schema = @Schema(implementation = CardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/createCardByUserId")
    public ResponseEntity<CardResponse> createCardByUserId(
            @Parameter(description = "UserID", required = true, example = "4")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long userId) {
        return ResponseEntity.ok().body(cardService.createCardByUserId(userId));
    }



    @Operation(summary = "Block card by ID", description = "Blocks a card by CardID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card blocked successfully",
                    content = @Content(schema = @Schema(implementation = CardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card ID"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PutMapping("/blockCardById")
    public ResponseEntity<CardResponse> blockCardById(
            @Parameter(description = "CardID", required = true, example = "4")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id) {

        return ResponseEntity.ok().body(cardService.blockCardById(id));
    }



    @Operation(summary = "Activate card by ID", description = "Activates a card by CardID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card activated successfully",
                    content = @Content(schema = @Schema(implementation = CardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card ID"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PutMapping("/activateCardById")
    public ResponseEntity<CardResponse> activateCardById(
            @Parameter(description = "CardID", required = true, example = "4")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id) {

        return ResponseEntity.ok().body(cardService.activateCardById(id));
    }




    @Operation(summary = "Delete card by ID", description = "Deletes a card by CardID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully",
                    content = @Content(schema = @Schema(implementation = CardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card ID"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin rights required"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @DeleteMapping("/deleteCardById")
    public ResponseEntity<CardResponse> deleteCardById(
            @Parameter(description = "CardID", required = true, example = "4")
            @RequestParam @NotNull(message = "ID cannot be null")
            @Positive(message = "ID must be positive") Long id) {

        return ResponseEntity.ok().body(cardService.deleteCardById(id));
    }

}

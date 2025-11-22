package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthRequest;
import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for authorization and authentification users")
public class AuthController {

    @Autowired
    private UserService userService;


    @Operation(summary = "Register new user", description = "Creates a new user account and returns JWT token for API access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data - validation failed"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.addUser(userRequest));
    }



    @Operation(summary = "User's authentification", description = "Return JWT-token for access to API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authentification",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect credentials"),
            @ApiResponse(responseCode = "400", description = "Incorrect request")
    })
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @Parameter(description = "Authentification credentials", required = true) @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(userService.authenticateUser(authRequest));
    }
}

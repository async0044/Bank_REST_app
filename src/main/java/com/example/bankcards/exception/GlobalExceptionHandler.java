package com.example.bankcards.exception;

import com.example.bankcards.dto.ExceptionResponse;
import com.example.bankcards.dto.ValidationResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ValidationResponse>> handleConstraintViolationException(ConstraintViolationException e) {
        List<ValidationResponse> errors = e.getConstraintViolations().stream().map(error -> new ValidationResponse(
                "ConstraintViolationException",
                error.getMessage(),
                error.getPropertyPath().toString()
        )).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors().stream().map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        "MethodArgumentNotValidException",
                        String.join(", ", errors),
                        e.getNestedPath()));
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> alreadyExistHandler(AlreadyExistException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getClass().getName(),
                        e.getMessage(),
                        e.getWhere()));
    }

    @ExceptionHandler(SomethingNotFoundException.class)
    public ResponseEntity<ExceptionResponse> somethingNotFoundHandler(SomethingNotFoundException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getClass().getName(),
                        e.getMessage(),
                        e.getWhere()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse> authHandler(AuthException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getClass().getName(),
                        e.getMessage(),
                        e.getWhere()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        "MissingServletRequestParameterException",
                        String.format("Required parameter '%s' is missing", e.getParameterName()),
                        e.getMethodParameter().getMethod().toString().split(" ")[2]));
    }

    @ExceptionHandler(RoleValidationException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(RoleValidationException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        "RoleValidationException",
                        e.getMessage(),
                        e.getWhere()));
    }

    @ExceptionHandler(BalanceException.class)
    public ResponseEntity<ExceptionResponse> handleBalance(BalanceException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        "BalanceException",
                        e.getMessage(),
                        e.getWhere()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        "AccessDeniedException",
                        e.getMessage(),
                        "null"));
    }

    @ExceptionHandler(DeleteUserException.class)
    public ResponseEntity<ExceptionResponse> handleDeleteUserException(DeleteUserException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                "DeleteUserException",
                e.getMessage(),
                e.getWhere()));
    }

}

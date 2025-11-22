package com.example.bankcards.dto;

import java.time.LocalDateTime;

public class ExceptionResponse {

    private String nameOfException;
    private String message;
    private String where;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String nameOfException, String message, String where) {
        this.nameOfException = nameOfException;
        this.message = message;
        this.where = where;
    }

    public String getNameOfException() {
        return nameOfException;
    }

    public void setNameOfException(String nameOfException) {
        this.nameOfException = nameOfException;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}

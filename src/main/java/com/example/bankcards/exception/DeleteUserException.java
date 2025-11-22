package com.example.bankcards.exception;

public class DeleteUserException extends RuntimeException {

    private String where;

    public DeleteUserException(String message, String where) {
        super(message);
        this.where = where;
    }

    public String getWhere() {
        return where;
    }
}

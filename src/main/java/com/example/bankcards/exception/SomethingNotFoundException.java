package com.example.bankcards.exception;

public class SomethingNotFoundException extends RuntimeException {
    private String where;

    public SomethingNotFoundException(String message) {
        super(message);
    }

    public SomethingNotFoundException(String message, String where) {
        super(message);
        this.where = "Where: " + where;
    }

    public String getWhere() {
        return where;
    }
}

package com.example.bankcards.exception;

public class AlreadyExistException extends RuntimeException {

    private String where;

    public AlreadyExistException(String message, String where) {
        super(message);
        this.where = where;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}

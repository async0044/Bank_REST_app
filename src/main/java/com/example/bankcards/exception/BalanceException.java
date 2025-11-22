package com.example.bankcards.exception;

public class BalanceException extends RuntimeException {

    String where;

    public BalanceException(String message, String where) {
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

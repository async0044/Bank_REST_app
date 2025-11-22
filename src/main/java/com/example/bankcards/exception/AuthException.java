package com.example.bankcards.exception;

public class AuthException extends RuntimeException {

  String where;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, String where) {
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

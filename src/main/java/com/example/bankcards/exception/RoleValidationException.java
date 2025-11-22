package com.example.bankcards.exception;

public class RoleValidationException extends IllegalArgumentException {
  private String where;

    public RoleValidationException(String message, String where) {
        super(message);
        this.where = where;
    }

  public String getWhere() {
    return where;
  }
}

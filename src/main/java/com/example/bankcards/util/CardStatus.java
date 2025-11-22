package com.example.bankcards.util;

public enum CardStatus {
    ACTIVE,
    BLOCKED,
    EXPIRED,
    BLOCKINGREQUEST;

    public String getCardStatus() {
        return this.name();
    }
}

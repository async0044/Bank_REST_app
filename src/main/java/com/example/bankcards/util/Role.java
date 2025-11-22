package com.example.bankcards.util;

import java.util.Arrays;

public enum Role {
    ADMIN,
    USER;

    public String getString() {
        return this.name();
    }

    public static boolean isValidRole(final String roleName) {
        return Arrays.stream(values())
                .anyMatch(role -> role.name().equalsIgnoreCase(roleName));
    }

}

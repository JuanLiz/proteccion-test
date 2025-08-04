package com.juanliz.protecciontest.model;

public enum UserRole {
    ADMIN("Administrador"),
    USER("Usuario");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}

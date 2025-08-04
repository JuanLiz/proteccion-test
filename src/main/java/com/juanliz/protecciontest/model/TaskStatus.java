package com.juanliz.protecciontest.model;

public enum TaskStatus {
    PENDING("Pendiente"),
    IN_PROGRESS("En progreso"),
    COMPLETED("Completada"),
    CANCELLED("Cancelada");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

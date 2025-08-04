package com.juanliz.protecciontest.model;

public enum UserRole {
    ADMIN("Administrador"),
    INVENTORY("Inventario"),
    PRODUCT_VIEW("Vista de productos"),
    ORDERS("Compras");

    private final String value;


    UserRole(String value) {
        this.value = value;
    }
}

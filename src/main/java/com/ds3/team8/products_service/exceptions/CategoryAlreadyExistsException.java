package com.ds3.team8.products_service.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String name) {
        super("La categoria con nombre '" + name + "' ya existe.");
    }
}

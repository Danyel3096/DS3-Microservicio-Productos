package com.ds3.team8.products_service.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("La categoria con ID " + id + " no fue encontrada.");
    }
}

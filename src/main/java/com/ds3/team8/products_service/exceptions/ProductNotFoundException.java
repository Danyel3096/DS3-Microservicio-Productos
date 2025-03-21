package com.ds3.team8.products_service.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("El producto con ID " + id + " no fue encontrado.");
    }
}

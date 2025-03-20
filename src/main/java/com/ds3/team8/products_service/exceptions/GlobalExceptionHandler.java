package com.ds3.team8.products_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GlobalExceptionHandler {

    public static class ProductExistingException extends RuntimeException {
        public ProductExistingException(String name) {
            super("El producto con nombre '" + name + "' ya existe.");
        }
    }

    public static class CategoryExistingException extends RuntimeException {
        public CategoryExistingException(String name) {
            super("La categoria con nombre '" + name + "' ya existe.");
        }
    }

    @ExceptionHandler(ProductExistingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleProductExistingException(ProductExistingException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CategoryExistingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleCategoryExistingException(CategoryExistingException ex) {
        return ex.getMessage();
    }
}

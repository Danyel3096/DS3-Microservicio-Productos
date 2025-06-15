package com.ds3.team8.products_service.exceptions;

public class CategoryDeletionException extends RuntimeException{
    public CategoryDeletionException(Long id) {
        super("La categoria con ID " + id + " no puede eliminarse porque tiene productos asociados.");}
}

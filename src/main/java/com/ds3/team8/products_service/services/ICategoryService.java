package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    CategoryResponse save(CategoryRequest categoryRequest); // Guardar categoría
    void delete(Long id); // Eliminar categoría
    CategoryResponse findById(Long id); // Buscar categoría por ID
    CategoryResponse update(Long id, CategoryRequest categoryRequest); // Actualizar categoría
    List<CategoryResponse> findAll(); // Buscar todas las categorías
    Page<CategoryResponse> findAllPageable(Pageable pageable); // Buscar todas las categorías con paginación
}


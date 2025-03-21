package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.exceptions.CategoryAlreadyExistsException;
import com.ds3.team8.products_service.repositories.ICategoryRepository;

import org.springframework.stereotype.Service;

@Service

public class CategoryServiceImpl implements ICategoryService {
    private ICategoryRepository categoryRepository;

    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //Verificamos si la categoría ya existe en la base de datos
    public CategoryResponse save(CategoryRequest categoryRequest) {

        if (categoryRepository.findByName(categoryRequest.getName()).isPresent()) {
            throw new CategoryAlreadyExistsException(categoryRequest.getName());
        }
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setIs_active(true);

        // Guardando
        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    private CategoryResponse convertToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIs_active()
        );
    }
}


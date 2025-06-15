package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.exceptions.CategoryAlreadyExistsException;
import com.ds3.team8.products_service.exceptions.CategoryDeletionException;
import com.ds3.team8.products_service.exceptions.CategoryNotFoundException;
import com.ds3.team8.products_service.repositories.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ICategoryService interface.
 * Handles business logic for Category operations.
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    private ICategoryRepository categoryRepository;

    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Verificamos si la categoría ya existe en la base de datos
    public CategoryResponse save(CategoryRequest categoryRequest) {
        categoryRepository.findByName(categoryRequest.getName()).ifPresent(category -> {
            throw new CategoryAlreadyExistsException(categoryRequest.getName());
        });

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setIsActive(true);

        return convertToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        // Cambiar el estado a inactivo
        existingCategory.setIs_active(false);

        category.setIsActive(false);
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        return convertToResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        Optional<Category> categoryWithSameName = categoryRepository.findByName(categoryRequest.getName());
        if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getId().equals(id)) {
            throw new CategoryAlreadyExistsException(categoryRequest.getName());
        }

        category.setName(categoryRequest.getName());

        return convertToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    /**
     * Converts a Category entity to its DTO response.
     */
    private CategoryResponse convertToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIsActive());
    }
}

package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.exceptions.CategoryAlreadyExistsException;
import com.ds3.team8.products_service.exceptions.CategoryDeletionException;
import com.ds3.team8.products_service.exceptions.CategoryNotFoundException;
import com.ds3.team8.products_service.repositories.ICategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CategoryServiceImpl implements ICategoryService {
    private ICategoryRepository categoryRepository;

    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Verificamos si la categoría ya existe en la base de datos
    public CategoryResponse save(CategoryRequest categoryRequest) {

        if (categoryRepository.findByName(categoryRequest.getName()).isPresent()) {
            throw new CategoryAlreadyExistsException(categoryRequest.getName());
        }
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setIsActive(true);

        // Guardando
        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    @Override
    public void delete(Long id) {
        // Buscar la categoria en la base de datos
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
// Verificar si tiene productos asociados
        if (!existingCategory.getProducts().isEmpty()) {
            throw new CategoryDeletionException(id);
        }
        // Cambiar el estado a inactivo
        existingCategory.setIsActive(false);

        // Guardar los cambios en la base de datos
        categoryRepository.save(existingCategory);
    }

    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        return convertToResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        // Buscar la categoría existente
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        // Verificar si el nuevo nombre ya está en uso por otra categoría
        Optional<Category> categoryWithSameName = categoryRepository.findByName(categoryRequest.getName());
        if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getId().equals(id)) {
            throw new CategoryAlreadyExistsException(categoryRequest.getName());
        }

        // Actualizar los datos de la categoría
        existingCategory.setName(categoryRequest.getName());

        // Guardar cambios en la base de datos
        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToResponse(updatedCategory);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private CategoryResponse convertToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIsActive());
    }
}

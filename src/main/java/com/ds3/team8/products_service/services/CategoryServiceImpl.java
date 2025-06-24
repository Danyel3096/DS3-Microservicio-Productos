package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.exceptions.NotFoundException;
import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.mappers.CategoryMapper;
import com.ds3.team8.products_service.repositories.ICategoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(ICategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryResponse save(CategoryRequest categoryRequest) {
        // Verificar si la categoría ya existe
        Optional<Category> existingCategory = categoryRepository.findByNameAndIsActiveTrue(categoryRequest.getName());
        if (existingCategory.isPresent()) {
            logger.warn("Intento de creación de categoría con nombre ya existente: {}", categoryRequest.getName());
            throw new BadRequestException("La categoría ya existe");
        }

        // Crear nueva categoría
        Category category = categoryMapper.toCategory(categoryRequest);
        Category savedCategory = categoryRepository.save(category);
        logger.info("Categoría creada: {}", savedCategory.getName());
        // Mapear a DTO
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(id);
        if (categoryOptional.isEmpty()) {
            logger.warn("Intento de eliminación de categoría no encontrada con ID: {}", id);
            throw new NotFoundException("Categoría no encontrada");
        }
        // Marcar categoría como inactiva
        Category category = categoryOptional.get();

        // Verificar si la categoría tiene productos asociados
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            logger.warn("Intento de eliminación de categoría con productos asociados: {}", category.getName());
            throw new BadRequestException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        logger.info("Categoría eliminada: {}", category.getName());
        category.setIsActive(false);
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(id);
        if (categoryOptional.isEmpty()) {
            logger.warn("Intento de búsqueda de categoría no encontrada con ID: {}", id);
            throw new NotFoundException("Categoría no encontrada");
        }
        // Mapear a DTO
        logger.info("Categoría encontrada con ID: {}", id);
        return categoryMapper.toCategoryResponse(categoryOptional.get());
    }

    @Transactional
    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(id);
        if (categoryOptional.isEmpty()) {
            logger.warn("Intento de actualización de categoría no encontrada con ID: {}", id);
            throw new NotFoundException("Categoría no encontrada");
        }

        // Verificar si la categoría ya existe
        Optional<Category> existingCategory = categoryRepository.findByNameAndIsActiveTrue(categoryRequest.getName());
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(id)) {
            logger.warn("Intento de actualización de categoría con nombre ya existente: {}", categoryRequest.getName());
            throw new BadRequestException("La categoría ya existe");
        }

        // Actualizar categoría
        Category category = categoryMapper.updateCategory(categoryOptional.get(), categoryRequest);
        Category updatedCategory = categoryRepository.save(category);
        logger.info("Categoría actualizada: {}", updatedCategory.getName());
        // Mapear a DTO
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAllByIsActiveTrue();
        if (categories.isEmpty()) {
            logger.warn("No se encontraron categorías activas");
            throw new NotFoundException("No se encontraron categorías activas");
        }
        logger.info("Número de categorías activas encontradas: {}", categories.size());
        return categoryMapper.toCategoryResponseList(categories);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CategoryResponse> findAllPageable(Pageable pageable) {
        Page<Category> categoriesPage = categoryRepository.findAllByIsActiveTrue(pageable);
        if (categoriesPage.isEmpty()) {
            logger.warn("No se encontraron categorías activas en la paginación");
            throw new NotFoundException("No se encontraron categorías activas");
        }
        logger.info("Número de categorías activas encontradas (paginadas): {}", categoriesPage.getTotalElements());
        return categoriesPage.map(categoryMapper::toCategoryResponse);
    }
}

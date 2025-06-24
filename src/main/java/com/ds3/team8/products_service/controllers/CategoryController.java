package com.ds3.team8.products_service.controllers;

import com.ds3.team8.products_service.client.enums.Role;
import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.services.ICategoryService;
import com.ds3.team8.products_service.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categorías", description = "Endpoints para categorías")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Obtener todas las categorías
    @Operation(summary = "Obtener todas las categorías", description = "Obtener todas las categorías del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // Obtener categorías con paginación
    // Ejemplo URL /api/v1/categories/pageable?page=0&size=8
    @Operation(summary = "Obtener las categorías con paginación", description = "Obtener las categorías con paginación del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/pageable")
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(Pageable pageable) {
        Page<CategoryResponse> categories = categoryService.findAllPageable(pageable);
        return ResponseEntity.ok(categories);
    }

    // Obtener una categoría por ID
    @Operation(summary = "Obtener una categoría por su id", description = "Obtener una categoría por su id del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    // Crear una nueva categoría
    @Operation(summary = "Guardar una categoría", description = "Guardar una categoría en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest,
            @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        CategoryResponse savedCategory = categoryService.save(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // Actualizar una categoría existente
    @Operation(summary = "Actualizar una categoría por su id", description = "Actualizar una categoría por su id en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest,
            @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        CategoryResponse updatedCategory = categoryService.update(id, categoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }

    // Eliminación lógica de una categoría
    @Operation(summary = "Eliminar una categoría por su id", description = "Eliminar una categoría por su id en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

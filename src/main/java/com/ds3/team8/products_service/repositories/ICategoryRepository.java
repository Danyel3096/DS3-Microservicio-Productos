package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndIsActiveTrue(String name); // Obtener categoría por nombre y activo
    Optional<Category> findByIdAndIsActiveTrue(Long id); // Obtener categoría por ID y activo
    List<Category> findAllByIsActiveTrue(); // Obtener todas las categorías activas
    Page<Category> findAllByIsActiveTrue(Pageable pageable); // Obtener todas las categorías activas con paginación
}
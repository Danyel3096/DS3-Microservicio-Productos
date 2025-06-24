package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndIsActiveTrue(Long id); // Obtener producto por ID y activo
    List<Product> findAllByIsActiveTrue(); // Obtener todos los productos activos
    Page<Product> findAllByIsActiveTrue(Pageable pageable); // Obtener todos los productos activos con paginación
    List<Product> findByCategoryIdAndIsActiveTrue(Long categoryId); // Obtener productos por ID de categoría y activo
    Page<Product> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable); // Obtener productos por ID de categoría y activo con paginación
}

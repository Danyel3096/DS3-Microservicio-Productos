package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    // Buscar productos activos
    List<Product> findByIsActiveTrue();

    // Buscar por nombre exacto
    Optional<Product> findByName(String name);

    // Buscar productos con stock menor a cierto valor
    List<Product> findByStockLessThan(int stock);

    // Buscar productos por categoría
    List<Product> findByCategoryId(Long categoryId);
}

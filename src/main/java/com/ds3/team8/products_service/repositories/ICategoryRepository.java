package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    // metodo para encontrar activas
    List<Category> findByIsActiveTrue();
}

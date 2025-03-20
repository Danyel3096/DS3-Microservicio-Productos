package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(@NotEmpty(message = "No puede estar vacio") @Size(min = 2, max = 20, message = "El tamaño tiene que estar entre 2 y 20") String name);
}

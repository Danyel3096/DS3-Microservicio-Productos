package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(@NotEmpty(message = "No puede estar vacio") @Size(min = 2, max = 20, message = "El tamaño tiene que estar entre 2 y 20") String name);
}

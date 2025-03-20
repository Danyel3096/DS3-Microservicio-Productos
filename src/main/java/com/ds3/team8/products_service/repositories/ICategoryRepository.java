package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
}

package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.exceptions.GlobalExceptionHandler;
import com.ds3.team8.products_service.repositories.ICategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements ICategoryService {

    ICategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category save(Category category) {
        if (categoryRepository.findByName((category.getName())).isPresent()) {
            throw new GlobalExceptionHandler.ProductExistingException(category.getName());
        }
        return categoryRepository.save(category);
    }
}

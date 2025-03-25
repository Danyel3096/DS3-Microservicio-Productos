package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;


import java.util.List;

public interface ICategoryService {
    CategoryResponse save(CategoryRequest categoryRequest);

    void delete(Long id);

    CategoryResponse findById(Long id);

    CategoryResponse update(Long id, CategoryRequest categoryRequest);

    List<CategoryResponse> findAll();
}

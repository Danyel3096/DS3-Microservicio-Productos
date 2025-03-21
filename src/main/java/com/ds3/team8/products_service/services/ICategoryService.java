package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.entities.Product;

public interface ICategoryService {
    CategoryResponse save(CategoryRequest categoryRequest);
}

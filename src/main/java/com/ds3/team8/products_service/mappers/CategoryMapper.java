package com.ds3.team8.products_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.entities.Category;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) return null;

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public Category toCategory(CategoryRequest categoryRequest) {
        if (categoryRequest == null) return null;

        return new Category(
                categoryRequest.getName(),
                categoryRequest.getDescription()
        );
    }

    public Category updateCategory(Category category, CategoryRequest categoryRequest) {
        if (category == null || categoryRequest == null) return null;

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        return category;
    }

    public List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        if (categories == null) return List.of();
        if (categories.isEmpty()) return List.of();

        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }
}

package com.ds3.team8.products_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.exceptions.CategoryAlreadyExistsException;
import com.ds3.team8.products_service.exceptions.CategoryNotFoundException;
import com.ds3.team8.products_service.repositories.ICategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class CategoryServiceImplTest {
    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveCategory_Success() {
        CategoryRequest request = new CategoryRequest("Bebidas");
        Category category = new Category();
        category.setId(1L);
        category.setName("Bebidas");
        category.setIsActive(true);

        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse response = categoryService.save(request);

        assertNotNull(response);
        assertEquals("Bebidas", response.getName());
        assertTrue(response.getIsActive());
    }

    @Test
    void testSaveCategory_AlreadyExists() {
        CategoryRequest request = new CategoryRequest("Bebidas");
        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.of(new Category()));

        assertThatThrownBy(() -> categoryService.save(request))
                .isInstanceOf(CategoryAlreadyExistsException.class);
    }

    @Test
    void testFindById_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Bebidas");
        category.setIsActive(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.findById(1L);
        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.findById(1L))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void testFindAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(null, "Bebidas", true, new ArrayList<>()));
        categories.add(new Category(null, "Snacks", true, new ArrayList<>()));


        when(categoryRepository.findByIsActiveTrue()).thenReturn(categories);

        List<CategoryResponse> responses = categoryService.findAll();
        assertEquals(2, responses.size());
    }
}

package com.ds3.team8.products_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.entities.Product;
import com.ds3.team8.products_service.exceptions.CategoryNotFoundException;
import com.ds3.team8.products_service.exceptions.ProductNotFoundException;
import com.ds3.team8.products_service.repositories.ICategoryRepository;
import com.ds3.team8.products_service.repositories.IProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

public class ProductServicelmplTest {
    @Mock
    private IProductRepository productRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    @Test
    void testSaveProduct_Success() {
        ProductRequest request = new ProductRequest("Producto 1", "El mejor", new BigDecimal("19.99"), 50, 1L);
        Category category = new Category(1L, "Bebidas", true, new ArrayList<>());
        Product product = new Product(1L, "Producto 1", "El mejor", new BigDecimal("19.99"), 50, true, category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.save(request);

        assertNotNull(response);
        assertEquals("Producto 1", response.getName());
        assertEquals(new BigDecimal("19.99"), response.getPrice());
        assertTrue(response.getIsActive());
    }

    @Test
    void testSaveProduct_CategoryNotFound() {
        ProductRequest request = new ProductRequest("Producto 1", "El mejor", new BigDecimal("19.99"), 50, 1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.save(request))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void testFindById_Success() {
        Category category = new Category(1L, "Bebidas", true, new ArrayList<>());
        Product product = new Product(1L, "Producto 1", "El mejor", new BigDecimal("19.99"), 50, true, category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}

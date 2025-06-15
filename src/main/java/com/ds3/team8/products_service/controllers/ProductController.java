package com.ds3.team8.products_service.controllers;

import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieve all products.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieve all products with pagination.
     * Example: /api/v1/products/pageable?page=0&size=8
     */
    @GetMapping("/pageable")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        Page<ProductResponse> productsPage = productService.findAll(pageable);
        return ResponseEntity.ok(productsPage);
    }

    /**
     * Retrieve a product by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Create a new product.
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse createdProduct = productService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Update an existing product by its ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updatedProduct = productService.update(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Soft delete a product by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

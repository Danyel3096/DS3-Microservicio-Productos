package com.ds3.team8.products_service.controllers;

import com.ds3.team8.products_service.dtos.StockRequest;
import com.ds3.team8.products_service.client.enums.Role;
import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.services.IProductService;
import com.ds3.team8.products_service.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Productos", description = "Endpoints para productos")
public class ProductController {

    private IProductService productService;

    public ProductController(IProductService productService){
        this.productService = productService;
    }

    // Obtener todos los productos
    @Operation(summary = "Obtener todos los productos", description = "Obtener todos los productos del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    // Obtener productos con paginación
    // Ejemplo URL /api/v1/products/pageable?page=0&size=8
    @Operation(summary = "Obtener los productos con paginación", description = "Obtener los productos con paginación del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/pageable")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        Page<ProductResponse> products = productService.findAllPageable(pageable);
        return ResponseEntity.ok(products);
    }

    // Obtener un producto por ID
    @Operation(summary = "Obtener un producto por su id", description = "Obtener un producto por su id del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    // Crear un nuevo producto
    @Operation(summary = "Guardar un producto", description = "Guardar un producto en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        ProductResponse savedProduct = productService.save(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // Actualizar un producto existente
    @Operation(summary = "Actualizar un producto", description = "Actualizar un producto en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest,
            @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        ProductResponse updatedProduct = productService.update(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    // Eliminación lógica de un producto
    @Operation(summary = "Eliminar un producto", description = "Eliminar un producto por su id del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener productos por ID de categoría
    @Operation(summary = "Obtener productos por ID de categoría", description = "Obtener productos por ID de categoría del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findByCategoryId(categoryId));
    }

    // Obtener productos por ID de categoría con paginación
    // Ejemplo URL /api/v1/products/category/1/pageable?page=0&size=8
    @Operation(summary = "Obtener productos por ID de categoría con paginación", description = "Obtener productos por ID de categoría con paginación del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/category/{categoryId}/pageable")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategoryId(@PathVariable Long categoryId, Pageable pageable) {
        Page<ProductResponse> products = productService.findByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    // Actualizar stock de productos
    @Hidden
    @PostMapping("/update-stock")
    public ResponseEntity<Void> updateStock(
            @Valid @RequestBody List<StockRequest> requests
    ) {
        productService.updateStock(requests);
        return ResponseEntity.noContent().build();
    }

    // Validar stock de productos
    @Hidden
    @PostMapping("/validate-stock")
    public ResponseEntity<Boolean> validateStock(
            @Valid @RequestBody List<StockRequest> requests
    ) {
        Boolean isValid = productService.validateStock(requests);
        return ResponseEntity.ok(isValid);
    }
}


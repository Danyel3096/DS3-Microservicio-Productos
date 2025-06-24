package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.StockRequest;
import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IProductService {
    ProductResponse save(ProductRequest productRequest); // Guardar producto
    void delete(Long id); // Eliminar producto
    ProductResponse findById(Long id); // Buscar producto por ID
    ProductResponse update(Long id, ProductRequest productRequest); // Actualizar producto
    List<ProductResponse> findAll(); // Buscar todos los productos
    Page<ProductResponse> findAllPageable(Pageable pageable); // Buscar todos los productos con paginación
    List<ProductResponse> findByCategoryId(Long categoryId); // Buscar productos por ID de categoría
    Page<ProductResponse> findByCategoryId(Long categoryId, Pageable pageable); // Buscar productos por ID de categoría con paginación
    void updateStock(List<StockRequest> requests); // Actualizar stock de productos
    Boolean validateStock(List<StockRequest> requests); // Validar stock de productos
}
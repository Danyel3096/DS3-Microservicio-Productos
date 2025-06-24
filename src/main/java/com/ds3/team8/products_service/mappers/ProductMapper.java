package com.ds3.team8.products_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.entities.Product;

@Component
public class ProductMapper {

    public ProductResponse toProductResponse(Product product) {
        if (product == null) return null;

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public Product toProduct(ProductRequest productRequest) {
        if (productRequest == null) return null;

        return new Product(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStock()
        );
    }

    public Product updateProduct(Product product, ProductRequest productRequest) {
        if (product == null || productRequest == null) return null;

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());

        return product;
    }

    public List<ProductResponse> toProductResponseList(List<Product> products) {
        if (products == null) return List.of();
        if (products.isEmpty()) return List.of();

        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }
}
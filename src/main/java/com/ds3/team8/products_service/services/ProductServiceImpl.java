package com.ds3.team8.products_service.services;


import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.entities.Product;
import com.ds3.team8.products_service.exceptions.CategoryNotFoundException;
import com.ds3.team8.products_service.repositories.ICategoryRepository;
import com.ds3.team8.products_service.repositories.IProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

    IProductRepository productRepository;
    ICategoryRepository categoryRepository;

    public ProductServiceImpl(IProductRepository productRepository, ICategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        // verificar la categoría exista y esté activa
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .filter(Category::getIs_active)
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setIsActive(true);
        product.setCategory(category);

        // Guardando
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getIsActive(),
                product.getCategory().getId()
        );
    }

}

package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.entities.Product;
import com.ds3.team8.products_service.exceptions.GlobalExceptionHandler;
import com.ds3.team8.products_service.repositories.IProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

    IProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        if (productRepository.findByName((product.getName())).isPresent()) {
            throw new GlobalExceptionHandler.ProductExistingException(product.getName());
        }
        return productRepository.save(product);
    }

}

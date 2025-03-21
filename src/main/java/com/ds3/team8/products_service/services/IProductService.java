package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.entities.Product;

public interface IProductService {
    ProductResponse save(ProductRequest productRequest);
}

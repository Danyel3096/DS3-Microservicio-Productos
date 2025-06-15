package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IProductService {
    ProductResponse save(ProductRequest productRequest);

    void delete(Long id);

    ProductResponse findById(Long id);

    ProductResponse update(Long id, ProductRequest productRequest);

    List<ProductResponse> findAll();

    Page<ProductResponse> findAll(Pageable pageable);
}

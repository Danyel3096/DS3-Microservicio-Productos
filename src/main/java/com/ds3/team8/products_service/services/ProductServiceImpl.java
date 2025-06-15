package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.entities.Product;
import com.ds3.team8.products_service.exceptions.CategoryNotFoundException;
import com.ds3.team8.products_service.exceptions.ProductNotFoundException;
import com.ds3.team8.products_service.exceptions.ProductNotFoundException;
import com.ds3.team8.products_service.repositories.ICategoryRepository;
import com.ds3.team8.products_service.repositories.IProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of IProductService interface.
 * Manages business logic for product operations.
 */
@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    public ProductServiceImpl(IProductRepository productRepository, ICategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional // Modifica la base de datos
    @Override
    @Transactional
    public ProductResponse save(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .filter(Category::getIsActive)
                .filter(Category::getIsActive)
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setIsActive(true);
        product.setCategory(category);

        return convertToResponse(productRepository.save(product));
    }

    /**
     * Logically deletes (deactivates) a product by ID.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setIsActive(false);
        productRepository.save(product);
    }

    /**
     * Updates a product and its associated category.
     */
    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .filter(Category::getIsActive)
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCategory(category);

        return convertToResponse(productRepository.save(product));
    }

    /**
     * Returns a product by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return convertToResponse(product);
    }

    /**
     * Returns all products.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional // Modifica la base de datos
    @Override
    public void delete(Long id) {
        // Buscar el producto en la base de datos
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Cambiar el estado a inactivo
        existingProduct.setIsActive(false);

        // Guardar los cambios en la base de datos
        productRepository.save(existingProduct);
    }

    @Transactional // Modifica la base de datos
    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        // Validar que el producto exista
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Validar que la nueva categoría exista y esté activa
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .filter(Category::getIsActive)
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        // Actualizar los datos del producto
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToResponse(updatedProduct);
    }

    @Transactional(readOnly = true) // Solo lectura
    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return convertToResponse(product);
    }

    @Transactional // Modifica la base de datos
    @Override
    public void delete(Long id) {
        // Buscar el producto en la base de datos
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Cambiar el estado a inactivo
        existingProduct.setIsActive(false);

        // Guardar los cambios en la base de datos
        productRepository.save(existingProduct);
    }

    @Transactional // Modifica la base de datos
    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        // Validar que el producto exista
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Validar que la nueva categoría exista y esté activa
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .filter(Category::getIsActive)
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        // Actualizar los datos del producto
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToResponse(updatedProduct);
    }

    @Transactional(readOnly = true) // Solo lectura
    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return convertToResponse(product);
    }

    @Transactional(readOnly = true) // Solo lectura
    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true) // Solo lectura
    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true) // Solo lectura
    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true) // Solo lectura
    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * Returns paginated list of products.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * Converts a Product entity into its DTO response.
     */
    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getIsActive(),
                product.getCategory().getId());
    }
}

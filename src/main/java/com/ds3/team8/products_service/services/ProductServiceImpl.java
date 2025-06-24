package com.ds3.team8.products_service.services;

import com.ds3.team8.products_service.exceptions.NotFoundException;
import com.ds3.team8.products_service.dtos.StockRequest;
import com.ds3.team8.products_service.client.OrderItemClient;
import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.entities.Product;
import com.ds3.team8.products_service.mappers.ProductMapper;
import com.ds3.team8.products_service.repositories.ICategoryRepository;
import com.ds3.team8.products_service.repositories.IProductRepository;

import feign.FeignException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final OrderItemClient orderItemClient;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(IProductRepository productRepository, ICategoryRepository categoryRepository, ProductMapper productMapper, OrderItemClient orderItemClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.orderItemClient = orderItemClient;
    }

    @Transactional
    @Override
    public ProductResponse save(ProductRequest productRequest) {
        // Verificar si la categoría existe
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(productRequest.getCategoryId());
        if (categoryOptional.isEmpty()) {
            logger.warn("Intento de crear producto con categoría no existente: {}", productRequest.getCategoryId());
            throw new BadRequestException("Categoría no encontrada");
        }

        // Crear nuevo producto
        Product product = productMapper.toProduct(productRequest);
        product.setCategory(categoryOptional.get());
        // Guardar el producto en la base de datos
        Product savedProduct = productRepository.save(product);
        logger.info("Producto creado: {}", savedProduct.getName());
        // Mapear a DTO
        return productMapper.toProductResponse(savedProduct);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<Product> productOptional = productRepository.findByIdAndIsActiveTrue(id);
        if (productOptional.isEmpty()) {
            logger.warn("Intento de eliminar producto no encontrado con ID: {}", id);
            throw new NotFoundException("Producto no encontrado");
        }
        Product product = productOptional.get();

        // Verificar si hay ítems de orden asociados
        try {
            if (orderItemClient.orderItemHasProducts(id)) {
                logger.warn("Intento de eliminar producto con pedidos asociados: {}", product.getName());
                throw new RuntimeException("No se puede eliminar el producto porque tiene pedidos asociados");
            }
        } catch (FeignException e) {
            logger.error("Error al verificar pedidos asociados al producto con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo verificar los pedidos asociados al producto. Intente más tarde.");
        }

        // Marcar producto como inactivo
        product.setIsActive(false);
        logger.info("Producto marcado como inactivo: {}", product.getName());
        productRepository.save(product);
    }

    @Transactional
    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        // Verificar si el producto existe
        Optional<Product> productOptional = productRepository.findByIdAndIsActiveTrue(id);
        if (productOptional.isEmpty()) {
            logger.warn("Intento de actualizar producto no encontrado con ID: {}", id);
            throw new NotFoundException("Producto no encontrado");
        }

        // Verificar si la categoría existe
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(productRequest.getCategoryId());
        if (categoryOptional.isEmpty()) {
            logger.warn("Intento de actualizar producto con categoría no existente: {}", productRequest.getCategoryId());
            throw new BadRequestException("Categoría no encontrada");
        }

        // Actualizar los campos del producto
        Product product = productMapper.updateProduct(productOptional.get(), productRequest);
        product.setCategory(categoryOptional.get());
        // Guardar el producto actualizado en la base de datos
        Product updatedProduct = productRepository.save(product);
        // Mapear a DTO
        logger.info("Producto actualizado: {}", updatedProduct.getName());
        return productMapper.toProductResponse(updatedProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse findById(Long id) {
        // Verificar si el producto existe
        Optional<Product> productOptional = productRepository.findByIdAndIsActiveTrue(id);
        if (productOptional.isEmpty()) {
            logger.warn("Intento de búsqueda de producto no encontrado con ID: {}", id);
            throw new NotFoundException("Producto no encontrado");
        }
        // Mapear a DTO
        logger.info("Producto encontrado con ID: {}", id);
        return productMapper.toProductResponse(productOptional.get());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponse> findAll() {
        // Obtener todos los productos activos
        List<Product> products = productRepository.findAllByIsActiveTrue();
        if (products.isEmpty()) {
            logger.warn("No se encontraron productos activos");
            throw new NotFoundException("No se encontraron productos activos");
        }
        // Mapear a DTOs
        logger.info("Número de productos activos encontrados: {}", products.size());
        return productMapper.toProductResponseList(products);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> findAllPageable(Pageable pageable) {
        // Obtener todos los productos activos con paginación
        Page<Product> productsPage = productRepository.findAllByIsActiveTrue(pageable);
        if (productsPage.isEmpty()) {
            logger.warn("No se encontraron productos activos en la paginación");
            throw new NotFoundException("No se encontraron productos activos");
        }
        // Mapear a DTOs
        logger.info("Número de productos activos encontrados (paginados): {}", productsPage.getTotalElements());
        return productsPage.map(productMapper::toProductResponse);
    }


    @Transactional(readOnly = true)
    @Override
    public List<ProductResponse> findByCategoryId(Long categoryId) {
        // Verificar si la categoría existe
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(categoryId);
        if (categoryOptional.isEmpty()) {
            logger.warn("Intento de búsqueda de productos por categoría no encontrada con ID: {}", categoryId);
            throw new NotFoundException("Categoría no encontrada");
        }
        // Obtener productos por ID de categoría
        List<Product> products = productRepository.findByCategoryIdAndIsActiveTrue(categoryId);
        // Mapear a DTOs
        logger.info("Número de productos encontrados para la categoría con ID {}: {}", categoryId, products.size());
        // Mapear a DTOs
        return productMapper.toProductResponseList(products);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> findByCategoryId(Long categoryId, Pageable pageable) {
        // Verificar si la categoría existe
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsActiveTrue(categoryId);
        if (categoryOptional.isEmpty()) {
            logger.warn("Intento de búsqueda de productos por categoría no encontrada con ID: {}", categoryId);
            throw new NotFoundException("Categoría no encontrada");
        }
        // Obtener productos por ID de categoría con paginación
        Page<Product> productsPage = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        // Mapear a DTOs
        logger.info("Número de productos encontrados para la categoría con ID {} (paginados): {}", categoryId, productsPage.getTotalElements());
        return productsPage.map(productMapper::toProductResponse);
    }

    @Transactional
    @Override
    public void updateStock(List<StockRequest> requests) {
        for (StockRequest request : requests) {
            Optional<Product> productOptional = productRepository.findByIdAndIsActiveTrue(request.getProductId());
            if (productOptional.isEmpty()) {
                logger.warn("Intento de actualización de stock para producto no encontrado con ID: {}", request.getProductId());
                throw new NotFoundException("Producto no encontrado");
            }

            Product product = productOptional.get();
            int currentStock = product.getStock();
            int quantityToAdjust = request.getQuantity();

            if (currentStock + quantityToAdjust < 0) {
                logger.warn("Stock insuficiente para el producto con ID: {}", request.getProductId());
                throw new BadRequestException("Stock insuficiente para el producto con ID: " + request.getProductId());
            }

            // Actualizar el stock del producto
            product.setStock(currentStock + quantityToAdjust);
            // Guardar el producto actualizado en la base de datos
            logger.info("Stock actualizado para el producto con ID: {}", request.getProductId());
            productRepository.save(product);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean validateStock(List<StockRequest> requests) {
        for (StockRequest request : requests) {
            Optional<Product> productOptional = productRepository.findByIdAndIsActiveTrue(request.getProductId());
            if (productOptional.isEmpty()) {
                logger.warn("Intento de validación de stock para producto no encontrado con ID: {}", request.getProductId());
                throw new NotFoundException("Producto no encontrado");
            }

            Product product = productOptional.get();
            int currentStock = product.getStock();
            int quantityToAdjust = request.getQuantity();

            if (currentStock + quantityToAdjust < 0) {
                logger.warn("Stock insuficiente para el producto con ID: {}", request.getProductId());
                return false; // Stock insuficiente
            }
        }
        return true; // Stock suficiente para todos los productos
    }
}

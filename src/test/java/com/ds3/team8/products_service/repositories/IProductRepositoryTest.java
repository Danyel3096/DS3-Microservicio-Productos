package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Category;
import com.ds3.team8.products_service.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IProductRepositoryTest {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();  // Limpia la base de datos antes de cada prueba
        categoryRepository.deleteAll();

        // Crear y guardar una categoría antes de cada prueba
        category = categoryRepository.save(new Category(null, "Bebidas", true, new ArrayList<>()));

    }

    @Test
    void testSaveProduct() {
        // Crear y guardar un producto
        Product product = new Product(null, "Café", "Café colombiano", BigDecimal.valueOf(5000.0), 100, true, category);
        Product savedProduct = productRepository.save(product);

        // Verificar que se guardó correctamente
        assertNotNull(savedProduct.getId());
        assertEquals("Café", savedProduct.getName());
        assertTrue(savedProduct.getIsActive());
    }

    @Test
    void testUpdateProduct() {
        Product product = productRepository.save(new Product(null, "Té", "Té verde", BigDecimal.valueOf(3000.0), 50, true, category));

        product.setPrice(BigDecimal.valueOf(3500.0));
        product.setStock(70);
        Product updated = productRepository.save(product);

        assertEquals(BigDecimal.valueOf(3500.0), updated.getPrice());
        assertEquals(70, updated.getStock());
    }

    @Test
    void testDeleteProduct() {
        Product product = productRepository.save(new Product(null, "Jugo", "Jugo de naranja", BigDecimal.valueOf(2500.0), 80, true, category));

        productRepository.delete(product);
        Optional<Product> deleted = productRepository.findById(product.getId());

        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindById() {
        // Guardar un producto
        Product product = productRepository.save(new Product(null, "Alcohol", "Alcohol colombiano", BigDecimal.valueOf(6000.0), 120, true, category));

        // Buscar por ID
        Optional<Product> foundProduct = productRepository.findById(product.getId());

        // Verificar que el producto se encontró
        assertTrue(foundProduct.isPresent());
        assertEquals("Alcohol", foundProduct.get().getName());
    }

    @Test
    void testFindAll() {
        // Guardar varios productos
        productRepository.save(new Product(null, "Café", "Café colombiano", BigDecimal.valueOf(5000.0), 100, true, category));
        productRepository.save(new Product(null, "Alcohol", "Alcohol colombiano", BigDecimal.valueOf(6000.0), 120, true, category));

        // Obtener todos los productos
        List<Product> products = productRepository.findAll();

        // Verificar que se guardaron 2 productos
        assertEquals(2, products.size());
    }
}

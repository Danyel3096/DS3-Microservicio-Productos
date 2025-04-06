package com.ds3.team8.products_service.repositories;

import com.ds3.team8.products_service.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") // Usa el archivo de configuración de test
public class lCategoryRepositoryTest {
    @Autowired
    private ICategoryRepository categoryRepository;


    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll(); // Limpia la base de datos antes de cada prueba
    }

    @Test
    void testSaveCategory() {
        // Guardar una categoría
        Category category = categoryRepository.save(new Category(null, "Bebidas", true, List.of()));

        // Verificar que se guardó correctamente
        assertNotNull(category.getId());
        assertEquals("Bebidas", category.getName());
        assertTrue(category.getIsActive());
        // Verificar que la categoría se encuentra en la base de datos
        Optional<Category> savedCategory = categoryRepository.findById(category.getId());
        assertTrue(savedCategory.isPresent());
        assertEquals("Bebidas", savedCategory.get().getName());
    }

    @Test
    void testFindByName() {
        // Guardar una categoría
        categoryRepository.save(new Category(null, "Snacks", true, List.of()));

        // Buscar por nombre
        Optional<Category> foundCategory = categoryRepository.findByName("Snacks");

        // Verificar que se encontró correctamente
        assertTrue(foundCategory.isPresent());
        assertEquals("Snacks", foundCategory.get().getName());
    }

    @Test
    void testFindByIsActiveTrue() {
        // Guardar categorías
        categoryRepository.save(new Category(null, "Bebidas", true, List.of()));
        categoryRepository.save(new Category(null, "Snacks", false, List.of()));

        // Buscar solo categorías activas
        List<Category> activeCategories = categoryRepository.findByIsActiveTrue();

        // Verificar que solo se devuelve la activa
        assertEquals(1, activeCategories.size());
        assertEquals("Bebidas", activeCategories.get(0).getName());
    }
}

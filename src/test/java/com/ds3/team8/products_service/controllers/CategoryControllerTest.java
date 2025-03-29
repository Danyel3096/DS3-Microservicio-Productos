package com.ds3.team8.products_service.controllers;

import com.ds3.team8.products_service.dtos.CategoryRequest;
import com.ds3.team8.products_service.dtos.CategoryResponse;
import com.ds3.team8.products_service.services.ICategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CategoryControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AutoCloseable closeable; // Se almacena la referencia para cerrarla después

    @BeforeEach
    void setUp() {
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            closeable = mocks; // Guarda la referencia para cerrarla luego
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar los mocks", e);
        }

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close(); // Cierra los mocks después de cada prueba
        }
    }

    // Se crea una lista simulada de categorías (CategoryResponse),
    // que representa lo que debería devolver el servicio
    @Test
    void testGetAllCategories() throws Exception {
        List<CategoryResponse> categories = Arrays.asList(
                new CategoryResponse(1L, "Electronics", true),
                new CategoryResponse(2L, "Clothing", true)
        );

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Clothing"));

        verify(categoryService, times(1)).findAll();
    }

    // comportamiento del endpoint GET /api/v1/categories/1 en categoría.
    @Test
    void testGetCategoryById() throws Exception {
        CategoryResponse category = new CategoryResponse(1L, "Books", true);

        when(categoryService.findById(1L)).thenReturn(category);

        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Books"))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(categoryService, times(1)).findById(1L);
    }

    // verificar el comportamiento del endpoint POST /api/v1/categories,
    // que se encarga de crear una nueva categoría.

    @Test
    void testCreateCategory() throws Exception {
        CategoryRequest request = new CategoryRequest("Furniture");
        CategoryResponse response = new CategoryResponse(1L, "Furniture", true);

        when(categoryService.save(any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Furniture"))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(categoryService, times(1)).save(any(CategoryRequest.class));
    }

    // verificar el comportamiento del endpoint PUT /api/v1/categories,
    // que se encarga de actualizar una nueva categoría.
    @Test
    void testUpdateCategory() throws Exception {
        CategoryRequest request = new CategoryRequest("Gaming");
        CategoryResponse response = new CategoryResponse(1L, "Gaming", true);

        when(categoryService.update(eq(1L), any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming"))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(categoryService, times(1)).update(eq(1L), any(CategoryRequest.class));
    }

    // verificar el comportamiento del endpoint DELETE /api/v1/categories,
    // que se encarga de eliminar una nueva categoría.
    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).delete(1L);
    }
}

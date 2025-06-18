package com.ds3.team8.products_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ds3.team8.products_service.dtos.ProductRequest;
import com.ds3.team8.products_service.dtos.ProductResponse;
import com.ds3.team8.products_service.services.IProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ProductControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AutoCloseable closeable; // Se almacena la referencia para cerrarla después

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    // Se crea una lista simulada de productos (ProductResponse),
    // que representa lo que debería devolver el servicio
    @Test
    void testGetAllProducts() throws Exception {
        List<ProductResponse> products = Arrays.asList(
                new ProductResponse(1L, "Laptop", "High-end gaming laptop", new BigDecimal("1500.00"), 10, true, null),
                new ProductResponse(2L, "Smartphone", "Latest model smartphone", new BigDecimal("999.99"), 20, true, null)
        );

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Smartphone"));

        verify(productService, times(1)).findAll();
    }

    // comportamiento del endpoint GET /api/v1/products/1 en productos.
    @Test
    void testGetProductById() throws Exception {
        ProductResponse product = new ProductResponse(1L, "Tablet", "High-resolution tablet", new BigDecimal("600.00"), 15, true, null);

        when(productService.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tablet"))
                .andExpect(jsonPath("$.price").value(600.00));

        verify(productService, times(1)).findById(1L);
    }

    /**
     * Prueba el escenario donde un producto con el ID especificado no existe.
     * Simula que el productService lanza una RuntimeException al buscar un ID de producto inexistente (99L).
     */
    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.findById(99L)).thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(get("/api/v1/products/99"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Producto no encontrado"));

        verify(productService, times(1)).findById(99L);
    }

    // verificar el comportamiento del endpoint POST /api/v1/products,
    // que se encarga de crear un nuevo producto.
    @Test
    void testCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Monitor", "4K Monitor", new BigDecimal("400.00"), 30, 1L);
        ProductResponse response = new ProductResponse(1L, "Monitor", "4K Monitor", new BigDecimal("400.00"), 30, true, null);

        when(productService.save(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Monitor"))
                .andExpect(jsonPath("$.price").value(400.00));

        verify(productService, times(1)).save(any(ProductRequest.class));
    }

    /**
     * Verifica que crear un producto con datos inválidos retorna un estado Bad Request (400).
     */
    @Test
    void testCreateProduct_InvalidRequest() throws Exception {
        ProductRequest invalidRequest = new ProductRequest("", "desc", BigDecimal.TEN, 5, 1L);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // verificar el comportamiento del endpoint PUT /api/v1/products,
    // que se encarga de actualizar un nuevo producto.
    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Headphones", "Noise-cancelling headphones", new BigDecimal("250.00"), 25, 1L);
        ProductResponse response = new ProductResponse(1L, "Headphones", "Noise-cancelling headphones", new BigDecimal("250.00"), 25, true, null);

        when(productService.update(eq(1L), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Headphones"))
                .andExpect(jsonPath("$.price").value(250.00));

        verify(productService, times(1)).update(eq(1L), any(ProductRequest.class));
    }

    // verificar el comportamiento del endpoint DELETE /api/v1/categories,
    // que se encarga de eliminar una nueva categoría.
    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(1L);
    }
}

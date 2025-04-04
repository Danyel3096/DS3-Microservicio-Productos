package com.ds3.team8.products_service.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
        // Este test valida que el handler retorne correctamente un 404 Not Found cuando no se encuentra un producto.
    void testHandleProductNotFoundException() {
        Long id = 1L;
        ProductNotFoundException ex = new ProductNotFoundException(id);
        ResponseEntity<Map<String, Object>> response = handler.handleProductNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El producto con ID " + id + " no fue encontrado.", response.getBody().get("error"));
        assertEquals(404, response.getBody().get("status"));
    }

    @Test
        // Este test asegura que el handler gestione correctamente la excepción de categoría no encontrada.
    void testHandleCategoryNotFoundException() {
        Long id = 5L;
        CategoryNotFoundException ex = new CategoryNotFoundException(id);
        ResponseEntity<Map<String, Object>> response = handler.handleCategoryNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("La categoria con ID " + id + " no fue encontrada.", response.getBody().get("error"));
        assertEquals(404, response.getBody().get("status"));
    }

    @Test
        // Este test comprueba que se maneje correctamente el intento de duplicación de una categoría.
    void testHandleCategoryAlreadyExistsException() {
        String nombre = "Categoría ya existe";
        CategoryAlreadyExistsException ex = new CategoryAlreadyExistsException(nombre);
        ResponseEntity<Map<String, Object>> response = handler.handleCategoryAlreadyExistsException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("La categoria con nombre '" + nombre + "' ya existe.", response.getBody().get("error"));
    }

    @Test
        //  Este test valida que no se pueda eliminar una categoría que aún tiene productos relacionados.
    void testHandleCategoryDeletionException() {
        Long id = 3L;
        CategoryDeletionException ex = new CategoryDeletionException(id);
        ResponseEntity<Map<String, Object>> response = handler.handleCategoryDeletionException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("La categoria con ID " + id + " no puede eliminarse porque tiene productos asociados.", response.getBody().get("error"));
        assertEquals(400, response.getBody().get("status"));
    }

    @SuppressWarnings("unchecked")
    @Test
        // Este test simula un error de validación en una petición (como un campo vacío) y verifica que el handler devuelva los errores correctos.
    void testHandleValidationExceptions() {
        // Mock de los errores de validación
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> errors = List.of(new FieldError("producto", "nombre", "El nombre es obligatorio"));
        when(bindingResult.getFieldErrors()).thenReturn(errors);
        @SuppressWarnings("ConstantConditions")
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, String> errorMap = (Map<String, String>) response.getBody().get("errors");
        assertTrue(errorMap.containsKey("nombre"));
        assertEquals("El nombre es obligatorio", errorMap.get("nombre"));
    }

    @Test
        // Este test valida que cualquier excepción de base de datos sea manejada correctamente como error interno del servidor.
    void testHandleDataAccessException() {
        Throwable cause = new RuntimeException("Detalle del error");
        DataAccessException ex = new DataAccessException("Error general", cause) {
        };
        ResponseEntity<Map<String, Object>> response = handler.handleDataAccessException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(((String) response.getBody().get("error")).contains("Detalle del error"));
    }

    @Test
        // Este test asegura que cualquier error inesperado sea manejado y comunicado correctamente como un error del servidor (500).
    void testHandleGeneralException() {
        Exception ex = new Exception("Algo salió mal");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneralException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(((String) response.getBody().get("error")).contains("Algo salió mal"));
    }
}

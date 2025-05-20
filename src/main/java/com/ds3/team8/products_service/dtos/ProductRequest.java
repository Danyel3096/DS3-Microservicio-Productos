package com.ds3.team8.products_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductRequest {
    @NotEmpty(message = "No puede estar vacio")
    @Size(min = 2, max = 20, message = "El tamaño tiene que estar entre 2 y 20")
    private String name;

    @NotEmpty(message = "No puede estar vacio")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private BigDecimal price;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "La categoria no puede ser nulo")
    private Long categoryId;

    @NotEmpty(message = "La URL de la imagen no puede estar vacía")
    private String imageUrl;

}

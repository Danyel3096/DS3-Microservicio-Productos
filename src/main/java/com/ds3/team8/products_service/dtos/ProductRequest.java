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
    @NotEmpty(message = "El campo 'name' es obligatorio")
    @Size(min = 2, max = 20, message = "El campo 'name' debe tener entre 2 y 20 caracteres")
    private String name;

    @NotEmpty(message = "El campo 'description' es obligatorio")
    @Size(max = 255, message = "El campo 'description' no puede exceder los 255 caracteres")
    private String description;

    @NotNull(message = "El campo 'price' es obligatorio")
    @Min(value = 0, message = "El campo 'price' no puede ser negativo")
    private BigDecimal price;

    @NotNull(message = "El campo 'stock' es obligatorio")
    @Min(value = 0, message = "El campo 'stock' no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El campo 'categoryId' es obligatorio")
    private Long categoryId;
}


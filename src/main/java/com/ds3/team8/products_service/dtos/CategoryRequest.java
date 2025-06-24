package com.ds3.team8.products_service.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotEmpty(message = "El campo 'name' es obligatorio")
    @Size(min = 2, max = 20, message = "El campo 'name' debe tener entre 2 y 20 caracteres")
    private String name;

    @NotEmpty(message = "El campo 'description' es obligatorio")
    @Size(min = 2, max = 100, message = "El campo 'description' debe tener entre 2 y 100 caracteres")
    private String description;
}

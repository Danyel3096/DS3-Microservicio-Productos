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
    @NotEmpty(message = "No puede estar vacio")
    @Size(min = 2, max = 20, message = "El tamaño tiene que estar entre 2 y 20")
    private String name;
}

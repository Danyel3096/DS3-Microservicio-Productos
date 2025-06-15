package com.ds3.team8.products_service.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for receiving category creation or update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres.")
    private String name;
}

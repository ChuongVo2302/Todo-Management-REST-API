package com.github.vdc2302.todo.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record TodoCreateDTO(
        @NotBlank 
        String title,
        String description
) {}

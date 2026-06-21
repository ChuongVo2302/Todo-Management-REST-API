package com.github.vdc2302.todo.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record TodoRequestDTO(
        @NotBlank(message = "Title cannot be empty")
        String title,
        String description
) {}

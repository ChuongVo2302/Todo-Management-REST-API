package com.github.vdc2302.todo.dto.Response;

public record TodoResponseDTO(
        Long id,
        String title,
        String description,
        Boolean completed
) {}

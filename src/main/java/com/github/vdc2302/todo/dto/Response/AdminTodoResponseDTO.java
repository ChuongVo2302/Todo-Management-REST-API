package com.github.vdc2302.todo.dto.Response;

// Like TodoResponseDTO, plus the owner's username so an admin knows whose todo it is
public record AdminTodoResponseDTO(
        Long id,
        String title,
        String description,
        Boolean completed,
        String username
) {}

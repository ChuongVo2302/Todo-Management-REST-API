package com.github.vdc2302.todo.dto.Request;

public record TodoPatchDTO(
        String title,
        String description,
        Boolean completed
) {}




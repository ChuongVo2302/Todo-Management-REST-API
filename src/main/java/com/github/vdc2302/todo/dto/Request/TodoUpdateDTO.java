package com.github.vdc2302.todo.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoUpdateDTO(
        @NotBlank String title,
        @NotNull String description,
        @NotNull Boolean completed 
) {}


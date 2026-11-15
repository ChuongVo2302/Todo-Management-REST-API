package com.github.vdc2302.todo.dto.Request;

import com.github.vdc2302.todo.model.Role;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateDTO(
        @NotNull(message = "Role is required (ROLE_USER or ROLE_ADMIN)")
        Role role
) {}

package com.github.vdc2302.todo.dto.Response;

import com.github.vdc2302.todo.model.Role;

// Never expose the password hash
public record UserResponseDTO(
        Long id,
        String username,
        Role role
) {}

package com.github.vdc2302.todo.mapper;

import com.github.vdc2302.todo.dto.Response.UserResponseDTO;
import com.github.vdc2302.todo.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDto(User user);
}

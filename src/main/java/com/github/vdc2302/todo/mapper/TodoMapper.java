package com.github.vdc2302.todo.mapper;

import com.github.vdc2302.todo.dto.Request.TodoRequestDTO;
import com.github.vdc2302.todo.dto.Response.TodoResponseDTO;
import com.github.vdc2302.todo.model.Todo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo toEntity(TodoRequestDTO request);

    TodoResponseDTO toResponseDto(Todo todo);
}

package com.github.vdc2302.todo.mapper;

import com.github.vdc2302.todo.dto.Request.TodoCreateDTO;
import com.github.vdc2302.todo.dto.Request.TodoPatchDTO;
import com.github.vdc2302.todo.dto.Request.TodoUpdateDTO;
import com.github.vdc2302.todo.dto.Response.TodoResponseDTO;
import com.github.vdc2302.todo.model.Todo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completed", ignore = true)
    @Mapping(target = "user", ignore = true)
    Todo toEntity(TodoCreateDTO dto);

    TodoResponseDTO toResponseDto(Todo todo);

    //put
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void update(TodoUpdateDTO dto, @MappingTarget Todo todo);

    //patch
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void patchEntity(TodoPatchDTO dto, @MappingTarget Todo todo);
}

package com.github.vdc2302.todo.service;

import com.github.vdc2302.todo.dto.Request.TodoCreateDTO;
import com.github.vdc2302.todo.dto.Request.TodoPatchDTO;
import com.github.vdc2302.todo.dto.Request.TodoUpdateDTO;
import com.github.vdc2302.todo.dto.Response.TodoResponseDTO;
import com.github.vdc2302.todo.exception.ResourceNotFoundException;
import com.github.vdc2302.todo.mapper.TodoMapper;
import com.github.vdc2302.todo.model.Todo;
import com.github.vdc2302.todo.model.User;
import com.github.vdc2302.todo.repository.TodoRepository;
import com.github.vdc2302.todo.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final UserRepository userRepository;
  private final TodoMapper todoMapper;

  @Transactional(readOnly = true)
  public List<TodoResponseDTO> getAllTodos(String username){
    return todoRepository.findAllByUserUsername(username).stream().map(todoMapper::toResponseDto).toList();
  }

  @Transactional(readOnly = true)
  public TodoResponseDTO getTodoById(Long id, String username){
    return todoMapper.toResponseDto(getOwnedTodo(id, username));
  }

  private Todo getOwnedTodo(Long id, String username){
    return todoRepository.findByIdAndUserUsername(id, username)
        .orElseThrow(() -> new ResourceNotFoundException("Todo not found with ID: " + id));
  }

  @Transactional 
  public TodoResponseDTO createTodo(TodoCreateDTO requestDTO, String username){
    User currentUser = userRepository.findByUsername(username)
      .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

    Todo todo = todoMapper.toEntity(requestDTO);
    todo.setCompleted(false);
    todo.setUser(currentUser);

    return todoMapper.toResponseDto(todoRepository.save(todo));
  }

  @Transactional
  public TodoResponseDTO updateTodo(Long id, TodoUpdateDTO requestDTO, String username){
    Todo todo = getOwnedTodo(id, username);
    todoMapper.update(requestDTO, todo); // PUT: all fields are validated as present
    return todoMapper.toResponseDto(todo);
  }

  @Transactional
  public TodoResponseDTO patchTodo(Long id, TodoPatchDTO requestDTO, String username){
    Todo todo = getOwnedTodo(id, username);
    todoMapper.patchEntity(requestDTO, todo); // PATCH: null fields are skipped
    return todoMapper.toResponseDto(todo);
  }

  @Transactional
  public void deleteTodo(Long id, String username) {
    todoRepository.delete(getOwnedTodo(id, username));
  }
}

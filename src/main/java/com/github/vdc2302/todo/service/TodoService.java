package com.github.vdc2302.todo.service;

import com.github.vdc2302.todo.dto.Request.TodoRequestDTO;
import com.github.vdc2302.todo.dto.Response.TodoResponseDTO;
import com.github.vdc2302.todo.mapper.TodoMapper;
import com.github.vdc2302.todo.model.Todo;
import com.github.vdc2302.todo.model.User;
import com.github.vdc2302.todo.repository.TodoRepository;
import com.github.vdc2302.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final UserRepository userRepository;
  private final TodoMapper todoMapper;

  public List<TodoResponseDTO> getAllTodos(){
    return todoRepository.findAll().stream().map(todoMapper::toResponseDto).toList();
  }

  public Todo getTodoById(Long id){
    return todoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Todo not found with ID: " + id));
  }

  public TodoResponseDTO createTodo(TodoRequestDTO requestDTO, String username){

    Todo todo = todoMapper.toEntity(requestDTO);
    todo.setCompleted(false);

    User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
    todo.setUser(currentUser);

    Todo savedTodo = todoRepository.save(todo);

    return todoMapper.toResponseDto(savedTodo);
  }

  public TodoResponseDTO updateTodo(Long id, TodoRequestDTO requestDTO){
//    Todo existingTodo = getTodoById(id);
//
//    existingTodo.setTitle(details.getTitle());
//    existingTodo.setDescription(details.getDescription());
//    existingTodo.setCompleted(details.getCompleted());
//
//    return todoRepository.save(existingTodo);

    Todo todo = todoMapper.toEntity(requestDTO);
    Todo existingTodo = getTodoById(id);

    existingTodo.setTitle(todo.getTitle());
    existingTodo.setDescription(todo.getDescription());
    existingTodo.setCompleted(todo.getCompleted());

    Todo savedTodo = todoRepository.save(existingTodo);

    return todoMapper.toResponseDto(savedTodo);
  }

  public void deleteTodo(Long id, String username) {
    //check if that todo is exist
    Todo existingTodo = todoRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Todo not found with ID: " + id));
    todoRepository.deleteById(id);

    //check if that todo belong to the user
    if(!existingTodo.getUser().getUsername().equals(username)){
      throw new AccessDeniedException("Security Alert: You do not have permission.");
    }

    todoRepository.delete(existingTodo);
  }
}

package com.github.vdc2302.todo.controller;

import com.github.vdc2302.todo.dto.Request.TodoRequestDTO;
import com.github.vdc2302.todo.dto.Response.TodoResponseDTO;
import com.github.vdc2302.todo.model.Todo;
import com.github.vdc2302.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/todo")
@RequiredArgsConstructor
public class TodoController {
  private final TodoService service;

  //  get all
  @GetMapping
  public List<TodoResponseDTO> getAll() {
    return service.getAllTodos();
  }

  // get by id
  @GetMapping("{id}")
  public ResponseEntity<Todo> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getTodoById(id));
  }

  //  create new
  @PostMapping("create")
  public ResponseEntity<TodoResponseDTO> create(@Valid @RequestBody TodoRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    TodoResponseDTO response = service.createTodo(requestDTO, username);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  //  update existing
  @PutMapping("{id}")
  public ResponseEntity<TodoResponseDTO> update(@PathVariable Long id, @RequestBody TodoRequestDTO requestDTO) {
    return ResponseEntity.ok(service.updateTodo(id, requestDTO));
  }

  //  delete
  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    String username=  userDetails.getUsername();
    service.deleteTodo(id, username);
    return ResponseEntity.noContent().build();
  }
}

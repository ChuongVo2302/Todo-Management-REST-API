package com.github.vdc2302.todo.controller;

import com.github.vdc2302.todo.dto.Request.TodoCreateDTO;
import com.github.vdc2302.todo.dto.Request.TodoPatchDTO;
import com.github.vdc2302.todo.dto.Request.TodoUpdateDTO;
import com.github.vdc2302.todo.dto.Response.TodoResponseDTO;
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
@RequestMapping("api/v1/todos")
@RequiredArgsConstructor
public class TodoController {
  private final TodoService service;

  //  get all
  @GetMapping
  public List<TodoResponseDTO> getAll(@AuthenticationPrincipal UserDetails user) {
    return service.getAllTodos(user.getUsername());
  }

  // get by id
  @GetMapping("{id}")
  public TodoResponseDTO getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
    return service.getTodoById(id, user.getUsername());
  }

  //  create new
  @PostMapping()
  public ResponseEntity<TodoResponseDTO> create(@Valid @RequestBody TodoCreateDTO requestDTO, @AuthenticationPrincipal UserDetails user) {
    return new ResponseEntity<>(service.createTodo(requestDTO, user.getUsername()), HttpStatus.CREATED);
  }

  //  update existing
  @PutMapping("{id}")
  public TodoResponseDTO update( @PathVariable Long id, @Valid @RequestBody TodoUpdateDTO requestDTO, @AuthenticationPrincipal UserDetails user) {
    return service.updateTodo(id, requestDTO, user.getUsername());
  }

  @PatchMapping("{id}")
  public TodoResponseDTO patch(@PathVariable Long id, @RequestBody TodoPatchDTO requestDTO, @AuthenticationPrincipal UserDetails user){
    return service.patchTodo(id, requestDTO, user.getUsername());
  }

  //  delete
  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
    service.deleteTodo(id, user.getUsername());
    return ResponseEntity.noContent().build();
  }
}

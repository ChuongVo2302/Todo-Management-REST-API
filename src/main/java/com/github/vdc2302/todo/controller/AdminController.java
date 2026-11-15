package com.github.vdc2302.todo.controller;

import com.github.vdc2302.todo.dto.Request.RoleUpdateDTO;
import com.github.vdc2302.todo.dto.Response.AdminTodoResponseDTO;
import com.github.vdc2302.todo.dto.Response.UserResponseDTO;
import com.github.vdc2302.todo.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Only ROLE_ADMIN can reach these endpoints (see SecurityConfig: /api/v1/admin/**)
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService service;

  // ---- Users ----

  @GetMapping("/users")
  public List<UserResponseDTO> getAllUsers() {
    return service.getAllUsers();
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails admin) {
    service.deleteUser(id, admin.getUsername());
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/users/{id}/role")
  public UserResponseDTO updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateDTO requestDTO, @AuthenticationPrincipal UserDetails admin) {
    return service.updateRole(id, requestDTO.role(), admin.getUsername());
  }

  // ---- Todos ----

  // GET /api/v1/admin/todos              -> every todo
  // GET /api/v1/admin/todos?username=abc -> only abc's todos
  @GetMapping("/todos")
  public List<AdminTodoResponseDTO> getAllTodos(@RequestParam(required = false) String username) {
    return service.getAllTodos(username);
  }

  @DeleteMapping("/todos/{id}")
  public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
    service.deleteTodo(id);
    return ResponseEntity.noContent().build();
  }
}

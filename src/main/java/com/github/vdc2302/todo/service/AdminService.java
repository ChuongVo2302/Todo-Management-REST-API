package com.github.vdc2302.todo.service;

import com.github.vdc2302.todo.dto.Response.AdminTodoResponseDTO;
import com.github.vdc2302.todo.dto.Response.UserResponseDTO;
import com.github.vdc2302.todo.exception.BadRequestException;
import com.github.vdc2302.todo.exception.ResourceNotFoundException;
import com.github.vdc2302.todo.mapper.TodoMapper;
import com.github.vdc2302.todo.mapper.UserMapper;
import com.github.vdc2302.todo.model.Role;
import com.github.vdc2302.todo.model.Todo;
import com.github.vdc2302.todo.model.User;
import com.github.vdc2302.todo.repository.TodoRepository;
import com.github.vdc2302.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final UserRepository userRepository;
  private final TodoRepository todoRepository;
  private final UserMapper userMapper;
  private final TodoMapper todoMapper;

  // ---- Users ----

  @Transactional(readOnly = true)
  public List<UserResponseDTO> getAllUsers() {
    return userRepository.findAll(Sort.by("id")).stream().map(userMapper::toResponseDto).toList();
  }

  @Transactional
  public void deleteUser(Long id, String currentAdmin) {
    User user = getUser(id);
    if (user.getUsername().equals(currentAdmin)) {
      throw new BadRequestException("You cannot delete your own account");
    }
    todoRepository.deleteAllByUser(user);
    userRepository.delete(user);
  }

  @Transactional
  public UserResponseDTO updateRole(Long id, Role role, String currentAdmin) {
    User user = getUser(id);
    if (user.getUsername().equals(currentAdmin) && role != Role.ROLE_ADMIN) {
      throw new BadRequestException("You cannot remove your own admin role");
    }
    user.setRole(role);
    return userMapper.toResponseDto(user);
  }

  private User getUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
  }

  // ---- Todos ----

  /** All todos, or only the given user's todos when username is provided. */
  @Transactional(readOnly = true)
  public List<AdminTodoResponseDTO> getAllTodos(String username) {
    List<Todo> todos = (username == null || username.isBlank())
        ? todoRepository.findAllByOrderByIdAsc()
        : todoRepository.findAllWithUserByUserUsernameOrderByIdAsc(username);
    return todos.stream().map(todoMapper::toAdminResponseDto).toList();
  }

  @Transactional
  public void deleteTodo(Long id) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Todo not found with ID: " + id));
    todoRepository.delete(todo);
  }
}

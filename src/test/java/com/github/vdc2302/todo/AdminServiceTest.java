package com.github.vdc2302.todo;

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
import com.github.vdc2302.todo.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

  private static final String ADMIN = "admin";

  @Mock
  private UserRepository userRepository;

  @Mock
  private TodoRepository todoRepository;

  @Spy
  private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Spy
  private TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);

  @InjectMocks
  private AdminService adminService;

  private final User admin = new User(1L, ADMIN, "hash", Role.ROLE_ADMIN);
  private final User alice = new User(2L, "alice", "hash", Role.ROLE_USER);

  @Test
  void testGetAllTodosIncludesOwnerUsername() {
    when(todoRepository.findAllByOrderByIdAsc())
        .thenReturn(List.of(new Todo(10L, "buy milk", null, false, alice)));

    List<AdminTodoResponseDTO> result = adminService.getAllTodos(null);

    assertEquals(1, result.size());
    assertEquals("alice", result.get(0).username());
  }

  @Test
  void testGetAllTodosFilteredByUsername() {
    when(todoRepository.findAllWithUserByUserUsernameOrderByIdAsc("alice")).thenReturn(List.of());

    adminService.getAllTodos("alice");

    verify(todoRepository).findAllWithUserByUserUsernameOrderByIdAsc("alice");
    verify(todoRepository, never()).findAllByOrderByIdAsc();
  }

  @Test
  void testDeleteUserAlsoDeletesTheirTodos() {
    when(userRepository.findById(2L)).thenReturn(Optional.of(alice));

    adminService.deleteUser(2L, ADMIN);

    verify(todoRepository).deleteAllByUser(alice);
    verify(userRepository).delete(alice);
  }

  @Test
  void testAdminCannotDeleteThemselves() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

    assertThrows(BadRequestException.class, () -> adminService.deleteUser(1L, ADMIN));
    verify(userRepository, never()).delete(any());
  }

  @Test
  void testDeleteUnknownUserThrowsNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> adminService.deleteUser(99L, ADMIN));
  }

  @Test
  void testUpdateRole() {
    when(userRepository.findById(2L)).thenReturn(Optional.of(alice));

    UserResponseDTO result = adminService.updateRole(2L, Role.ROLE_ADMIN, ADMIN);

    assertEquals(Role.ROLE_ADMIN, result.role());
    assertEquals(Role.ROLE_ADMIN, alice.getRole());
  }

  @Test
  void testAdminCannotDemoteThemselves() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

    assertThrows(BadRequestException.class, () -> adminService.updateRole(1L, Role.ROLE_USER, ADMIN));
    assertEquals(Role.ROLE_ADMIN, admin.getRole());
  }

  @Test
  void testDeleteUnknownTodoThrowsNotFound() {
    when(todoRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> adminService.deleteTodo(99L));
  }
}

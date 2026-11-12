package com.github.vdc2302.todo;

import com.github.vdc2302.todo.dto.Response.TodoResponseDTO;
import com.github.vdc2302.todo.exception.ResourceNotFoundException;
import com.github.vdc2302.todo.mapper.TodoMapper;
import com.github.vdc2302.todo.model.Todo;
import com.github.vdc2302.todo.repository.TodoRepository;
import com.github.vdc2302.todo.repository.UserRepository;
import com.github.vdc2302.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

  private static final String USERNAME = "user1";

  @Mock
  private TodoRepository todoRepository;

  @Mock
  private UserRepository userRepository;

  @Spy
  private TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);

  @InjectMocks
  private TodoService todoService;

  ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

  @Test
  void testGetTodoById(){
    Todo todo = new Todo(1L, "todo 1", "description for todo 1", false, null);
    when(todoRepository.findByIdAndUserUsername(1L, USERNAME)).thenReturn(Optional.of(todo));

    TodoResponseDTO result = todoService.getTodoById(1L, USERNAME);

    assertEquals(1L, result.id());
    assertEquals("todo 1", result.title());
    assertEquals("description for todo 1", result.description());
    assertEquals(false, result.completed());
    verify(todoRepository).findByIdAndUserUsername(1L, USERNAME);
  }

  @Test
  void shouldThrowExceptionWhenTodoNotFound(){
    Long notFoundId = 123L;
    when(todoRepository.findByIdAndUserUsername(notFoundId, USERNAME)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      todoService.getTodoById(notFoundId, USERNAME);
    });
    verify(todoRepository).findByIdAndUserUsername(notFoundId, USERNAME);
  }

//  @Test
//  void shouldSaveNewTodo(){
//    Todo inputTodo = new Todo();
//    inputTodo.setTitle("Created Todo 1");
//
//    todoService.createTodo(inputTodo);
//
//    verify(todoRepository).save(todoCaptor.capture());
//    Todo savedTodo = todoCaptor.getValue();
//
//    assertEquals("Created Todo 1", savedTodo.getTitle());
//    assertFalse(savedTodo.getCompleted());
//  }
}
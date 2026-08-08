package com.github.vdc2302.todo;

import com.github.vdc2302.todo.model.Todo;
import com.github.vdc2302.todo.repository.TodoRepository;
import com.github.vdc2302.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

  @Mock
  private TodoRepository todoRepository;

  @InjectMocks
  private TodoService todoService;

  ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

  @Test
  void testGetTodoById(){
    Todo todo = new Todo(1L, "todo 1", "description for todo 1", false, null);
    when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

    Todo result = todoService.getTodoById(1L);

    assertEquals("todo 1", result.getTitle());
    assertEquals("description for todo 1", result.getDescription());
    assertEquals(false, result.getCompleted());
    verify(todoRepository).findById(1L);
  }

  @Test
  void shouldThrowExceptionWhenTodoNotFound(){
    Long notFoundId = 123L;
    when(todoRepository.findById(notFoundId)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      todoService.getTodoById(notFoundId);
    });
    verify(todoRepository).findById(notFoundId);
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
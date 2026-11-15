package com.github.vdc2302.todo;

import com.github.vdc2302.todo.config.SecurityConfig;
import com.github.vdc2302.todo.config.SecurityErrorHandler;
import com.github.vdc2302.todo.controller.TodoController;
import com.github.vdc2302.todo.jwt.JwtService;
import com.github.vdc2302.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@Import({SecurityConfig.class, SecurityErrorHandler.class})
public class TodoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TodoService todoService;

  @MockitoBean
  private JwtService jwtService;

  @MockitoBean
  private UserDetailsService userDetailsService;

  @Test
  @WithMockUser(username = "user1")
  void shouldReturnAllTodos() throws Exception {
    when(todoService.getAllTodos("user1")).thenReturn(List.of());

    mockMvc.perform(get("/api/v1/todos"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}

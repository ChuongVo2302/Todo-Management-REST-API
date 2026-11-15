package com.github.vdc2302.todo;

import com.github.vdc2302.todo.config.SecurityConfig;
import com.github.vdc2302.todo.config.SecurityErrorHandler;
import com.github.vdc2302.todo.controller.AdminController;
import com.github.vdc2302.todo.jwt.JwtService;
import com.github.vdc2302.todo.service.AdminService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Web layer only (no database): checks that /api/v1/admin/** is restricted to ROLE_ADMIN
// and that security errors come back as JSON with a clear message.
@WebMvcTest(AdminController.class)
@Import({SecurityConfig.class, SecurityErrorHandler.class})
public class AdminControllerSecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AdminService adminService;

  // Needed by JwtAuthenticationFilter / SecurityConfig
  @MockitoBean
  private JwtService jwtService;

  @MockitoBean
  private UserDetailsService userDetailsService;

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void adminCanListUsers() throws Exception {
    when(adminService.getAllUsers()).thenReturn(List.of());

    mockMvc.perform(get("/api/v1/admin/users"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "alice", roles = "USER")
  void normalUserGets403WithMessage() throws Exception {
    mockMvc.perform(get("/api/v1/admin/users"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.detail").value("You do not have permission to access this resource."));
  }

  @Test
  void missingTokenGets401WithMessage() throws Exception {
    mockMvc.perform(get("/api/v1/admin/users"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.detail").exists());
  }

  @Test
  void expiredTokenGets401WithMessage() throws Exception {
    when(jwtService.extractUsername("expired-token"))
        .thenThrow(new ExpiredJwtException(null, null, "expired"));

    mockMvc.perform(get("/api/v1/admin/users").header("Authorization", "Bearer expired-token"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.detail").value("Token has expired. Please log in again."));
  }

  @Test
  void malformedTokenGets401WithMessage() throws Exception {
    when(jwtService.extractUsername("garbage"))
        .thenThrow(new MalformedJwtException("bad"));

    mockMvc.perform(get("/api/v1/admin/users").header("Authorization", "Bearer garbage"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.detail").value("Invalid token."));
  }
}

package com.github.vdc2302.todo.controller;

import com.github.vdc2302.todo.jwt.JwtService;
import com.github.vdc2302.todo.model.Role;
import com.github.vdc2302.todo.model.User;
import com.github.vdc2302.todo.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  //register
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request){
    if(userRepository.findByUsername(request.getUsername()).isPresent()){
      return ResponseEntity.badRequest().build();
    }

    User user = new User();
    user.setUsername(request.getUsername());

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ROLE_USER);
    userRepository.save(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
    String token = jwtService.generateToken(userDetails);

    return ResponseEntity.ok(new AuthResponse(token));
  }

  //login
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
    authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword()));

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    String token = jwtService.generateToken(userDetails);

    return ResponseEntity.ok(new AuthResponse(token));
  }

  //DTO
  @Data
  public static class AuthRequest{
    private String username;
    private String password;
  }

  @Data
  public static class AuthResponse{
    private final String token;
  }
}
package com.github.vdc2302.todo.config;

import com.github.vdc2302.todo.model.Role;
import com.github.vdc2302.todo.model.User;
import com.github.vdc2302.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Creates the first admin account on startup from ADMIN_USERNAME / ADMIN_PASSWORD.
 * Does nothing if the variables are empty or the user already exists.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.admin.username:}")
  private String adminUsername;

  @Value("${app.admin.password:}")
  private String adminPassword;

  @Override
  public void run(String... args) {
    if (adminUsername.isBlank() || adminPassword.isBlank()) {
      log.warn("ADMIN_USERNAME / ADMIN_PASSWORD not set - skipping admin account creation");
      return;
    }

    if (userRepository.findByUsername(adminUsername).isPresent()) {
      log.info("Admin account '{}' already exists", adminUsername);
      return;
    }

    User admin = new User();
    admin.setUsername(adminUsername);
    admin.setPassword(passwordEncoder.encode(adminPassword));
    admin.setRole(Role.ROLE_ADMIN);
    userRepository.save(admin);

    log.info("Admin account '{}' created", adminUsername);
  }
}

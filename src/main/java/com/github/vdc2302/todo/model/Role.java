package com.github.vdc2302.todo.model;

// Stored as text in the DB. The "ROLE_" prefix is what Spring Security's hasRole("ADMIN") expects.
public enum Role {
  ROLE_USER,
  ROLE_ADMIN
}

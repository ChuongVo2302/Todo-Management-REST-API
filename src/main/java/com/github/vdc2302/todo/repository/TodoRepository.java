package com.github.vdc2302.todo.repository;

import com.github.vdc2302.todo.model.Todo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserUsername(String username);
    Optional<Todo> findByIdAndUserUsername(Long id, String username);
}

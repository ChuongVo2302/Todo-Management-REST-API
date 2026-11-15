package com.github.vdc2302.todo.repository;

import com.github.vdc2302.todo.model.Todo;
import com.github.vdc2302.todo.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserUsername(String username);
    Optional<Todo> findByIdAndUserUsername(Long id, String username);

    // Admin: all todos, loading each owner in the same query (avoids N+1 queries)
    @EntityGraph(attributePaths = "user")
    List<Todo> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "user")
    List<Todo> findAllWithUserByUserUsernameOrderByIdAsc(String username);

    // Admin: remove a user's todos before deleting the user (user_id foreign key)
    void deleteAllByUser(User user);
}

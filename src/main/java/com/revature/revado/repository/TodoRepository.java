package com.revature.revado.repository;

import com.revature.revado.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Repository
public interface TodoRepository extends JpaRepository<TodoItem, UUID> {
    List<TodoItem> findByAssignedToId(Long userId);

    List<TodoItem> findByStatus(TodoItem.ItemStatus status);

    Optional<TodoItem> findById(Long id);

    boolean existsById(Long todoId);

    void deleteById(Long todoId);
}

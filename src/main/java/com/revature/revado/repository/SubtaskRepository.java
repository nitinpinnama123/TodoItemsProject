package com.revature.revado.repository;

import com.revature.revado.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

    List<Subtask> findByTodoId(Long todoId);

    Optional<Subtask> findByIdAndTodoId(Long id, Long todoId);

    void deleteByIdAndTodoId(Long id, Long todoId);
}

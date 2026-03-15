package com.revature.revado.service;

import com.revature.revado.dto.SubtaskRequest;
import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.repository.SubtaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Service
@RequiredArgsConstructor
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TodoItemService todoService;


    @Transactional
    public List<Subtask> getTodoSubtasks(Long todoItemId) {
        // Verify user owns the todo
        todoService.getTodoItemById(todoItemId);
        return subtaskRepository.findByTodoItemId(todoItemId);
    }

    @Transactional
    public Subtask getSubtaskById(Long subtaskId, Long todoItemId) {
        // Verify user owns the todo
        todoService.getTodoItemById(todoItemId);

        return subtaskRepository.findByIdAndTodoItemId(subtaskId, todoItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id: " + subtaskId));
    }

    @Transactional
    public Subtask createSubtask(Long todoItemId, SubtaskRequest request) {
        TodoItem todo = todoService.getTodoItemById(todoItemId);

        Subtask subtask = new Subtask();
        subtask.setTitle(request.getTitle());
        subtask.setCompleted(request.getCompleted() != null ? request.getCompleted() : false);
        subtask.setCreatedAt(LocalDateTime.now());
        subtask.setUpdatedAt(LocalDateTime.now());
        subtask.setTodoItem(todo);

        return subtaskRepository.save(subtask);
    }

    @Transactional
    public Subtask updateSubtask(Long subtaskId, Long todoItemId, SubtaskRequest request) {
        Subtask subtask = getSubtaskById(subtaskId, todoItemId);

        if (request.getTitle() != null) {
            subtask.setTitle(request.getTitle());
        }
        if (request.getCompleted() != null) {
            subtask.setCompleted(request.getCompleted());
        }

        return subtaskRepository.save(subtask);
    }

    @Transactional
    public Subtask toggleSubtaskComplete(Long subtaskId, Long todoItemId) {
        Subtask subtask = getSubtaskById(subtaskId, todoItemId);
        subtask.setCompleted(!subtask.isCompleted());
        return subtaskRepository.save(subtask);
    }

    @Transactional
    public void deleteSubtask(Long subtaskId, Long todoItemId) {
        Subtask subtask = getSubtaskById(subtaskId, todoItemId);
        subtaskRepository.delete(subtask);
    }
}
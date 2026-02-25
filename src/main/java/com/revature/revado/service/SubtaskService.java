package com.revature.revado.service;

import com.revature.revado.dto.SubtaskRequest;
import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.repository.SubtaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<Subtask> getTodoSubtasks(Long todoId) {
        // Verify user owns the todo
        todoService.getTodoItemById(todoId);
        return subtaskRepository.findByTodoId(todoId);
    }

    @Transactional
    public Subtask getSubtaskById(Long subtaskId, Long todoId) {
        // Verify user owns the todo
        todoService.getTodoItemById(todoId);

        return subtaskRepository.findByIdAndTodoId(subtaskId, todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id: " + subtaskId));
    }

    @Transactional
    public Subtask createSubtask(Long todoId, SubtaskRequest request) {
        TodoItem todo = todoService.getTodoItemById(todoId);

        Subtask subtask = new Subtask();
        subtask.setTitle(request.getTitle());
        subtask.setCompleted(request.getCompleted() != null ? request.getCompleted() : false);
        subtask.setTodo(todo);

        return subtaskRepository.save(subtask);
    }

    @Transactional
    public Subtask updateSubtask(Long subtaskId, Long todoId, SubtaskRequest request) {
        Subtask subtask = getSubtaskById(subtaskId, todoId);

        if (request.getTitle() != null) {
            subtask.setTitle(request.getTitle());
        }
        if (request.getCompleted() != null) {
            subtask.setCompleted(request.getCompleted());
        }

        return subtaskRepository.save(subtask);
    }

    @Transactional
    public Subtask toggleSubtaskComplete(Long subtaskId, Long todoId) {
        Subtask subtask = getSubtaskById(subtaskId, todoId);
        subtask.setCompleted(!subtask.isCompleted());
        return subtaskRepository.save(subtask);
    }

    @Transactional
    public void deleteSubtask(Long subtaskId, Long todoId) {
        Subtask subtask = getSubtaskById(subtaskId, todoId);
        subtaskRepository.delete(subtask);
    }
}
package com.revature.revado.controller;

import com.revature.revado.dto.SubtaskRequest;
import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.repository.SubtaskRepository;
import com.revature.revado.service.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author $ {USER}
 **/

@RestController
@RequestMapping("/api/items/{todo_id}/subtasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SubtaskController {
    private final SubtaskService subtaskService;

    @GetMapping
    public ResponseEntity<List<Subtask>> getTodoItemSubtasks(@PathVariable Long todoId) {
        List<Subtask> subtasks = subtaskService.getTodoSubtasks(todoId);
        return ResponseEntity.ok(subtasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getSubtaskById(@PathVariable Long todoId, @PathVariable Long subtaskId) {
        Subtask subtask = subtaskService.getSubtaskById(subtaskId, todoId);
        return ResponseEntity.ok(subtask);
    }

    @PostMapping
    public ResponseEntity<Subtask> createSubtask(Long todoId, SubtaskRequest request) {
        Subtask subtask = subtaskService.createSubtask(todoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subtask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subtask> updateSubtask(@PathVariable Long todoId, @PathVariable Long subtaskId, SubtaskRequest request) {
        Subtask subtask = subtaskService.updateSubtask(subtaskId, todoId, request);
        return ResponseEntity.ok(subtask);
    }

    @PatchMapping
    public ResponseEntity<Subtask> toggleSubtaskComplete(@PathVariable Long subtaskId, @PathVariable Long todoId) {
        Subtask subtask = subtaskService.toggleSubtaskComplete(subtaskId, todoId);
        return ResponseEntity.ok(subtask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long todoId, @PathVariable Long subtaskId) {
        subtaskService.deleteSubtask(subtaskId, todoId);
        return ResponseEntity.noContent().build();
    }


}

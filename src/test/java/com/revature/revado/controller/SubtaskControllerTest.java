package com.revature.revado.controller;

import com.revature.revado.dto.SubtaskRequest;
import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.repository.SubtaskRepository;
import com.revature.revado.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author $ {USER}
 **/
@SpringBootTest
@Transactional
class SubtaskControllerTest {

    @Autowired
    private SubtaskController subtaskController;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    private TodoItem createTodo() {
        TodoItem todo = new TodoItem();
        todo.setTitle("Test Todo");
        return todoRepository.save(todo);
    }

    private Subtask createSubtask(TodoItem todo) {
        Subtask subtask = new Subtask();
        subtask.setTitle("Test Subtask");
        subtask.setCompleted(false);
        subtask.setTodo(todo);
        return subtaskRepository.save(subtask);
    }


    @Test
    void getAllSubtasks_ShouldReturnList() {

        TodoItem todo = createTodo();
        createSubtask(todo);

        ResponseEntity<List<Subtask>> response =
                subtaskController.getTodoItemSubtasks(todo.getId());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getSubtaskById_ShouldReturnSubtask() {

        TodoItem todo = createTodo();
        Subtask subtask = createSubtask(todo);

        ResponseEntity<Subtask> response =
                subtaskController.getSubtaskById(todo.getId(), subtask.getId());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(subtask.getTitle(), response.getBody().getTitle());
    }

    @Test
    void createSubtask_ShouldCreateSubtask() {

        TodoItem todo = createTodo();

        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("New Subtask");
        request.setCompleted(false);

        ResponseEntity<Subtask> response =
                subtaskController.createSubtask(todo.getId(), request);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody().getId());
    }

    // --------------------------
    // UPDATE
    // --------------------------
    @Test
    void updateSubtask_ShouldUpdateSubtask() {

        TodoItem todo = createTodo();
        Subtask subtask = createSubtask(todo);

        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("Updated Title");
        request.setCompleted(true);

        ResponseEntity<Subtask> response =
                subtaskController.updateSubtask(
                        todo.getId(),
                        subtask.getId(),
                        request
                );

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Updated Title", response.getBody().getTitle());
    }

    // --------------------------
    // TOGGLE
    // --------------------------
    @Test
    void toggleSubtask_ShouldToggleCompletion() {

        TodoItem todo = createTodo();
        Subtask subtask = createSubtask(todo);

        ResponseEntity<Subtask> response =
                subtaskController.toggleSubtaskComplete(
                        todo.getId(),
                        subtask.getId()
                );

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isCompleted());
    }

    @Test
    void deleteSubtask_ShouldDeleteSubtask() {

        TodoItem todo = createTodo();
        Subtask subtask = createSubtask(todo);

        ResponseEntity<Void> response =
                subtaskController.deleteSubtask(
                        todo.getId(),
                        subtask.getId()
                );

        assertEquals(204, response.getStatusCode().value());

        assertFalse(
                subtaskRepository.findById(subtask.getId()).isPresent()
        );
    }
}
package com.revature.revado.controller;

import com.revature.revado.dto.TodoItemRequest;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import com.revature.revado.repository.TodoRepository;
import com.revature.revado.repository.UserRepository;
import com.revature.revado.service.TodoItemService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $ {USER}
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodoItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TodoItemController todoItemController;

    @Autowired
    private TodoItemService todoItemService;

    @Autowired
    private UserRepository userRepo;

    private TodoItemRequest request;

    @Autowired
    private TodoRepository todoRepo;

    @Test
    void createTodoItem_ShouldPersistItem() {
        request = new TodoItemRequest();
        request.setDescription("Task 1");
        request.setTitle("Task 1 Title");
        request.setStatus(TodoItem.ItemStatus.PENDING);
        TodoItem created = todoItemController.createTodoItem(request);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Task 1", created.getDescription());
    }

    @Test
    void getAllTodoItems_ShouldReturnList() {
        request = new TodoItemRequest();
        request.setDescription("Task 1");
        request.setTitle("Task 1 Title");
        todoItemController.createTodoItem(request);

        List<TodoItem> items = todoItemController.getAllTodoItems();

        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals("Task 1 Title", items.get(0).getTitle());
    }

    @Test
    void getTodoItemById_WhenExists_ShouldReturnItem() {
        request = new TodoItemRequest();
        request.setDescription("Task 1");
        request.setTitle("Task 1 Title");
        TodoItem created = todoItemController.createTodoItem(request);

        TodoItem found = todoItemController.getTodoItemById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    void updateTodoItem_ShouldUpdateItem() {
        User user1 = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        User savedUser = userRepo.save(user1);
        request = new TodoItemRequest();
        request.setDescription("Task 1");
        request.setTitle("Task 1 Title");
        TodoItem created = todoItemController.createTodoItem(request);

        TodoItemRequest newReq = new TodoItemRequest();
        newReq.setTitle("Updated title");
        newReq.setStatus(TodoItem.ItemStatus.COMPLETED);
        newReq.setAssignedToId(savedUser.getId());

        ResponseEntity<TodoItem> updated = todoItemController.updateTodoItem(created.getId(), newReq);

        //TodoItem updated = todoItemController.getTodoItemById(created.getId());

        assertEquals(TodoItem.ItemStatus.COMPLETED, updated.getBody().getStatus());
        assertEquals(savedUser.getId(), updated.getBody().getAssignedTo().getId());
    }

    @Test
    void test_update() throws Exception {
        User existingUser = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        User userSaved = userRepo.save(existingUser);

        TodoItem existingTodoItem = new TodoItem(null, "title", "description", TodoItem.ItemStatus.PENDING, null, null, LocalDateTime.now(), LocalDateTime.now());
        TodoItem saved = todoRepo.save(existingTodoItem);

        TodoItemRequest update_req = new TodoItemRequest("updated title", "description", TodoItem.ItemStatus.PENDING, null, userSaved.getId());


        mockMvc.perform(put("/api/items/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update_req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedTo.id", is(userSaved.getId().intValue())))
                .andExpect(jsonPath("$.title", is("updated title")));
    }

    @Test
    void deleteTodoItem_ShouldRemoveItem() {
        request = new TodoItemRequest();
        request.setDescription("Task 1");
        request.setTitle("Task 1 Title");
        TodoItem created = todoItemController.createTodoItem(request);

        todoItemController.deleteTodoItem(created.getId());


        TodoItem deleted = todoItemController.getTodoItemById(created.getId());
        assertNull(deleted.getId());
    }

}

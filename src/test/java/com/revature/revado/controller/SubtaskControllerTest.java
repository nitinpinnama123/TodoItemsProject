package com.revature.revado.controller;

import com.revature.revado.dto.SubtaskRequest;
import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import com.revature.revado.repository.SubtaskRepository;
import com.revature.revado.repository.TodoRepository;
import com.revature.revado.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author $ {USER}
 **/
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class SubtaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubtaskController subtaskController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    @Test
    public void test_createSubtask() throws Exception {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        //Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        //Subtask savedSubtask = subtaskRepository.save(subtask);

        SubtaskRequest request = new SubtaskRequest("Subtask 2", false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + savedTask1.getId() + "/subtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("Subtask 2")));
        //.andExpect(MockMvcResultMatchers.jsonPath("$.lName", is("Doe")))
        //.andExpect(MockMvcResultMatchers.jsonPath("$.email", is("john.doe@example.com")));
    }


    @Test
    void getAllSubtasks_ShouldReturnList() throws Exception {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/items/" + savedTask1.getId() + "/subtasks"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Subtask 1")));

    }

    @Test
    void getSubtaskById_ShouldReturnSubtask() throws Exception {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/items/" + savedTask1.getId() + "/subtasks/" + savedSubtask.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Subtask 1")));

    }


    // --------------------------
    // UPDATE
    // --------------------------
    @Test
    void updateSubtask_ShouldUpdateSubtask() throws Exception {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("Updated Title");
        request.setCompleted(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/items/" + savedTask1.getId() + "/subtasks/" + savedSubtask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Updated Title")));

    }


    // --------------------------
    // TOGGLE
    // --------------------------
    @Test
    void toggleSubtask_ShouldToggleCompletion() throws Exception {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/items/" + savedTask1.getId() + "/subtasks/" + savedSubtask.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("completed", is(true)));
    }

    @Test
    void deleteSubtask_ShouldDeleteSubtask() throws Exception {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/items/" + savedTask1.getId() + "/subtasks/" + savedSubtask.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/items/" + savedTask1.getId() + "/subtasks/" + savedSubtask.getId()))
                .andExpect(status().isNotFound());
    }


}
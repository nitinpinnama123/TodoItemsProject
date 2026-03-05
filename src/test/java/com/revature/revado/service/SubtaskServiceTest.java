package com.revature.revado.service;

import com.revature.revado.dto.SubtaskRequest;
import com.revature.revado.dto.TodoItemRequest;
import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import com.revature.revado.repository.SubtaskRepository;
import com.revature.revado.repository.TodoRepository;
import com.revature.revado.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $ {USER}
 **/
@SpringBootTest
public class SubtaskServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubtaskService subtaskService;
    @Autowired
    private SubtaskRepository subtaskRepository;

    @Autowired
    private TodoRepository todoItemRepository;


    @Test
    void findAll_ShouldReturnAllSubtasks() {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null, null);
        TodoItem savedTask1 = todoItemRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        Subtask subtask2 = new Subtask(null, "Subtask 2", false, savedTask1, null, null);
        Subtask savedSubtask2 = subtaskRepository.save(subtask2);

        List<Subtask> subtasks = subtaskService.getTodoSubtasks(savedTask1.getId());
        assertEquals(2, subtasks.size());
        assertEquals("Subtask 2", subtasks.get(1).getTitle());


    }

    @Test
    void createSubtask_ShouldSaveSubtask() {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null, null);
        TodoItem savedTask1 = todoItemRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        //Subtask subtask2 = new Subtask(null, "Subtask 2", false, savedTask1, null, null);
        SubtaskRequest request = new SubtaskRequest("Subtask 2", false);

        Subtask result =
                subtaskService.createSubtask(savedTask1.getId(), request);

        assertNotNull(result.getId());
        assertEquals("Subtask 2", result.getTitle());
        assertFalse(result.isCompleted());
    }

    @Test
    void updateSubtask_ShouldUpdateFields() {

        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null, null);
        TodoItem savedTask1 = todoItemRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        SubtaskRequest request = new SubtaskRequest("Updated Title", true);

        Subtask updated =
                subtaskService.updateSubtask(savedSubtask.getId(), savedTask1.getId(), request);

        assertEquals("Updated Title", updated.getTitle());
        assertTrue(updated.isCompleted());
    }

    @Test
    void toggleSubtaskComplete_ShouldFlipCompletionStatus() {

        User user1 = new User(null, "John", "Doe", "john-doe", "pwd", "john.doe@example.com", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null, null);
        TodoItem savedTask1 = todoItemRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        Subtask toggled =
                subtaskService.toggleSubtaskComplete(savedSubtask.getId(), savedTask1.getId());

        assertTrue(toggled.isCompleted());
    }

    @Test
    void deleteSubtask_ShouldRemoveSubtask() {

        User user1 = new User(null, "John", "Doe", "john-doe", "pwd", "john.doe@example.com", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null ,null);
        TodoItem savedTask1 = todoItemRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        subtaskService.deleteSubtask(savedSubtask.getId(), savedTask1.getId());

        boolean exists =
                subtaskRepository.findById(savedSubtask.getId()).isPresent();

        assertFalse(exists);
    }



}

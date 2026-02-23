package com.revature.revado.repository;

import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.autoconfigure.container.ContainerImageMetadata.isPresent;

/**
 * @author $ {USER}
 **/
@SpringBootTest
public class TodoItemRepoTest {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private TodoItem todoItem1;
    private TodoItem todoItem2;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
        userRepository.deleteAll();

        user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");
        user1.setUsername("john-doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("pwd");

        user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");
        user2.setUsername("jane-smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("pwd2");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        todoItem1 = new TodoItem();
        todoItem1.setTitle("Task 1");
        todoItem1.setDescription("Description 1");
        todoItem1.setStatus(TodoItem.ItemStatus.PENDING);
        todoItem1.setAssignedTo(user1);

        todoItem2 = new TodoItem();
        todoItem2.setTitle("Task 2");
        todoItem2.setDescription("Description 2");
        todoItem2.setStatus(TodoItem.ItemStatus.IN_PROGRESS);
        todoItem2.setAssignedTo(user2);

        /*todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);*/


    }

    @Test
    void findById_WhenTaskExists_ShouldReturnTask() {

        TodoItem savedTask = todoRepository.save(todoItem1);
        Optional<TodoItem> foundTask = todoRepository.findById(savedTask.getId());

        assertTrue(foundTask.isPresent());
        assertEquals("Task 1", foundTask.get().getTitle());
    }

    @Test
    void findAll_ShouldReturnAllTasks() {
        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);

        List<TodoItem> tasks = todoRepository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    void findByAssignedToId_ShouldReturnUserTasks() {
        User savedUser = userRepository.save(user1);
        todoItem1.setAssignedTo(savedUser);
        todoItem2.setAssignedTo(savedUser);

        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);

        List<TodoItem> todoItems = todoRepository.findByAssignedToId(savedUser.getId());
        assertEquals(2, todoItems.size());
        assertTrue(todoItems.stream().allMatch(t -> t.getAssignedTo().getId().equals(savedUser.getId())));

    }

    @Test
    void findByAssignedToId_WhenNoTasksAssigned_ShouldReturnEmpty() {
        User savedUser = userRepository.save(user1);

        List<TodoItem> todoItems = todoRepository.findByAssignedToId(savedUser.getId());
        assertTrue(todoItems.isEmpty());
    }

    @Test
    void findByStatus_ShouldReturnTasksWithStatus() {
        User savedUser = userRepository.save(user1);
        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);
        TodoItem todoItem3 = new TodoItem();
        todoItem3.setTitle("Task 3");
        todoItem3.setStatus(TodoItem.ItemStatus.PENDING);
        todoItem3.setAssignedTo(user1);
        todoRepository.save(todoItem3);

        List<TodoItem> inProgressTasks = todoRepository.findByStatus(TodoItem.ItemStatus.IN_PROGRESS);

        assertEquals(1, inProgressTasks.size());
        assertTrue(inProgressTasks.stream().allMatch(t -> t.getStatus() == TodoItem.ItemStatus.IN_PROGRESS));
    }

    @Test
    void deleteTask_ShouldRemoveTask() {
        TodoItem savedTask = todoRepository.save(todoItem1);
        Long taskId = savedTask.getId();

        todoRepository.delete(savedTask);
        Optional<TodoItem> deletedTask = todoRepository.findById(taskId);
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void updateTask_ShouldModifyTask() {
        TodoItem savedTask = todoRepository.save(todoItem1);
        Long taskId = savedTask.getId();

        savedTask.setTitle("Updated Task");
        savedTask.setStatus(TodoItem.ItemStatus.COMPLETED);
        todoRepository.save(savedTask);

        Optional<TodoItem> updatedTask = todoRepository.findById(taskId);
        assertTrue(updatedTask.isPresent());
        assertEquals("Updated Task", updatedTask.get().getTitle());
        assertEquals(TodoItem.ItemStatus.COMPLETED, updatedTask.get().getStatus());

    }

    @Test
    void taskWithUser_ShouldMaintainRelationship()
    {
        todoItem1.setAssignedTo(user1);

        TodoItem savedTask = todoRepository.save(todoItem1);

        Optional<TodoItem> foundTask = todoRepository.findById(savedTask.getId());
        assertTrue(foundTask.isPresent());
        assertNotNull(foundTask.get().getAssignedTo());
        assertEquals("John", foundTask.get().getAssignedTo().getFName());
        assertEquals("Doe", foundTask.get().getAssignedTo().getLName());

    }

    @Test
    void unassignTask_ShouldRemoveUserRelationship() {
        User savedUser = userRepository.save(user1);
        todoItem1.setAssignedTo(savedUser);

        TodoItem savedTask = todoRepository.save(todoItem1);
        Long taskId = savedTask.getId();

        savedTask.setAssignedTo(null);
        todoRepository.save(savedTask);

        Optional<TodoItem> updatedTask = todoRepository.findById(taskId);
        assertTrue(updatedTask.isPresent());
        assertNull(updatedTask.get().getAssignedTo());
    }

    @Test
    void multipleTasksForDifferentUsers_ShouldMaintainSeparation() {
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        todoItem1.setAssignedTo(savedUser1);
        todoItem2.setAssignedTo(savedUser2);

        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);

        List<TodoItem> user1Tasks = todoRepository.findByAssignedToId(savedUser1.getId());
        List<TodoItem> user2Tasks = todoRepository.findByAssignedToId(savedUser2.getId());

        assertEquals(1, user1Tasks.size());
        assertEquals(1, user2Tasks.size());
        assertEquals("Task 1", user1Tasks.get(0).getTitle());
        assertEquals("Task 2", user2Tasks.get(0).getTitle());
    }

    @Test
    void taskTimestamps_ShouldBeAutomaticallySet() {
        TodoItem savedTask = todoRepository.save(todoItem1);

        assertNotNull(savedTask.getCreatedAt());
        assertNotNull(savedTask.getUpdatedAt());
    }


    }



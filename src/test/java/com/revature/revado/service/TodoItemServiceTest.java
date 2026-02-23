package com.revature.revado.service;

import com.revature.revado.dto.TodoItemRequest;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.repository.TodoRepository;
import com.revature.revado.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author $ {USER}
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodoItemServiceTest {
    @Autowired
    private TodoItemService todoItemService;

    @Autowired
    private TodoRepository todoItemRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        todoItemRepository.deleteAll();
        userRepository.deleteAll();
        testUser = userRepository.save(new User(null, "John", "Doe", "pwd", "johndoe", "john.doe@example.com", User.Role.USER, null, null));
        testUser2 = userRepository.save(new User(null, "Jane", "Doe", "janedoe", "pwd", "jane.doe@example.com", User.Role.USER, null, null));

    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        TodoItemRequest todoItem1 = new TodoItemRequest("Task 1", "Description 1", "PENDING", testUser.getId(), testUser.getId());
        TodoItemRequest todoItem2 = new TodoItemRequest("Task 2", "Description 2", "IN_PROGRESS", testUser.getId(), testUser.getId());
        todoItemService.createTodoItem(todoItem1);
        todoItemService.createTodoItem(todoItem2);

        List<TodoItem> result = todoItemService.getAllTodos();
        assertEquals(2, result.size());
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        TodoItemRequest taskRequest = new TodoItemRequest("Test Task", "Test Description", "PENDING", testUser.getId(), testUser.getId());
        TodoItem createdTask = todoItemService.createTodoItem(taskRequest);

        TodoItem result = todoItemService.getTodoItemById(createdTask.getId());

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldReturnEmptyTodo() {

        TodoItem result = todoItemService.getTodoItemById(99L);

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    void getTasksByUserId_ShouldReturnUserTasks() {
        TodoItemRequest task1 = new TodoItemRequest("Task 1", "Description 1", "PENDING", testUser.getId(), testUser.getId());
        todoItemService.createTodoItem(task1);

        TodoItemRequest task2 = new TodoItemRequest("Task 2", "Description 2", "PENDING", testUser2.getId(), testUser2.getId());
        todoItemService.createTodoItem(task2);

        TodoItem result = todoItemService.getTodoItemById(testUser2.getId());
        assertEquals("Task 2", result.getTitle());
    }

    @Test
    void getTasksByStatus_ShouldReturnTasksWithStatus() {
        // Arrange
        TodoItemRequest task1 = new TodoItemRequest("Task 1", "Description 1", "PENDING", testUser.getId(), testUser.getId());
        TodoItemRequest task2 = new TodoItemRequest("Task 2", "Description 2", "PENDING", testUser2.getId(), testUser2.getId());
        TodoItemRequest task3 = new TodoItemRequest("Task 3", "Description 3", "IN_PROGRESS", testUser.getId(), testUser2.getId());
        /*TaskRequest task2 = new TaskRequest("Task 2", "Description 2", "IN_PROGRESS", testUser.getId());
        TaskRequest task3 = new TaskRequest("Task 3", "Description 3", "PENDING", testUser.getId());*/
        todoItemService.createTodoItem(task1);
        todoItemService.createTodoItem(task2);
        todoItemService.createTodoItem(task3);

        List<TodoItem> result = todoItemService.getAllTodos()
                .stream()
                .filter(todo -> todo.getStatus() == TodoItem.ItemStatus.IN_PROGRESS)
                .toList();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(t -> t.getStatus() == TodoItem.ItemStatus.IN_PROGRESS));
    }

    @Test
    void createTask_WithoutAssignedUser_ShouldCreateTask() {
        TodoItemRequest taskRequest = new TodoItemRequest("New Task", "New Description", "PENDING", testUser.getId(), null);

        TodoItem result = todoItemService.createTodoItem(taskRequest);

        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertNull(result.getAssignedTo());
    }

    @Test
    void createTask_WithoutStatus_ShouldDefaultToPending() {
        // Arrange
        TodoItemRequest taskRequest = new TodoItemRequest("New Task", "New Description", null, testUser.getId(), testUser.getId());

        // Act
        TodoItem result = todoItemService.createTodoItem(taskRequest);

        // Assert
        assertNotNull(result);
        assertEquals(TodoItem.ItemStatus.PENDING, result.getStatus());
    }

    @Test
    void createTask_WithInvalidUserId_ShouldThrowException() {
        // Arrange
        TodoItemRequest taskRequest = new TodoItemRequest("New Task", "New Description", "PENDING", 999L, 999L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> todoItemService.createTodoItem(taskRequest));
    }

    @Test
    void updateTask_WhenTaskExists_ShouldUpdateTask() {
        // Arrange
        TodoItemRequest taskRequest = new TodoItemRequest("Original Task", "Original Description", "PENDING", testUser.getId(), testUser.getId());
        TodoItem createdTask = todoItemService.createTodoItem(taskRequest);

        TodoItemRequest taskRequest2 = new TodoItemRequest("Updated Task", "Updated Description", "IN_PROGRESS", testUser.getId(), testUser.getId());
        TodoItem updatedTask = todoItemService.createTodoItem(taskRequest2);

        TodoItem result = todoItemService.updateTodoItem(createdTask.getId(), updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TodoItem.ItemStatus.IN_PROGRESS, result.getStatus());

        TodoItem foundTask = todoItemRepository.findById(result.getId()).orElse(null);
        assertNotNull(foundTask);
        assertEquals("Updated Task", foundTask.getTitle());
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowException() {
        TodoItem updateData = new TodoItem();
        updateData.setTitle("Updated Task");

        assertThrows(RuntimeException.class,
                () -> todoItemService.updateTodoItem(999L, updateData));
    }



    @Test
    void updateTaskStatus_ShouldUpdateStatus() {
        TodoItemRequest taskRequest = new TodoItemRequest("Test Task", "Test Description", "PENDING", testUser.getId(), testUser.getId());
        TodoItem createdTask = todoItemService.createTodoItem(taskRequest);

        createdTask.setStatus(TodoItem.ItemStatus.COMPLETED);

        assertEquals(TodoItem.ItemStatus.COMPLETED, createdTask.getStatus());
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        TodoItemRequest taskRequest = new TodoItemRequest("Test Task", "Test Description", "PENDING", testUser.getId(), testUser.getId());
        TodoItem createdTask = todoItemService.createTodoItem(taskRequest);
        Long taskId = createdTask.getId();

        // Act
        todoItemService.deleteTodoItem(taskId);

        assertFalse(todoItemRepository.findById(taskId).isPresent());
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> todoItemService.deleteTodoItem(999L));
    }


    @Test
    void workflowTest_CreateAssignUpdateComplete() {
        TodoItemRequest taskRequest = new TodoItemRequest("Test Task", "Test Description", "PENDING", testUser.getId(), null);
        TodoItem task = todoItemService.createTodoItem(taskRequest);

        TodoItemRequest taskRequest2 = new TodoItemRequest("Test Task", "Test Description", "IN_PROGRESS", testUser.getId(), null);

        TodoItem updatedTask = todoItemService.createTodoItem(taskRequest2);
        todoItemService.updateTodoItem(task.getId(), updatedTask);
        assertEquals(TodoItem.ItemStatus.IN_PROGRESS, task.getStatus());

        TodoItemRequest taskRequest3 = new TodoItemRequest("Test Task", "Test Description", "COMPLETED", testUser.getId(), null);

        TodoItem updatedTask3 = todoItemService.createTodoItem(taskRequest3);
        todoItemService.updateTodoItem(task.getId(), updatedTask3);

        assertEquals(TodoItem.ItemStatus.COMPLETED, task.getStatus());



    }
}

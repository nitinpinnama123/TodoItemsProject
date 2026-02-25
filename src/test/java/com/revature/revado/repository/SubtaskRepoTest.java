package com.revature.revado.repository;

import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author $ {USER}
 **/
@SpringBootTest
public class SubtaskRepoTest {
    @Autowired
    private SubtaskRepository subtaskRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private TodoItem todoItem;
    private Subtask subtask1;
    private Subtask subtask2;

    @Test
    void findById_WhenSubtaskExists_ReturnsSubtask() {
        User user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");
        user1.setUsername("john-doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");
        user2.setUsername("jane-smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("pwd2");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("Task 1");
        todoItem1.setDescription("Description 1");
        todoItem1.setStatus(TodoItem.ItemStatus.PENDING);
        todoItem1.setAssignedTo(user1);

        TodoItem todoItem2 = new TodoItem();
        todoItem2.setTitle("Task 2");
        todoItem2.setDescription("Description 2");
        todoItem2.setStatus(TodoItem.ItemStatus.IN_PROGRESS);
        todoItem2.setAssignedTo(user2);


        TodoItem savedTask1 = todoRepository.save(todoItem1);
        TodoItem savedTask2 = todoRepository.save(todoItem2);

        Subtask subtask1 = new Subtask();
        subtask1.setTitle("Subtask 1");
        subtask1.setTodo(todoItem1);
        subtask1.setCompleted(false);

        Subtask savedSubtask1 = subtaskRepository.save(subtask1);

        assertEquals("Task 1", savedSubtask1.getTodo().getTitle());
    }

    @Test
    void findAll_ShouldReturnAllSubtasks() {
        User user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");
        user1.setUsername("john-doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");
        user2.setUsername("jane-smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("pwd2");

        userRepository.save(user1);
        userRepository.save(user2);

        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("Task 1");
        todoItem1.setDescription("Description 1");
        todoItem1.setStatus(TodoItem.ItemStatus.PENDING);
        todoItem1.setAssignedTo(user1);

        TodoItem todoItem2 = new TodoItem();
        todoItem2.setTitle("Task 2");
        todoItem2.setDescription("Description 2");
        todoItem2.setStatus(TodoItem.ItemStatus.IN_PROGRESS);
        todoItem2.setAssignedTo(user2);

        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);

        Subtask subtask1 = new Subtask();
        subtask1.setTitle("Subtask 1");
        subtask1.setTodo(todoItem1);
        subtask1.setCompleted(false);

        Subtask subtask2 = new Subtask();
        subtask2.setTitle("Subtask 2");
        subtask2.setTodo(todoItem1);
        subtask2.setCompleted(false);

        Subtask subtask1_todo2 = new Subtask();
        subtask1_todo2.setTitle("Subtask1_Todo2");
        subtask1_todo2.setTodo(todoItem2);
        subtask1_todo2.setCompleted(false);

        Subtask subtask2_todo2 = new Subtask();
        subtask2_todo2.setTitle("Substask2_Todo2");
        subtask2_todo2.setTodo(todoItem2);
        subtask2_todo2.setCompleted(false);

        subtaskRepository.save(subtask1);
        subtaskRepository.save(subtask2);
        subtaskRepository.save(subtask1_todo2);
        subtaskRepository.save(subtask2_todo2);

        List<Subtask> subtasks = subtaskRepository.findAll();

        assertEquals(4, subtasks.size());
    }

    @Test
    void findByTodoId_ShouldReturnSubtasks() {
        User user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");
        user1.setUsername("john-doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");
        user2.setUsername("jane-smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("pwd2");

        userRepository.save(user1);
        userRepository.save(user2);

        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("Task 1");
        todoItem1.setDescription("Description 1");
        todoItem1.setStatus(TodoItem.ItemStatus.PENDING);
        todoItem1.setAssignedTo(user1);

        TodoItem todoItem2 = new TodoItem();
        todoItem2.setTitle("Task 2");
        todoItem2.setDescription("Description 2");
        todoItem2.setStatus(TodoItem.ItemStatus.IN_PROGRESS);
        todoItem2.setAssignedTo(user2);

        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);

        Subtask subtask1 = new Subtask();
        subtask1.setTitle("Subtask 1");
        subtask1.setTodo(todoItem1);
        subtask1.setCompleted(false);

        Subtask subtask2 = new Subtask();
        subtask2.setTitle("Subtask 2");
        subtask2.setTodo(todoItem1);
        subtask2.setCompleted(false);

        Subtask subtask1_todo2 = new Subtask();
        subtask1_todo2.setTitle("Subtask1_Todo2");
        subtask1_todo2.setTodo(todoItem2);
        subtask1_todo2.setCompleted(false);

        Subtask subtask2_todo2 = new Subtask();
        subtask2_todo2.setTitle("Substask2_Todo2");
        subtask2_todo2.setTodo(todoItem2);
        subtask2_todo2.setCompleted(false);

        subtaskRepository.save(subtask1);
        subtaskRepository.save(subtask2);
        subtaskRepository.save(subtask1_todo2);
        subtaskRepository.save(subtask2_todo2);


        List<Subtask> subtasksByTodoItem = subtaskRepository.findByTodoId(todoItem2.getId());
        assertEquals(2, subtasksByTodoItem.size());
        assertTrue(subtasksByTodoItem.stream().allMatch(t -> t.getTodo().getId().equals(todoItem2.getId())));
    }

    @Test
    void findByIdAndTodoId_ShouldReturnTasksWithIdAndTodoId()
    {
        User user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");
        user1.setUsername("john-doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");
        user2.setUsername("jane-smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("pwd2");

        userRepository.save(user1);
        userRepository.save(user2);

        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("Task 1");
        todoItem1.setDescription("Description 1");
        todoItem1.setStatus(TodoItem.ItemStatus.PENDING);
        todoItem1.setAssignedTo(user1);

        TodoItem todoItem2 = new TodoItem();
        todoItem2.setTitle("Task 2");
        todoItem2.setDescription("Description 2");
        todoItem2.setStatus(TodoItem.ItemStatus.IN_PROGRESS);
        todoItem2.setAssignedTo(user2);

        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);

        Subtask subtask1 = new Subtask();
        subtask1.setTitle("Subtask 1");
        subtask1.setTodo(todoItem1);
        subtask1.setCompleted(false);

        Subtask subtask2 = new Subtask();
        subtask2.setTitle("Subtask 2");
        subtask2.setTodo(todoItem1);
        subtask2.setCompleted(false);

        Subtask subtask1_todo2 = new Subtask();
        subtask1_todo2.setTitle("Subtask1_Todo2");
        subtask1_todo2.setTodo(todoItem2);
        subtask1_todo2.setCompleted(false);

        Subtask subtask2_todo2 = new Subtask();
        subtask2_todo2.setTitle("Subtask2_Todo2");
        subtask2_todo2.setTodo(todoItem2);
        subtask2_todo2.setCompleted(false);

        subtaskRepository.save(subtask1);
        subtaskRepository.save(subtask2);
        subtaskRepository.save(subtask1_todo2);
        subtaskRepository.save(subtask2_todo2);

        Optional<Subtask> subtaskByIdAndTodoId = subtaskRepository.findByIdAndTodoId(subtask2_todo2.getId(), todoItem2.getId());
        assertTrue(subtaskByIdAndTodoId.isPresent());
        assertEquals("Subtask2_Todo2", subtaskByIdAndTodoId.get().getTitle());
    }

    @Test
    void updateSubtask_ShouldModifySubtask() {

        User user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");
        user1.setUsername("john-doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");
        user2.setUsername("jane-smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("pwd2");

        userRepository.save(user1);
        userRepository.save(user2);

        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("Task 1");
        todoItem1.setDescription("Description 1");
        todoItem1.setStatus(TodoItem.ItemStatus.PENDING);
        todoItem1.setAssignedTo(user1);

        TodoItem todoItem2 = new TodoItem();
        todoItem2.setTitle("Task 2");
        todoItem2.setDescription("Description 2");
        todoItem2.setStatus(TodoItem.ItemStatus.IN_PROGRESS);
        todoItem2.setAssignedTo(user2);

        todoRepository.save(todoItem1);
        todoRepository.save(todoItem2);
        Subtask subtask1 = new Subtask();
        subtask1.setTitle("Subtask 1");
        subtask1.setTodo(todoItem1);
        subtask1.setCompleted(false);

        Subtask savedSubtask = subtaskRepository.save(subtask1);
        Long subtaskId = savedSubtask.getId();

        savedSubtask.setTitle("Updated subtask");
        savedSubtask.setCompleted(true);
        subtaskRepository.save(savedSubtask);

        Optional<Subtask> updatedSubtask = subtaskRepository.findById(subtaskId);
        assertTrue(updatedSubtask.isPresent());
        assertEquals("Updated subtask", updatedSubtask.get().getTitle());
        assertEquals(true, updatedSubtask.get().isCompleted());

    }
}

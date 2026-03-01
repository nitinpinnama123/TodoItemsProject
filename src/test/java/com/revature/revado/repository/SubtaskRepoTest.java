package com.revature.revado.repository;

import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $ {USER}
 **/
@SpringBootTest
@Transactional
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
    void createSubtask_AndVerifyTimesUpdated() {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        assertEquals("Subtask 1", savedSubtask.getTitle());
        assertNotNull(savedSubtask.getCreatedAt());
        assertNotNull(savedSubtask.getUpdatedAt());

    }

    @Test
    void findById_WhenSubtaskExists_ReturnsSubtask() {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        Optional<Subtask> subtaskFound = subtaskRepository.findById(savedSubtask.getId());
        assertTrue(subtaskFound.isPresent());
        assertEquals(1L, subtaskFound.get().getId());
    }

    @Test
    void findAll_ShouldReturnAllSubtasks() {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        Subtask subtask2 = new Subtask(null, "Subtask 2", false, savedTask1, null, null);
        Subtask savedSubtask2 = subtaskRepository.save(subtask2);

        List<Subtask> subtasks = subtaskRepository.findAll();

        assertEquals(2, subtasks.size());
    }

    @Test
    void findByTodoId_ShouldReturnSubtasks() {

        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        Subtask subtask2 = new Subtask(null, "Subtask 2", false, savedTask1, null, null);
        Subtask savedSubtask2 = subtaskRepository.save(subtask2);


        List<Subtask> subtasksByTodoItem = subtaskRepository.findByTodoId(savedTask1.getId());
        assertEquals(2, subtasksByTodoItem.size());
        assertTrue(subtasksByTodoItem.stream().allMatch(t -> t.getTodo().getId().equals(savedTask1.getId())));
    }

    @Test
    void findByIdAndTodoId_ShouldReturnTasksWithIdAndTodoId()
    {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        Subtask subtask2 = new Subtask(null, "Subtask 2", false, savedTask1, null, null);
        Subtask savedSubtask2 = subtaskRepository.save(subtask2);

        Optional<Subtask> subtaskByIdAndTodoId = subtaskRepository.findByIdAndTodoId(savedSubtask2.getId(), savedTask1.getId());
        assertTrue(subtaskByIdAndTodoId.isPresent());
        assertEquals("Subtask 2", subtaskByIdAndTodoId.get().getTitle());
    }

    @Test
    void updateSubtask_ShouldModifySubtask() {

        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);


        savedSubtask.setTitle("Updated subtask");
        savedSubtask.setCompleted(true);
        subtaskRepository.save(savedSubtask);

        Optional<Subtask> updatedSubtask = subtaskRepository.findById(savedSubtask.getId());
        assertTrue(updatedSubtask.isPresent());
        assertEquals("Updated subtask", updatedSubtask.get().getTitle());
        assertEquals(true, updatedSubtask.get().isCompleted());

    }

    @Test
    void deleteSubtask_ShouldRemoveSubtask() {
        User user1 = new User(null, "John", "Doe", "john-doe", "john.doe@example.com", "pwd", User.Role.USER, null, null);
        user1 = userRepository.save(user1);

        TodoItem todoItem1 = new TodoItem(null, "Task 1", "Description 1", TodoItem.ItemStatus.PENDING, null, null, null, null);
        TodoItem savedTask1 = todoRepository.save(todoItem1);

        Subtask subtask = new Subtask(null, "Subtask 1", false, savedTask1, null, null);
        Subtask savedSubtask = subtaskRepository.save(subtask);


        subtaskRepository.deleteById(savedSubtask.getId());
        Optional<Subtask> deletedSubtask = subtaskRepository.findById(savedSubtask.getId());
        assertFalse(deletedSubtask.isPresent());
    }
}

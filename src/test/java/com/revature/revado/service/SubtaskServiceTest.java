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
    private SubtaskService subtaskService;
    @Autowired
    private SubtaskRepository subtaskRepository;

    @Autowired
    private TodoRepository todoItemRepository;


    @Test
    void findAll_ShouldReturnAllSubtasks() {
       TodoItem todoItem = new TodoItem();
       todoItem.setTitle("Test Todo");
       todoItemRepository.save(todoItem);

       Subtask sub1 = new Subtask();
       sub1.setTitle("Subtask1");
       sub1.setCompleted(false);
       sub1.setTodo(todoItem);

        Subtask sub2 = new Subtask();
        sub2.setTitle("Subtask 2");
        sub2.setCompleted(true);
        sub2.setTodo(todoItem);

        subtaskRepository.save(sub1);
        subtaskRepository.save(sub2);

        List<Subtask> subtasks = subtaskService.getTodoSubtasks(todoItem.getId());
        assertEquals(2, subtasks.size());
        assertEquals("Subtask 2", subtasks.get(1).getTitle());


    }

    @Test
    void createSubtask_ShouldSaveSubtask() {

        TodoItem todo = new TodoItem();
        todo.setTitle("Create Test Todo");
        todo = todoItemRepository.save(todo);

        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("New Subtask");
        request.setCompleted(true);

        Subtask result =
                subtaskService.createSubtask(todo.getId(), request);

        assertNotNull(result.getId());
        assertEquals("New Subtask", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void updateSubtask_ShouldUpdateFields() {

        TodoItem todo = new TodoItem();
        todo.setTitle("Update Test Todo");
        todo = todoItemRepository.save(todo);

        Subtask subtask = new Subtask();
        subtask.setTitle("Old Title");
        subtask.setCompleted(false);
        subtask.setTodo(todo);
        subtask = subtaskRepository.save(subtask);

        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("Updated Title");
        request.setCompleted(true);

        Subtask updated =
                subtaskService.updateSubtask(subtask.getId(), todo.getId(), request);

        assertEquals("Updated Title", updated.getTitle());
        assertTrue(updated.isCompleted());
    }

    @Test
    void toggleSubtaskComplete_ShouldFlipCompletionStatus() {

        TodoItem todo = new TodoItem();
        todo.setTitle("Toggle Test Todo");
        todo = todoItemRepository.save(todo);

        Subtask subtask = new Subtask();
        subtask.setTitle("Toggle Subtask");
        subtask.setCompleted(false);
        subtask.setTodo(todo);
        subtask = subtaskRepository.save(subtask);

        Subtask toggled =
                subtaskService.toggleSubtaskComplete(subtask.getId(), todo.getId());

        assertTrue(toggled.isCompleted());
    }

    @Test
    void deleteSubtask_ShouldRemoveSubtask() {
        TodoItem todo = new TodoItem();
        todo.setTitle("Delete Test Todo");
        todoItemRepository.save(todo);

        Subtask subtask = new Subtask();
        subtask.setTitle("Delete Me");
        subtask.setCompleted(false);
        subtask.setTodo(todo);
        subtask = subtaskRepository.save(subtask);

        subtaskService.deleteSubtask(subtask.getId(), todo.getId());

        boolean exists =
                subtaskRepository.findById(subtask.getId()).isPresent();

        assertFalse(exists);
    }



}

package com.revature.revado.service;

import com.revature.revado.entity.TodoItem;
import com.revature.revado.dto.TodoItemRequest;
import com.revature.revado.entity.User;
import com.revature.revado.repository.TodoRepository;
import com.revature.revado.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Service
@RequiredArgsConstructor
public class TodoItemService {
    private final TodoRepository todoRepo;
    private final UserService userService;
    private final UserRepository userRepo;

    public TodoItem createTodoItem(TodoItemRequest todoItemRequest) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(todoItemRequest.getTitle());
        todoItem.setDescription(todoItemRequest.getDescription());
        if (todoItemRequest.getStatus() != null)
        {
            todoItem.setStatus(TodoItem.ItemStatus.valueOf(String.valueOf(todoItemRequest.getStatus())));
        }
        else {
            todoItem.setStatus(TodoItem.ItemStatus.PENDING);
        }
        if (todoItemRequest.getAssignedToId() != null) {
            User user = userService.getUserById(todoItemRequest.getAssignedToId());
            todoItem.setAssignedTo(user);
        }
        if (todoItemRequest.getCreatedById() != null) {
            User user = userService.getUserById(todoItemRequest.getCreatedById());
            todoItem.setCreatedBy(user);
        }
        return todoRepo.save(todoItem);
    }

    public List<TodoItem> getAllTodos() {
        return todoRepo.findAll();
    }

    public TodoItem getTodoItemById(Long id)
    {
        Optional<TodoItem> possibleTodoItem = todoRepo.findById(id);
        if (possibleTodoItem.isPresent()) {
            return possibleTodoItem.get();
        } else {
            return new TodoItem();
        }
    }

    @Transactional
    public TodoItem updateTodoItem(Long id, TodoItemRequest request)
    {
        TodoItem existingItem = todoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        existingItem.setTitle(request.getTitle());
        existingItem.setDescription(request.getDescription());
        existingItem.setStatus(request.getStatus());
        if (request.getAssignedToId() != null) {
            User user = userRepo.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingItem.setAssignedTo(user);
        }
        else {
            existingItem.setAssignedTo(null);
        }
        //existingItem.setAssignedTo(updatedItem.getAssignedTo());
        return todoRepo.save(existingItem);
    }

    public void deleteTodoItem(Long todoId)
    {
        if (!todoRepo.existsById(todoId)) {
            throw new RuntimeException("Todo item not found with id: " + todoId);
        }

        todoRepo.deleteById(todoId);
    }



}

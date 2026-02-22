package com.revature.revado.controller;

import com.revature.revado.dto.TodoItemRequest;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import com.revature.revado.service.TodoItemService;
import com.revature.revado.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TodoItemController {
    private final TodoItemService todoItemService;

    //String description, String status, User createdById, User assignedToId
    @PostMapping
    public TodoItem createTodoItem(@RequestBody TodoItemRequest todoItemRequest){
        return todoItemService.createTodoItem(todoItemRequest);
    }

    @DeleteMapping
    public void deleteTodoItem(@RequestBody TodoItem todoItem){
        todoItemService.deleteTodoItem(todoItem.getId());
    }

    @PatchMapping
    public void updateTodoItem(@RequestBody TodoItem todoItem) {
        todoItemService.updateTodoItem(todoItem.getId(), todoItem);
    }

    @GetMapping
    public List<TodoItem> getAllTodoItems() {
        return todoItemService.getAllTodos();
    }

    @GetMapping("/{id}")
    public TodoItem getTodoItemById(@PathVariable("id") Long id)
    {
        Optional<TodoItem> possibleTodoItem = Optional.ofNullable(todoItemService.getTodoItemById(id));
        if (possibleTodoItem.isPresent())
        {
            return possibleTodoItem.get();
        }
        else {
            return new TodoItem();
        }
    }
}

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
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TodoItemController {
    private final TodoItemService todoItemService;

    //String description, String status, User createdById, User assignedToId
    @PostMapping
    public TodoItem createTodoItem(@RequestBody TodoItemRequest todoItemRequest){
        return todoItemService.createTodoItem(todoItemRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteTodoItem(@PathVariable("id") Long id){
        todoItemService.deleteTodoItem(id);
    }

    @PatchMapping("/{id}")
    public void updateTodoItem(@PathVariable("id") Long id, @RequestBody TodoItem todoItem) {
        todoItemService.updateTodoItem(id, todoItem);
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

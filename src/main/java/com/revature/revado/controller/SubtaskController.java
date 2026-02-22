package com.revature.revado.controller;

import com.revature.revado.entity.Subtask;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.repository.SubtaskRepository;
import com.revature.revado.service.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author $ {USER}
 **/

@RestController
@RequestMapping("/api/items/{id}/subtasks")
@RequiredArgsConstructor
public class SubtaskController {
    private final SubtaskService subtaskService;
    @GetMapping
    public Subtask getSubtaskInformation(){
        Subtask task = new Subtask();
        /*task.setSubtaskId(1);
        task.setSubtaskDesc("");
        task.setStatus();*/
        return task;
    }

    @PostMapping
    public ResponseEntity<Subtask> addSubtask(@RequestBody Subtask subtask)
    {
        /*subtask.setSubtaskId(subtask.getSubtaskId());
        subtask.setComplete(true);
        subtask.setSubtaskDesc(subtask.getSubtaskDesc());*/
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("custom-header", "custom value").body(subtask);
    }

}

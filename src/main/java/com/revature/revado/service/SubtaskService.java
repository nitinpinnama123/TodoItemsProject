package com.revature.revado.service;

import com.revature.revado.entity.Subtask;
import com.revature.revado.repository.SubtaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Service
@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepository subtaskRepo;

    public Subtask createSubtask(Subtask subtask)
    {
        return subtaskRepo.save(subtask);
    }

    public List<Subtask> getAllSubtasks() {
        return subtaskRepo.findAll();
    }

    public Subtask getSubTaskById(UUID id) {
        return subtaskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));
    }

    public Subtask updateSubTask(UUID id, Subtask updatedSubTask) {

        Subtask existing = getSubTaskById(id);

        existing.setSubtaskDesc(updatedSubTask.getSubtaskDesc());
        existing.setStatus(updatedSubTask.getStatus());
        existing.setTodoItem(updatedSubTask.getTodoItem());

        return subtaskRepo.save(existing);
    }

    public void deleteSubTask(UUID id) {
        subtaskRepo.deleteById(id);
    }
}

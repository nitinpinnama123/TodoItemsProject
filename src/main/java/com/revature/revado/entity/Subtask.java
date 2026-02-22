package com.revature.revado.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Data
@Entity
@Table(name = "subtasks")
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID subtaskId;
    private String subtaskDesc;
    private String status;
    @ManyToOne
    @JoinColumn(name = "todo_item_id")
    private TodoItem todoItem;
}

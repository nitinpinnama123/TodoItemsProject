package com.revature.revado.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author $ {USER}
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemRequest {
    private String title;
    private String description;
    private String status;
    private Long createdById;
    private Long assignedToId;
}

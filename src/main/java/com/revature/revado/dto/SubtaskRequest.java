package com.revature.revado.dto;

import com.revature.revado.entity.TodoItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author $ {USER}
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtaskRequest {
    private String title;
    private Boolean completed;
}

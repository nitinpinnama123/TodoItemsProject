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
public class SubtaskRequest {
    private String title;
    private Boolean completed;
}

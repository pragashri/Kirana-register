package com.example.springboot.feature_users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String errorCode;
    private String displayMsg;
    private Object errorResponse;
    private Object data;

    // Custom Constructor for Success Response
    public ApiResponse(boolean success, String displayMsg, Object data) {
        this.success = success;
        this.displayMsg = displayMsg;
        this.data = data;
    }
}

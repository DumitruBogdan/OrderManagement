package com.project.ordermanagement.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private List<String> message;
    private LocalDateTime timeStamp;
}

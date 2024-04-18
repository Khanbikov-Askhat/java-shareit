package ru.practicum.shareit.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    String message;
    String error;

    public ErrorResponse(String createdMessage, String createdMessageException) {
        this.message = createdMessage;
        this.error = createdMessageException;
    }
}




package ru.practicum.shareit.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    public String message;
    public String error;

    public ErrorResponse(String createdMessage, String createdMessageException) {
        this.message = createdMessage;
        this.error = createdMessageException;
    }
}




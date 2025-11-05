package ru.practicum.shareit.request.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(final Long requestId) {
        super("Request with id = %d not found".formatted(requestId));
    }
}

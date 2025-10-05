package ru.practicum.shareit.request.exception;

public class DescriptionExistsException extends RuntimeException {
    public DescriptionExistsException(final String description) {
        super("This description already exists" + description);
    }
}

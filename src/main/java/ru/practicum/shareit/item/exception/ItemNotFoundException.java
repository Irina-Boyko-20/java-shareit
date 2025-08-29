package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(final Long userId) {
        super("Item with id = %d not found".formatted(userId));
    }
}

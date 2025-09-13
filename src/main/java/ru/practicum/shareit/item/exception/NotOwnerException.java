package ru.practicum.shareit.item.exception;

public class NotOwnerException extends RuntimeException {
    public NotOwnerException(final Long ownerId, final Long itemId) {
        super("User with id = %d is not the owner of item with id = %d".formatted(ownerId, itemId));
    }
}

package ru.practicum.shareit.item.exception;

public class NullOrEmptyException extends RuntimeException {
    public NullOrEmptyException(String message) {
        super(message);
    }
}

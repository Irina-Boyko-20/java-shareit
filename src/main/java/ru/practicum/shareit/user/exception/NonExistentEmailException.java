package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NonExistentEmailException extends RuntimeException {
    public NonExistentEmailException(final String email) {
        super("This email already exists" + email);
    }
}

package ru.practicum.shareit.user.exception;

/**
 * Исключение, выбрасываемое при попытке создать или обновить пользователя с уже существующим email.
 */
public class EmailExistsException extends RuntimeException {

    /**
     * Конструктор исключения.
     *
     * @param email Электронная почта, которая уже существует.
     */
    public EmailExistsException(final String email) {
        super("This email already exists" + email);
    }
}

package ru.practicum.shareit.user.exception;

/**
 * Исключение, выбрасываемое при попытке получить пользователя по несуществующему идентификатору.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения.
     *
     * @param userId Идентификатор пользователя, который не найден.
     */
    public UserNotFoundException(final Long userId) {
        super("User with id = %d not found".formatted(userId));
    }
}

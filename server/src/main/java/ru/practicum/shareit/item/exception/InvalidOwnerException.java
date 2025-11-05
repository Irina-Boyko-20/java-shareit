package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое при попытке обновить предмет пользователем,
 * который не является его владельцем.
 * <p>
 * Используется в сервисе для проверки прав на обновление.
 */
public class InvalidOwnerException extends RuntimeException {

    /**
     * Конструктор с параметрами.
     *
     * @param ownerId ID пользователя, пытающегося обновить предмет.
     * @param itemId ID предмета.
     */
    public InvalidOwnerException(final Long ownerId, final Long itemId) {
        super("User with id = %d is not the owner of item with id = %d".formatted(ownerId, itemId));
    }
}

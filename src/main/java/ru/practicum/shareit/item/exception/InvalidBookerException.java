package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое при попытке добавить комментарий пользователем,
 * который не арендовал предмет.
 * <p>
 * Используется в сервисе для проверки прав на комментарий.
 */
public class InvalidBookerException extends RuntimeException {

    /**
     * Конструктор с параметрами.
     *
     * @param author ID пользователя, пытающегося добавить комментарий.
     * @param itemId ID предмета.
     */
    public InvalidBookerException(final Long author, final Long itemId) {
        super("The user with id = %d did not rent the item with id = %d".formatted(author, itemId));
    }
}

package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое при отсутствии предмета с указанным ID.
 * <p>
 * Используется в репозиториях и сервисах при поиске предмета.
 */
public class ItemNotFoundException extends RuntimeException {

    /**
     * Конструктор с параметром.
     *
     * @param userId ID предмета (или пользователя, если контекст требует).
     */
    public ItemNotFoundException(final Long userId) {
        super("Item with id = %d not found".formatted(userId));
    }
}

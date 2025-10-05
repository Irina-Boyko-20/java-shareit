package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое при null или пустом значении поля.
 * <p>
 * Используется для валидации данных в сервисах.
 */
public class NullOrEmptyException extends RuntimeException {

    /**
     * Конструктор с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public NullOrEmptyException(String message) {
        super(message);
    }
}

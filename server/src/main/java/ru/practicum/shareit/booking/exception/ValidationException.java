package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое при некорректных датах бронирования.
 * <p>
 * Дата начала должна быть раньше даты окончания.
 * </p>
 */
public class ValidationException extends RuntimeException {

    /**
     * Конструктор со строковым параметром.
     * @param message сообщение об конкретной ошибке.
     */
    public ValidationException(String message) {
        super(message);
    }
}

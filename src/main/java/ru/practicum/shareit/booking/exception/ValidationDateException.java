package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое при некорректных датах бронирования.
 * <p>
 * Дата начала должна быть раньше даты окончания.
 * </p>
 */
public class ValidationDateException extends RuntimeException {

    /**
     * Конструктор без параметров.
     */
    public ValidationDateException() {
        super("Start date must be before end date");
    }
}

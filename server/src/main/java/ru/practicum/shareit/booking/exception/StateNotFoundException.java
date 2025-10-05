package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое при неизвестном состоянии фильтрации бронирований.
 * <p>
 * Состояние должно быть одним из предопределенных (ALL, PAST, FUTURE и т.д.).
 * </p>
 */
public class StateNotFoundException extends RuntimeException {

    /**
     * Конструктор с неизвестным состоянием.
     *
     * @param state Неизвестное состояние.
     */
    public StateNotFoundException(String state) {
        super("Unknown state - %s ".formatted(state));
    }
}

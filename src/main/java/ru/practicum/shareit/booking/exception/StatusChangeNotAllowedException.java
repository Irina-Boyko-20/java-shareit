package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.booking.model.Status;

/**
 * Исключение, выбрасываемое при попытке изменить статус бронирования в недопустимом состоянии.
 * <p>
 * Изменения разрешены только в определенных статусах.
 * </p>
 */
public class StatusChangeNotAllowedException extends RuntimeException {

    /**
     * Конструктор с текущим статусом.
     *
     * @param status Текущий статус бронирования.
     */
    public StatusChangeNotAllowedException(Status status) {
        super("When the status is %s, changes are not allowed.".formatted(status));
    }
}

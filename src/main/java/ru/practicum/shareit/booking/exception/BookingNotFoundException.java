package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое при отсутствии бронирования с указанным ID.
 * <p>
 * Это исключение используется в сервисах для индикации, что бронирование не найдено в базе данных.
 * </p>
 */
public class BookingNotFoundException extends RuntimeException {

    /**
     * Конструктор с ID бронирования.
     *
     * @param bookingId ID бронирования, которое не найдено.
     */
    public BookingNotFoundException(final Long bookingId) {
        super("Booking with id = %d not found".formatted(bookingId));
    }
}

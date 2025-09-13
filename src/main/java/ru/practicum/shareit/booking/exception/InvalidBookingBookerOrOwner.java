package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к бронированию без прав.
 * <p>
 * Доступ к бронированию разрешен только бронирующему или владельцу вещи.
 * </p>
 */
public class InvalidBookingBookerOrOwner extends RuntimeException {

    /**
     * Конструктор с ID пользователя и бронирования.
     *
     * @param userId ID пользователя, пытающегося получить доступ.
     * @param bookingId ID бронирования.
     */
    public InvalidBookingBookerOrOwner(Long userId, Long bookingId) {
        super("User with id - %d access is not allowed. Obtaining data about a specific booking ".formatted(userId) +
                "- %d can be done either by the booking author or by the item owner.".formatted(bookingId));
    }
}

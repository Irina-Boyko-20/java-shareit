package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое при попытке изменить статус бронирования не владельцем вещи.
 * <p>
 * Только владелец вещи может подтверждать или отклонять бронирование.
 * </p>
 */
public class InvalidOwnerBooking extends RuntimeException {

    /**
     * Конструктор с ID бронирования.
     *
     * @param bookingId ID бронирования.
     */
    public InvalidOwnerBooking(Long bookingId) {
        super("Confirmation or rejection of a booking request " +
                "- %d can only be done by the owner of the item".formatted(bookingId));
    }
}

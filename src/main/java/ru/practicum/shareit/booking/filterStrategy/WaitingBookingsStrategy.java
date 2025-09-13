package ru.practicum.shareit.booking.filterStrategy;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

/**
 * Стратегия фильтрации ожидающих бронирований пользователя.
 * <p>
 * Реализует паттерн "Стратегия" для получения бронирований со статусом WAITING.
 * </p>
 */
public class WaitingBookingsStrategy implements BookingsFilterStrategy {

    /**
     * Фильтрует ожидающие бронирования пользователя.
     *
     * @param userId ID пользователя.
     * @param repository Репозиторий бронирований.
     * @return Список ожидающих бронирований.
     */
    @Override
    public List<Booking> filter(Long userId, BookingRepository repository) {
        return repository.findAllByBookerIdAndStatus(userId, Status.WAITING);
    }
}

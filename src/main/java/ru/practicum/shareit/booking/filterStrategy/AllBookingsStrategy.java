package ru.practicum.shareit.booking.filterStrategy;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

/**
 * Стратегия фильтрации всех бронирований пользователя.
 * <p>
 * Реализует паттерн "Стратегия" для получения всех бронирований бронирующего,
 * отсортированных по дате начала в убывающем порядке.
 * </p>
 */
public class AllBookingsStrategy implements BookingsFilterStrategy {

    /**
     * Фильтрует все бронирования пользователя.
     *
     * @param userId ID пользователя.
     * @param repository Репозиторий бронирований.
     * @return Список всех бронирований пользователя.
     */
    @Override
    public List<Booking> filter(Long userId, BookingRepository repository) {
        return repository.findAllByBookerIdOrderByStartDesc(userId);
    }
}

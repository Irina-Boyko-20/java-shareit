package ru.practicum.shareit.booking.filterStrategy;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

/**
 * Интерфейс стратегии фильтрации бронирований.
 * <p>
 * Определяет контракт для конкретных стратегий фильтрации.
 * Каждая стратегия реализует логику фильтрации по определенному состоянию.
 * </p>
 */
public interface BookingsFilterStrategy {

    /**
     * Фильтрует бронирования пользователя.
     *
     * @param userId ID пользователя.
     * @param repository Репозиторий бронирований.
     * @return Список отфильтрованных бронирований.
     */
    List<Booking> filter(Long userId, BookingRepository repository);
}

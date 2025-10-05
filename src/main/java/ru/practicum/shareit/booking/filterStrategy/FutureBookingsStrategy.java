package ru.practicum.shareit.booking.filterStrategy;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Стратегия фильтрации будущих бронирований пользователя.
 * <p>
 * Реализует паттерн "Стратегия" для получения бронирований, которые начнутся после текущего времени.
 * </p>
 */
public class FutureBookingsStrategy implements BookingsFilterStrategy {

    /**
     * Сортировка по дате начала в убывающем порядке.
     */
    static final Sort SORT = Sort.by(Sort.Direction.DESC, "start");

    /**
     * Фильтрует будущие бронирования пользователя.
     *
     * @param userId ID пользователя.
     * @param repository Репозиторий бронирований.
     * @return Список будущих бронирований.
     */
    @Override
    public List<Booking> filter(Long userId, BookingRepository repository) {
        return repository.findAllByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), SORT);
    }
}

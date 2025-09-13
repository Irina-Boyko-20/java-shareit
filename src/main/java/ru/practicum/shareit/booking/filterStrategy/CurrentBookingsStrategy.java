package ru.practicum.shareit.booking.filterStrategy;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Стратегия фильтрации текущих бронирований пользователя.
 * <p>
 * Реализует паттерн "Стратегия" для получения бронирований, которые активны в настоящий момент
 * (начались до текущего времени и закончатся после).
 * </p>
 */
public class CurrentBookingsStrategy implements BookingsFilterStrategy {

    /**
     * Сортировка по дате начала в убывающем порядке.
     */
    static final Sort SORT = Sort.by(Sort.Direction.DESC, "start");

    /**
     * Фильтрует текущие бронирования пользователя.
     *
     * @param userId ID пользователя.
     * @param repository Репозиторий бронирований.
     * @return Список текущих бронирований.
     */
    @Override
    public List<Booking> filter(Long userId, BookingRepository repository) {
        return repository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
                userId, LocalDateTime.now(), LocalDateTime.now(), SORT);
    }
}

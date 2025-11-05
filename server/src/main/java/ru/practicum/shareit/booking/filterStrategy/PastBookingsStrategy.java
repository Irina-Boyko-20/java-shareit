package ru.practicum.shareit.booking.filterStrategy;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Стратегия фильтрации прошедших бронирований пользователя.
 * <p>
 * Реализует паттерн "Стратегия" для получения бронирований, которые закончились до текущего времени.
 * </p>
 */
public class PastBookingsStrategy implements BookingsFilterStrategy {

    /**
     * Сортировка по дате начала в убывающем порядке.
     */
    static final Sort SORT = Sort.by(Sort.Direction.DESC, "start");

    /**
     * Фильтрует прошедшие бронирования пользователя.
     *
     * @param userId ID пользователя.
     * @param repository Репозиторий бронирований.
     * @param isOwner показатель, является ли пользователь владельцем.
     * @return Список прошедших бронирований.
     */
    @Override
    public List<Booking> filter(Long userId, BookingRepository repository, boolean isOwner) {
        if (isOwner) {
            return repository.findAllByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), SORT);
        } else {
            return repository.findAllByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), SORT);
        }
    }
}

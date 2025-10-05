package ru.practicum.shareit.booking.filterStrategy;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

/**
 * Стратегия фильтрации отклоненных бронирований пользователя.
 * <p>
 * Реализует паттерн "Стратегия" для получения бронирований со статусом REJECTED.
 * </p>
 */
public class RejectedBookingsStrategy implements BookingsFilterStrategy {

    /**
     * Фильтрует отклоненные бронирования пользователя.
     *
     * @param userId ID пользователя.
     * @param repository Репозиторий бронирований.
     * @param isOwner показатель, является ли пользователь владельцем.
     * @return Список отклоненных бронирований.
     */
    @Override
    public List<Booking> filter(Long userId, BookingRepository repository, boolean isOwner) {
        if (isOwner) {
            return repository.findAllByItemOwnerIdAndStatus(userId, Status.REJECTED);
        } else {
            return repository.findAllByBookerIdAndStatus(userId, Status.REJECTED);
        }
    }
}

package ru.practicum.shareit.booking.filterStrategy;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контекст для выбора стратегии фильтрации бронирований.
 * <p>
 * Этот класс реализует паттерн "Стратегия" с использованием карты стратегий.
 * Он выбирает подходящую стратегию на основе состояния и применяет её для фильтрации.
 * </p>
 */
public class BookingsFilterContext {

    /**
     * Карта стратегий по состояниям.
     */
    private static final Map<State, BookingsFilterStrategy> strategies = new HashMap<>();

    static {
        strategies.put(State.ALL, new AllBookingsStrategy());
        strategies.put(State.PAST, new PastBookingsStrategy());
        strategies.put(State.FUTURE, new FutureBookingsStrategy());
        strategies.put(State.CURRENT, new CurrentBookingsStrategy());
        strategies.put(State.WAITING, new WaitingBookingsStrategy());
        strategies.put(State.REJECTED, new RejectedBookingsStrategy());
    }

    /**
     * Фильтрует бронирования с использованием выбранной стратегии.
     *
     * @param userId ID пользователя.
     * @param state Состояние для фильтрации.
     * @param repository Репозиторий бронирований.
     * @return Список отфильтрованных бронирований.
     */
    public static List<Booking> filterBookings(Long userId, State state, BookingRepository repository) {
        BookingsFilterStrategy strategy = strategies.get(state);
        return strategy.filter(userId, repository);
    }
}

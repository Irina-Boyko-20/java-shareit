package ru.practicum.shareit.booking.dto;

import java.util.Optional;

/**
 * Перечисление состояний для фильтрации бронирований.
 * <p>
 * Определяет возможные состояния бронирований для запросов пользователя.
 * </p>
 */
public enum BookingState {
    /** Все бронирования. */
    ALL,
    /** Текущие бронирования. */
    CURRENT,
    /** Завершённые бронирования. */
    PAST,
    /** Будущие бронирования. */
    FUTURE,
    /** Бронирования, ожидающие подтверждения. */
    WAITING,
    /** Отклоненные бронирования. */
    REJECTED;

    /**
     * Проверяет существование состояния по строковому значению.
     *
     * @param value строковое представление состояния.
     * @return соответствующий объект State.
     */
    public static Optional<BookingState> from(String value) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return Optional.of(state);
            }
        }

        return Optional.empty();
    }
}

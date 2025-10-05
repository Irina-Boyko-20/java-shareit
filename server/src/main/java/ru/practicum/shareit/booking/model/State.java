package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.exception.StateNotFoundException;
import ru.practicum.shareit.item.exception.NullOrEmptyException;

/**
 * Перечисление состояний для фильтрации бронирований.
 * <p>
 * Определяет возможные состояния бронирований для запросов пользователя.
 * </p>
 */
public enum State {
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
     * @throws NullOrEmptyException если значение null или пустое.
     * @throws StateNotFoundException если состояние не найдено.
     */
    public static State stateExists(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new NullOrEmptyException("State cannot be null or empty");
        }
        try {
            return State.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException stateException) {
            throw new StateNotFoundException(value);
        }
    }
}

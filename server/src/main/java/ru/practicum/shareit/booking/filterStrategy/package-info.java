/**
 * Пакет стратегий фильтрации для модуля бронирования.
 * <p>
 * Этот пакет реализует паттерн "Стратегия" для фильтрации бронирований по различным состояниям
 * (например, все, прошедшие, будущие, текущие и т.д.). Стратегии позволяют гибко изменять логику
 * фильтрации без изменения основного кода сервиса.
 * </p>
 * <p>
 * Основные классы:
 * <ul>
 *   <li>{@link ru.practicum.shareit.booking.filterStrategy.BookingsFilterStrategy} - интерфейс стратегии.</li>
 *   <li>{@link ru.practicum.shareit.booking.filterStrategy.BookingsFilterContext} - контекст для выбора стратегии.</li>
 *   <li>Конкретные стратегии: {@link ru.practicum.shareit.booking.filterStrategy.AllBookingsStrategy},
 *       {@link ru.practicum.shareit.booking.filterStrategy.PastBookingsStrategy} и т.д.</li>
 * </ul>
 * </p>
 */
package ru.practicum.shareit.booking.filterStrategy;
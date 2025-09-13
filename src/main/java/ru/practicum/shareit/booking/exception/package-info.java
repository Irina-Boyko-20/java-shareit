/**
 * Пакет исключений для модуля бронирования.
 * <p>
 * Этот пакет содержит пользовательские исключения, которые выбрасываются в случае ошибок бизнес-логики,
 * валидации или доступа к данным в модуле бронирования. Исключения наследуются от {@link java.lang.RuntimeException}
 * и обрабатываются глобально через {@code @ControllerAdvice}.
 * </p>
 * <p>
 * Основные исключения:
 * <ul>
 *   <li>{@link ru.practicum.shareit.booking.exception.BookingNotFoundException} - бронирование не найдено.</li>
 *   <li>{@link ru.practicum.shareit.booking.exception.InvalidBookingBookerOrOwner} - недопустимый доступ
 *   к бронированию.</li>
 *   <li>{@link ru.practicum.shareit.booking.exception.ValidationDateException} - ошибка валидации дат.</li>
 * </ul>
 * </p>
 */
package ru.practicum.shareit.booking.exception;
/**
 * Пакет содержит глобальный обработчик исключений для REST-контроллеров приложения.
 * <p>
 * Класс {@link ru.practicum.shareit.advice.GlobalExceptionHandler} перехватывает различные исключения,
 * возникающие в процессе обработки HTTP-запросов, и формирует
 * информативные HTTP-ответы с соответствующими статусами и сообщениями об ошибках.
 * <p>
 * Обрабатываемые исключения включают в себя:
 * <ul>
 *     <li>Ошибки валидации входных данных {@link org.springframework.web.bind.MethodArgumentNotValidException}</li>
 *     <li>Исключения, связанные с бронированиями, такие как
 *     {@link ru.practicum.shareit.booking.exception.BookingNotFoundException},
 *     {@link ru.practicum.shareit.booking.exception.StateNotFoundException},
 *     {@link ru.practicum.shareit.booking.exception.InvalidBookingBookerOrOwner} и др.</li>
 *     <li>Исключения, связанные с предметами, например
 *     {@link ru.practicum.shareit.item.exception.ItemNotFoundException},
 *     {@link ru.practicum.shareit.item.exception.InvalidBookerException} и др.</li>
 *     <li>Исключения, связанные с пользователями, такие как
 *     {@link ru.practicum.shareit.user.exception.EmailExistsException},
 *     {@link ru.practicum.shareit.user.exception.UserNotFoundException}</li>
 * </ul>
 * <p>
 * Каждый обработчик исключения формирует JSON-ответ с полями:
 * <ul>
 *     <li>timestamp — время возникновения ошибки</li>
 *     <li>status — HTTP-статус ошибки</li>
 *     <li>errorMessages или error — описание ошибки</li>
 * </ul>
 */
package ru.practicum.shareit.advice;
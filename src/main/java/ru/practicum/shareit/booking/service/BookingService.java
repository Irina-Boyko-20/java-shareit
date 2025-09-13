package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * Интерфейс сервиса для управления бронированиями.
 * <p>
 * Определяет методы для создания, подтверждения и поиска бронирований.
 * </p>
 */
public interface BookingService {

    /**
     * Создает новое бронирование.
     *
     * @param userId     идентификатор пользователя.
     * @param bookingDto DTO бронирования.
     * @return DTO ответа бронирования.
     */
    BookingResponseDto create(Long userId, BookingDto bookingDto);

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param userId    идентификатор пользователя.
     * @param bookingId идентификатор бронирования.
     * @param approved  флаг подтверждения.
     * @return DTO ответа бронирования.
     */
    BookingResponseDto approve(Long userId, Long bookingId, Boolean approved);

    /**
     * Получает бронирование по бронирующему.
     *
     * @param bookerId  идентификатор бронирующего.
     * @param bookingId идентификатор бронирования.
     * @return DTO ответа бронирования.
     */
    BookingResponseDto getBookingByBooker(Long bookerId, Long bookingId);

    /**
     * Получает бронирования по владельцу с фильтром.
     *
     * @param ownerId идентификатор владельца.
     * @param state   состояние для фильтрации.
     * @return список DTO бронирований.
     */
    List<BookingResponseDto> getBookingByOwner(Long ownerId, String state);

    /**
     * Получает все бронирования пользователя с фильтром.
     *
     * @param userId идентификатор пользователя.
     * @param state  состояние для фильтрации.
     * @return список DTO бронирований.
     */
    List<BookingResponseDto> getAllBookingUserById(Long userId, String state);

    /**
     * Проверяет существование бронирования.
     *
     * @param bookingId идентификатор бронирования.
     * @return объект Booking.
     */
    Booking bookingExists(Long bookingId);
}

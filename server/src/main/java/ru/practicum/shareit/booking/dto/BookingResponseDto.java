package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

/**
 * DTO для ответа с данными бронирования.
 * <p>
 * Этот record используется для сериализации данных бронирования в JSON-ответ.
 * Содержит информацию о вещи, датах, бронирующем и статусе.
 * </p>
 */
public record BookingResponseDto(
        Long id,
        ItemResponseDto item,
        LocalDateTime start,
        LocalDateTime end,
        UserResponseDto booker,
        String status) {

    /**
     * Иммутабельный DTO для представления предмета в ответе бронирования.
     * Содержит идентификатор и название предмета.
     *
     * @param id   уникальный идентификатор предмета
     * @param name название предмета
     */
    public record ItemResponseDto(Long id, String name) {
    }

    /**
     * Иммутабельный DTO для представления пользователя в ответе бронирования.
     * Содержит идентификатор пользователя.
     *
     * @param id уникальный идентификатор пользователя
     */
    public record UserResponseDto(Long id) {
    }
}

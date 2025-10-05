package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

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
}

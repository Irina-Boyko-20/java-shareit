package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

/**
 * DTO для подробного ответа с информацией о предмете.
 * <p>
 * Включает основную информацию о предмете, владельце, комментариях,
 * а также последние и следующие бронирования (если доступно).
 */
public record ItemDetailedResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        UserResponseDto owner,
        Long requestId,
        List<CommentResponseDto> comments,
        Booking lastBooking,
        Booking nextBooking) {
}

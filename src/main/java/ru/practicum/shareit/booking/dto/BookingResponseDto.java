package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

public record BookingResponseDto(
        Long id,
        LocalDateTime start,
        LocalDateTime end,
        ItemResponseDto item,
        UserDto booker,
        String status) {
}

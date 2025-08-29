package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.dto.UserDto;

public record ItemResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        UserDto owner,
        Long requestId
) {
}

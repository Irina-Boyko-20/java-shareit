package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.dto.UserResponseDto;

/**
 * DTO для ответа с базовой информацией о предмете.
 * <p>
 * Используется для возврата данных о предмете в списках или кратких ответах.
 */
public record ItemResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        UserResponseDto owner,
        Long requestId
) {
}

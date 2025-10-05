package ru.practicum.shareit.item.dto;

/**
 * DTO для запроса на обновление предмета.
 * <p>
 * Используется для частичного обновления полей предмета.
 * Все поля опциональны (могут быть null).
 */
public record ItemUpdateRequestDto(
        String name,
        String description,
        Boolean available) {
}

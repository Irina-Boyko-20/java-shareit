package ru.practicum.shareit.item.dto;

/**
 * DTO для краткой информации о предмете (Item).
 * <p>
 * Используется для передачи данных в ответах API, где не требуется полная информация о предмете.
 */
public record ItemShortDto(Long id, String name, Long ownerId) {
}

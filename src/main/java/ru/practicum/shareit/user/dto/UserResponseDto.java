package ru.practicum.shareit.user.dto;

/**
 * DTO для передачи данных пользователя в ответах API.
 *
 * @param id    Идентификатор пользователя.
 * @param name  Имя пользователя.
 * @param email Электронная почта пользователя.
 */
public record UserResponseDto(Long id, String name, String email) {
}

package ru.practicum.shareit.user.dto;

/**
 * DTO для передачи данных пользователя при частичном обновлении.
 *
 * @param name  Новое имя пользователя.
 * @param email Новая электронная почта пользователя.
 */
public record UserUpdateRequestDto(
        String name,
        String email) {
}

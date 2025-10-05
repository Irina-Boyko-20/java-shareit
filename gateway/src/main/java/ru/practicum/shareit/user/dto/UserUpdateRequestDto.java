package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import ru.practicum.shareit.validation.Update;

/**
 * DTO для передачи данных пользователя при частичном обновлении.
 *
 * @param name  Новое имя пользователя (необязательно).
 * @param email Новая электронная почта пользователя (необязательно).
 */
public record UserUpdateRequestDto(
        String name,
        @Email(groups = {Update.class}) String email) {
}

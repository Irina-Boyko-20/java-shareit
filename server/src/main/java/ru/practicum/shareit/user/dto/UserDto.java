package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO для передачи данных пользователя при создании.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    /**
     * Идентификатор пользователя.
     */
    Long id;

    /**
     * Имя пользователя.
     */
    String name;

    /**
     * Электронная почта пользователя.
     */
    String email;
}

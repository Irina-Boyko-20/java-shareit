package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

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
    @NotBlank(groups = {Create.class})
    String name;

    /**
     * Электронная почта пользователя.
     */
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    String email;
}

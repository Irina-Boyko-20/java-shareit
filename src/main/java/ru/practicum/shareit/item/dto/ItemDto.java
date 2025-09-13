package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Create;

/**
 * DTO для создания или обновления предмета.
 * <p>
 * Используется для передачи данных о предмете от клиента.
 * Поля name, description и available валидируются для группы Create.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    /** Уникальный идентификатор предмета (может быть null при создании). */
    Long id;

    /**
     * Название предмета.
     * Не может быть пустым (валидация для группы Create).
     */
    @NotBlank(groups = {Create.class})
    String name;

    /**
     * Описание предмета.
     * Не может быть пустым (валидация для группы Create).
     */
    @NotBlank(groups = {Create.class})
    String description;

    /**
     * Доступность предмета для аренды.
     * Не может быть null (валидация для группы Create).
     */
    @NotNull(groups = {Create.class})
    Boolean available;
}

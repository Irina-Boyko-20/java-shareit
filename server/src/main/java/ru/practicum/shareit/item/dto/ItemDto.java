package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * DTO для создания или обновления предмета.
 * <p>
 * Используется для передачи данных о предмете от клиента.
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
     */
    String name;

    /**
     * Описание предмета.
     */
    String description;

    /**
     * Доступность предмета для аренды.
     */
    Boolean available;

    /**
     * Идентификатор запроса предмета.
     */
    public Long requestId;
}

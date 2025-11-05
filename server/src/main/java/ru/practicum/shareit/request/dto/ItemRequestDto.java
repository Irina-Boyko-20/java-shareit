package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * DTO для представления запроса на предмет (ItemRequest).
 * Этот класс используется для передачи данных о запросе между слоями приложения,
 * такими как контроллер и сервис.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {

    /**
     * Уникальный идентификатор запроса на предмет.
     */
    Long id;

    /**
     * Описание запроса на предмет.
     */
    String description;

    /**
     * Дата и время создания запроса.
     */
    LocalDateTime created;
}

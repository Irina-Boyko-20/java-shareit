package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для передачи подробной информации о запросе на предмет.
 *
 * @param id          уникальный идентификатор запроса
 * @param description описание запроса
 * @param created     дата и время создания запроса
 * @param items       список кратких данных по предметам, связанным с запросом
 */
public record ItemRequestDetailsDto(
        Long id,
        String description,
        LocalDateTime created,
        List<ItemShortDto> items) {
}

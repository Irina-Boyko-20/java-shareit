package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о комментарии.
 * <p>
 * Используется для возврата данных о комментарии клиенту,
 * включая ID, текст, имя автора и время создания.
 */
public record CommentResponseDto(
        Long id,
        String text,
        String authorName,
        LocalDateTime created
) {
}

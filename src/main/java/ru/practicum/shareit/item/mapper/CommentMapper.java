package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Маппер для преобразования комментариев между сущностями и DTO.
 * <p>
 * Использует MapStruct для автоматического маппинга.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразует данные в сущность Comment.
     *
     * @param item Предмет, к которому относится комментарий.
     * @param authorName Имя автора.
     * @param text Текст комментария.
     * @return Сущность Comment (ID игнорируется).
     */
    @Mapping(target = "id", ignore = true)
    Comment toComment(Item item, String authorName, String text);

    /**
     * Преобразует сущность Comment в DTO для ответа.
     *
     * @param comment Сущность комментария.
     * @return DTO CommentResponseDto.
     */
    CommentResponseDto toCommentDto(Comment comment);

    /**
     * Преобразует список сущностей Comment в список DTO.
     *
     * @param comments Список сущностей.
     * @return Список DTO CommentResponseDto.
     */
    List<CommentResponseDto> mapToCommentDto(Iterable<Comment> comments);
}

package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

/**
 * Интерфейс для маппинга между сущностями ItemRequest и их DTO.
 * <p>
 * Используется MapStruct для автоматической генерации реализаций маппинга.
 */
@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface ItemRequestMapper {

    /**
     * Преобразует объект ItemRequest в ItemRequestDto, включая все связанные поля.
     *
     * @param itemRequest объект сущности ItemRequest для преобразования
     */
    ItemRequestDto toItemRequestAll(ItemRequest itemRequest);

    /**
     * Преобразует объект ItemRequestDto в ItemRequest.
     * Поля "requestor" и "items" игнорируются при маппинге, так как они могут быть установлены отдельно
     * или не требуются в данном контексте (например, при создании нового запроса).
     *
     * @param newItemRequestDto объект DTO ItemRequestDto для преобразования
     */
    @Mapping(target = "requestor", ignore = true)
    @Mapping(target = "items", ignore = true)
    ItemRequest toItemRequest(ItemRequestDto newItemRequestDto);

    /**
     * Преобразует объект ItemRequest в ItemRequestDto.
     *
     * @param itemRequest объект сущности ItemRequest для преобразования
     */
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);
}

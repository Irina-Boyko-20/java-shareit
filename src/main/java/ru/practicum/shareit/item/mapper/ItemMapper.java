package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingTarget;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDetailedResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

/**
 * Маппер для преобразования предметов между сущностями и DTO.
 * <p>
 * Использует MapStruct для автоматического маппинга.
 */
@Mapper(componentModel = "spring", uses = {CommentMapper.class, UserMapper.class, Booking.class})
public interface ItemMapper {

    /**
     * Преобразует сущность Item в DTO для ответа.
     *
     * @param item Сущность предмета.
     * @return DTO ItemResponseDto.
     */
    @Mapping(source = "request.id", target = "requestId")
    ItemResponseDto toItemDto(Item item);

    /**
     * Преобразует DTO в сущность Item.
     *
     * @param itemDto DTO предмета.
     * @return Сущность Item.
     */
    Item toItem(ItemDto itemDto);

    /**
     * Обновляет сущность Item на основе DTO запроса.
     * Игнорирует null-значения.
     *
     * @param item Сущность для обновления.
     * @param updateItem DTO с обновлениями.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "request", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromRequest(@MappingTarget Item item, ItemUpdateRequestDto updateItem);

    /**
     * Преобразует сущность Item в подробный DTO с бронированиями.
     *
     * @param item Сущность предмета.
     * @param lastBooking Последнее бронирование.
     * @param nextBooking Следующее бронирование.
     * @return DTO ItemDetailedResponseDto.
     */
    @Mapping(target = "id", source = "item.id")
    ItemDetailedResponseDto toDetailedResponseDto(Item item, Booking lastBooking, Booking nextBooking);

    /**
     * Преобразует список сущностей Item в список DTO.
     *
     * @param items Список сущностей.
     * @return Список DTO ItemResponseDto.
     */
    List<ItemResponseDto> mapToItemDto(Iterable<Item> items);
}

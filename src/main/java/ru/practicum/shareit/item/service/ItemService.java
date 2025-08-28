package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemResponseDto createItem(Long ownerId, ItemDto itemDto);

    ItemResponseDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    ItemResponseDto findById(Long itemId);

    List<ItemResponseDto> findAllItems(Long ownerId);

    Item itemExists(Long id);

    List<ItemResponseDto> searchItems(String text);

    void deleteItem(Long itemId);
}

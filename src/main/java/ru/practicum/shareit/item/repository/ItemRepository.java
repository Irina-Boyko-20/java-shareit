package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Item update(Long itemId, Long ownerId, ItemDto itemDto);

    Collection<Item> findAllItems();

    Optional<Item> findById(Long id);

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> findAvailableItems();

    List<Item> searchItems(String text);

    void delete(Long id);

}

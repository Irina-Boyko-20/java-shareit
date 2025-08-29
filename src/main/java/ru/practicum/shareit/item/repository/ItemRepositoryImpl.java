package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NotOwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> storage = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long itemId, Long ownerId, ItemDto itemDto) {
        Optional<Item> itemOptional = findById(itemId);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            if (!item.getOwner().getId().equals(ownerId)) {
                throw new NotOwnerException(ownerId, itemId);
            }

            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }

            return item;
        }
        throw new ItemNotFoundException(itemId);
    }

    @Override
    public Collection<Item> findAllItems() {
        return storage.values();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return storage.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findAvailableItems() {
        return storage.values().stream()
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return storage.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    private long getNextId() {
        long currentMaxId = storage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

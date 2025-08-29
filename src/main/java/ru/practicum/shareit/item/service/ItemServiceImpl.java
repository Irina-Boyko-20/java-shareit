package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NullOrEmptyException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    public final ItemRepository itemRepository;
    public final ItemMapper itemMapper;

    @Override
    public ItemResponseDto createItem(Long ownerId, ItemDto itemDto) {
        User owner = userService.userExists(ownerId);
        if (itemDto.getAvailable() == null) {
            throw new NullOrEmptyException("The Available field cannot be null");
        }
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        return itemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemResponseDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = itemExists(itemId);
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new ItemNotFoundException(ownerId);
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

        Item updatedItem = itemRepository.create(item);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemResponseDto findById(Long itemId) {
        return itemMapper.toItemDto(itemExists(itemId));
    }

    @Override
    public List<ItemResponseDto> findAllItems(Long ownerId) {
        return itemRepository.findAllByOwnerId(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> searchItems(String text) {
        return itemRepository.searchItems(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item itemExists(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.delete(itemId);
    }
}

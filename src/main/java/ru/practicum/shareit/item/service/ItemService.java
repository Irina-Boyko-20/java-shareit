package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemDetailedResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс сервиса для работы с предметами.
 * <p>
 * Определяет методы для управления предметами и комментариями.
 */
public interface ItemService {

    /**
     * Создает новый предмет.
     *
     * @param ownerId ID владельца.
     * @param itemDto DTO с данными предмета.
     * @return DTO с созданным предметом.
     */
    ItemResponseDto createItem(Long ownerId, ItemDto itemDto);

    /**
     * Обновляет предмет.
     *
     * @param ownerId ID владельца.
     * @param itemId ID предмета.
     * @param itemUpdateRequestDto DTO с обновлениями.
     * @return DTO с обновленным предметом.
     */
    ItemResponseDto updateItem(Long ownerId, Long itemId, ItemUpdateRequestDto itemUpdateRequestDto);

    /**
     * Получает подробную информацию о предмете.
     *
     * @param itemId ID предмета.
     * @param userId ID пользователя.
     * @return DTO с подробной информацией.
     */
    ItemDetailedResponseDto getById(Long itemId, Long userId);

    /**
     * Получает все предметы владельца.
     *
     * @param ownerId ID владельца.
     * @return Список DTO предметов.
     */
    List<ItemResponseDto> getAllItemsOwner(Long ownerId);

    /**
     * Проверяет существование предмета и возвращает его.
     *
     * @param id ID предмета.
     * @return Сущность предмета.
     * @throws ItemNotFoundException если предмет не найден.
     */
    Item itemExists(Long id);

    /**
     * Ищет доступные предметы по тексту.
     *
     * @param text Текст для поиска.
     * @return Список DTO предметов.
     */
    List<ItemResponseDto> searchItems(String text);


    /**
     * Удаляет предмет.
     *
     * @param itemId ID предмета.
     */
    void deleteItem(Long itemId);

    /**
     * Добавляет комментарий к предмету.
     *
     * @param reviewerId ID автора.
     * @param itemId ID предмета.
     * @param commentDto DTO с комментарием.
     * @return DTO с созданным комментарием.
     */
    CommentResponseDto addComment(Long reviewerId, Long itemId, CommentDto commentDto);
}

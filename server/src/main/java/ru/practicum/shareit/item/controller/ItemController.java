package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.item.dto.ItemDetailedResponseDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * REST-контроллер для управления предметами (items) в приложении ShareIt.
 * <p>
 * Этот контроллер обрабатывает HTTP-запросы для операций с предметами,
 * такими как создание, обновление, получение, поиск и добавление комментариев.
 * Использует заголовок "X-Sharer-User-Id" для идентификации пользователя.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    /** Заголовок для передачи ID пользователя. */
    private static final String userIdHeader = "X-Sharer-User-Id";

    /**
     * Константа, представляющая шаблон пути для параметра itemId в URL эндпоинтов.
     */
    private final String itemId = "/{itemId}";

    /** Сервис для работы с предметами. */
    private final ItemService itemService;

    /**
     * Создает новый предмет.
     *
     * @param ownerId ID владельца предмета (из заголовка).
     * @param itemDto DTO с данными для создания предмета.
     * @return ResponseEntity с созданным предметом и статусом 201 Created.
     */
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@RequestHeader(userIdHeader) Long ownerId,
                                                      @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.create(ownerId, itemDto), HttpStatus.CREATED);
    }

    /**
     * Обновляет существующий предмет.
     *
     * @param ownerId ID владельца предмета (из заголовка).
     * @param itemId ID предмета для обновления.
     * @param itemUpdateRequestDto DTO с данными для обновления.
     * @return ResponseEntity с обновленным предметом и статусом 200 OK.
     */
    @PatchMapping(itemId)
    public ResponseEntity<ItemResponseDto> updateItem(@RequestHeader(userIdHeader) Long ownerId,
                                                      @PathVariable Long itemId,
                                                      @RequestBody ItemUpdateRequestDto itemUpdateRequestDto) {
        return ResponseEntity.ok(itemService.update(ownerId, itemId, itemUpdateRequestDto));
    }

    /**
     * Получает подробную информацию о предмете.
     *
     * @param itemId ID предмета.
     * @param bookerId ID пользователя (из заголовка), для которого показываются бронирования.
     * @return ResponseEntity с подробным DTO предмета и статусом 200 OK.
     */
    @GetMapping(itemId)
    public ResponseEntity<ItemDetailedResponseDto> getById(@PathVariable Long itemId,
                                                           @RequestHeader(userIdHeader) Long bookerId) {
        return ResponseEntity.ok(itemService.getById(itemId, bookerId));
    }

    /**
     * Получает список всех предметов владельца.
     *
     * @param ownerId ID владельца (из заголовка).
     * @return ResponseEntity со списком предметов и статусом 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItemsOwner(@RequestHeader(userIdHeader) Long ownerId) {
        return ResponseEntity.ok(itemService.getAllItemsOwner(ownerId));
    }

    /**
     * Ищет доступные предметы по тексту.
     *
     * @param text Текст для поиска (в имени или описании).
     * @return Список найденных предметов.
     */
    @GetMapping("/search")
    public List<ItemResponseDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    /**
     * Удаляет предмет по ID.
     *
     * @param itemId ID предмета для удаления.
     */
    @DeleteMapping(itemId)
    public void deleteItem(@PathVariable Long itemId) {
        itemService.delete(itemId);
    }

    /**
     * Добавляет комментарий к предмету.
     *
     * @param authorId ID автора комментария (из заголовка).
     * @param itemId ID предмета.
     * @param commentDto DTO с текстом комментария.
     * @return ResponseEntity с созданным комментарием и статусом 201 Created.
     */
    @PostMapping(itemId + "/comment")
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestHeader(userIdHeader) Long authorId,
            @PathVariable Long itemId,
            @RequestBody CommentDto commentDto
    ) {
        CommentResponseDto responseDto = itemService.addComment(itemId, authorId, commentDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}

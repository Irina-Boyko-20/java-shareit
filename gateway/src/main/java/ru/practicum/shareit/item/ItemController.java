package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.validation.Create;

/**
 * REST-контроллер для управления предметами (items) в приложении ShareIt.
 * <p>
 * Этот контроллер обрабатывает HTTP-запросы для операций с предметами,
 * такими как создание, обновление, получение, поиск и добавление комментариев.
 * Использует заголовок "X-Sharer-User-Id" для идентификации пользователя.
 * Все запросы принимают и возвращают объекты DTO и делегируют обработку сервису {@link ItemClient}.
 */
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private static final String ITEM_ID = "/{itemId}";
    private final ItemClient itemClient;

    /**
     * Создает новый предмет.
     *
     * @param ownerId идентификатор владельца предмета, передается в заголовке {@code X-Sharer-User-Id}
     * @param request объект с данными для создания предмета, валидируется по группе {@link Create}
     * @return {@link ResponseEntity} с результатом создания предмета
     */
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(userIdHeader) Long ownerId,
                                             @Validated({Create.class}) @RequestBody ItemDto request) {
        log.info("Gateway: create item ownerId={}, request={}", ownerId, request);
        return itemClient.create(ownerId, request);
    }

    /**
     * Обновляет данные существующего предмета.
     *
     * @param ownerId идентификатор владельца, передается в заголовке {@code X-Sharer-User-Id}
     * @param itemId  идентификатор предмета для обновления
     * @param request объект с данными для обновления предмета
     * @return {@link ResponseEntity} с результатом обновления предмета
     */
    @PatchMapping(ITEM_ID)
    public ResponseEntity<Object> updateItem(@RequestHeader(userIdHeader) Long ownerId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemUpdateDto request) {
        log.info("Gateway: update item itemId={}, ownerId={}, request={}", itemId, ownerId, request);
        return itemClient.update(ownerId, itemId, request);
    }

    /**
     * Получает предмет по его идентификатору.
     *
     * @param itemId   идентификатор предмета
     * @param bookerId идентификатор пользователя, делающего запрос, передается в заголовке {@code X-Sharer-User-Id}
     * @return {@link ResponseEntity} с данными предмета
     */
    @GetMapping(ITEM_ID)
    public ResponseEntity<Object> getById(@PathVariable Long itemId,
                                          @RequestHeader(userIdHeader) Long bookerId) {
        log.info("Gateway: get item itemId={}, bookerId={}", itemId, bookerId);
        return itemClient.getById(itemId, bookerId);
    }

    /**
     * Получает список предметов, принадлежащих владельцу.
     *
     * @param ownerId идентификатор владельца, передается в заголовке {@code X-Sharer-User-Id}
     * @return {@link ResponseEntity} со списком предметов владельца
     */
    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(userIdHeader) Long ownerId) {
        log.info("Gateway: get item owner ownerId={}", ownerId);
        return itemClient.getItemsByOwner(ownerId);
    }

    /**
     * Выполняет поиск предметов по тексту.
     *
     * @param text поисковый запрос
     * @return {@link ResponseEntity} со списком предметов, удовлетворяющих поисковому запросу
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Gateway: search items text={}", text);
        return itemClient.searchItems(text);
    }

    /**
     * Удаляет предмет по идентификатору.
     *
     * @param itemId идентификатор предмета для удаления
     * @return {@link ResponseEntity} с результатом удаления
     */
    @DeleteMapping(ITEM_ID)
    public ResponseEntity<Object> deleteItem(@PathVariable Long itemId) {
        log.info("Gateway: delete itemId={}", itemId);
        return itemClient.delete(itemId);
    }

    /**
     * Добавляет комментарий к предмету.
     *
     * @param authorId идентификатор автора комментария, передается в заголовке {@code X-Sharer-User-Id}
     * @param itemId   идентификатор предмета, к которому добавляется комментарий
     * @param comment  объект комментария, валидируется по группе {@link Create}
     * @return {@link ResponseEntity} с результатом добавления комментария
     */
    @PostMapping(ITEM_ID + "/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader(userIdHeader) Long authorId,
            @PathVariable Long itemId,
            @Validated({Create.class}) @RequestBody CommentDto comment
    ) {
        log.info("Gateway: add comment itemId={}, authorId={}, comment={}", itemId, authorId, comment);
        return itemClient.addComment(itemId, authorId, comment);
    }
}

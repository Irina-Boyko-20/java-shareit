package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.validation.Create;

/**
 * REST-контроллер для управления запросами на предметы (ItemRequest).
 * Этот контроллер служит шлюзом (gateway) для обработки HTTP-запросов,
 * связанных с созданием, получением и управлением запросами на предметы.
 * Все эндпоинты начинаются с пути "/requests".
 * Контроллер использует клиент {@link ItemRequestClient} для взаимодействия с сервисом ItemRequest.
 * Валидация входных данных осуществляется с помощью аннотации {@code @Validated}.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    /**
     * Создает новый запрос на предмет.
     * Эндпоинт: POST /requests
     *
     * @param requestorId ID пользователя, создающего запрос (извлекается из HTTP-заголовка {@code X-Sharer-User-Id}).
     * @param request DTO с данными нового запроса, валидированный для создания ({@link Create}).
     * @return {@link ResponseEntity} с результатом операции (обычно созданный запрос).
     */
    @PostMapping
    public ResponseEntity<Object> addRequest(
            @RequestHeader(userIdHeader) Long requestorId,
            @Validated({Create.class}) @RequestBody NewItemRequestDto request) {
        log.info("Gateway: create request requestorId={}, request={}", requestorId, request);
        return itemRequestClient.add(requestorId, request);
    }

    /**
     * Получает все запросы, созданные указанным пользователем.
     * Эндпоинт: GET /requests
     *
     * @param requestorId ID пользователя, чьи запросы нужно получить (извлекается из HTTP-заголовка {@code X-Sharer-User-Id}).
     * @return {@link ResponseEntity} со списком запросов пользователя.
     */
    @GetMapping
    public ResponseEntity<Object> getByRequestorId(@RequestHeader(userIdHeader) Long requestorId) {
        log.info("Gateway: get request by requestor requestorId={}", requestorId);
        return itemRequestClient.getByRequestor(requestorId);
    }

    /**
     * Получает все запросы (для указанного пользователя, например, для просмотра доступных запросов).
     * Эндпоинт: GET /requests/all
     *
     * @param userId ID пользователя, для которого получаются запросы (извлекается из HTTP-заголовка {@code X-Sharer-User-Id}).
     * @return {@link ResponseEntity} со списком всех запросов.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@RequestHeader(userIdHeader) Long userId) {
        log.info("Gateway: get all requests");
        return itemRequestClient.getAll(userId);
    }

    /**
     * Получает запрос по его ID.
     * Эндпоинт: GET /requests/{requestId}
     *
     * @param requestId ID запроса, который нужно получить.
     * @return {@link ResponseEntity} с данными запроса.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId) {
        log.info("Gateway: get request requestId={}", requestId);
        return itemRequestClient.getById(requestId);
    }
}

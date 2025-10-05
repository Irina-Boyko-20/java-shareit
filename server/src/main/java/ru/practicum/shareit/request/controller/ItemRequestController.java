package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.request.dto.ItemRequestDetailsDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * REST-контроллер для управления запросами на предметы (item requests).
 * Предоставляет эндпоинты для создания, получения и просмотра запросов пользователей.
 * Использует заголовок "X-Sharer-User-Id" для идентификации пользователя-запросителя.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    /** Заголовок для передачи ID пользователя. */
    private static final String userIdHeader = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    /**
     * Добавляет новый запрос на предмет.
     *
     * @param requestorId ID пользователя-запросителя, передаваемый в заголовке "X-Sharer-User-Id".
     * @param itemRequestDto DTO с данными запроса (например, описание предмета).
     * @return ResponseEntity с созданным ItemRequestDto и статусом 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<ItemRequestDto> addRequest(
            @RequestHeader(userIdHeader) Long requestorId,
            @RequestBody ItemRequestDto itemRequestDto) {
        return new ResponseEntity<>(itemRequestService.addRequest(requestorId, itemRequestDto), HttpStatus.CREATED);
    }

    /**
     * Получает список запросов, созданных указанным пользователем-запросителем.
     *
     * @param requestorId ID пользователя-запросителя, передаваемый в заголовке "X-Sharer-User-Id".
     * @return ResponseEntity со списком ItemRequestDetailsDto (запросы с деталями) и статусом 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ItemRequestDetailsDto>> getByRequestorId(
            @RequestHeader(userIdHeader) Long requestorId
    ) {
        return ResponseEntity.ok(itemRequestService.getRequestByRequestor(requestorId));
    }

    /**
     * Получает список всех запросов на предметы (без фильтрации по пользователю).
     *
     * @return ResponseEntity со списком ItemRequestDto и статусом 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAllRequest() {
        return ResponseEntity.ok(itemRequestService.getAllRequests());
    }

    /**
     * Получает детали конкретного запроса по его ID.
     *
     * @param requestId ID запроса, передаваемый в пути URL.
     * @return ResponseEntity с ItemRequestDetailsDto (запрос с деталями) и статусом 200 (OK).
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDetailsDto> getRequestById(@PathVariable Long requestId) {
        return ResponseEntity.ok(itemRequestService.getRequestById(requestId));
    }
}

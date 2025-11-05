package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDetailsDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Сервис для управления запросами на предметы (ItemRequest).
 * Предоставляет методы для создания, получения и проверки запросов на предметы,
 * включая детализацию со связанными предметами.
 */
public interface ItemRequestService {

    /**
     * Добавляет новый запрос на предмет от указанного пользователя.
     *
     * @param requestorId идентификатор пользователя, создающего запрос
     * @param itemRequestDto DTO с данными запроса, включая описание
     */
    ItemRequestDto addRequest(Long requestorId, ItemRequestDto itemRequestDto);

    /**
     * Получает список запросов на предметы, созданных указанным пользователем,
     * включая детализацию со связанными предметами.
     *
     * @param requestorId идентификатор пользователя
     */
    List<ItemRequestDetailsDto> getRequestByRequestor(Long requestorId);

    /**
     * Получает список всех запросов на предметы, отсортированных по дате создания.
     *
     * @return список DTO всех запросов
     */
    List<ItemRequestDto> getAllRequests();

    /**
     * Получает детали конкретного запроса на предмет по его ID,
     * включая связанные предметы.
     *
     * @param requestId идентификатор запроса
     */
    ItemRequestDetailsDto getRequestById(Long requestId);

    /**
     * Проверяет существование запроса на предмет по его ID и возвращает его,
     * если он существует.
     *
     * @param id идентификатор запроса (не может быть null)
     */
    ItemRequest requestExists(Long id);
}

package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDetailsDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.DescriptionExistsException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для управления запросами на предметы (ItemRequest).
 * Реализует логику создания, получения и проверки запросов, включая связывание с предметами и пользователями.
 * Использует репозитории для работы с базой данных и маппер для преобразования объектов.
 */
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;

    /**
     * Добавляет новый запрос на предмет от пользователя.
     * Проверяет существование пользователя и уникальность описания запроса.
     *
     * @param requestorId идентификатор пользователя, создающего запрос
     * @param itemRequestDto DTO с данными запроса (описание)
     * @return DTO созданного запроса с присвоенным ID и временем создания
     * @throws UserNotFoundException если пользователь с указанным ID не найден
     * @throws DescriptionExistsException если описание запроса уже существует
     */
    @Override
    public ItemRequestDto addRequest(Long requestorId, ItemRequestDto itemRequestDto) {
        User requestor = userService.userExists(requestorId);
        descriptionExists(itemRequestDto.getDescription());
        ItemRequest request = itemRequestMapper.toItemRequest(itemRequestDto);
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());

        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(request));
    }

    /**
     * Получает список запросов, созданных указанным пользователем, отсортированных по времени создания (по убыванию).
     * Запросы включают связанные предметы.
     *
     * @param requestorId идентификатор пользователя, чьи запросы нужно получить
     * @return список DTO с деталями запросов и связанных предметов, или пустой список, если запросов нет
     * @throws UserNotFoundException если пользователь с указанным ID не найден
     */
    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDetailsDto> getRequestByRequestor(Long requestorId) {
        userService.userExists(requestorId);

        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);
        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        return mapToItemRequestDto(requests);
    }

    /**
     * Получает список всех запросов, отсортированных по времени создания (по убыванию).
     * Возвращает упрощенные DTO без связанных предметов.
     *
     * @return список DTO всех запросов
     */
    @Override
    public List<ItemRequestDto> getAllRequests() {
        return itemRequestRepository.findAllByOrderByCreatedDesc().stream()
                .map(itemRequestMapper::toItemRequestAll)
                .toList();
    }

    /**
     * Получает детали запроса по его ID, включая связанные предметы.
     *
     * @param requestId идентификатор запроса
     * @return DTO с деталями запроса и связанных предметов
     * @throws RequestNotFoundException если запрос с указанным ID не найден
     */
    @Transactional(readOnly = true)
    @Override
    public ItemRequestDetailsDto getRequestById(Long requestId) {
        List<ItemRequest> requests = Collections.singletonList(requestExists(requestId));
        if (requests.isEmpty()) {
            throw new RequestNotFoundException(requestId);
        }

        return mapToItemRequestDto(requests).getFirst();
    }

    /**
     * Проверяет существование запроса по ID и возвращает его, если найден.
     *
     * @param id идентификатор запроса
     * @return объект запроса
     * @throws RequestNotFoundException если запрос с указанным ID не найден
     */
    @Override
    public ItemRequest requestExists(Long id) {
        return itemRequestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }

    /**
     * Проверяет уникальность описания запроса.
     * Выбрасывает исключение, если описание уже существует.
     *
     * @param description описание запроса для проверки
     * @throws DescriptionExistsException если описание уже используется в другом запросе
     */
    public void descriptionExists(String description) {
        if (itemRequestRepository.existsByDescription(description)) {
            throw new DescriptionExistsException(description);
        }
    }

    /**
     * Преобразует список запросов в список DTO с деталями, включая связанные предметы.
     * Группирует предметы по ID запроса для эффективного маппинга.
     *
     * @param requests список запросов для преобразования
     * @return список DTO с деталями запросов и предметов
     */
    private List<ItemRequestDetailsDto> mapToItemRequestDto(List<ItemRequest> requests) {
        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        Map<Long, List<ItemShortDto>> itemsByRequestId = itemRepository
                .findByRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(
                        item -> item.getRequest().getId(),
                        Collectors.mapping(item -> new ItemShortDto(
                                item.getId(),
                                item.getName(),
                                item.getOwner().getId()
                        ), Collectors.toList())
                ));

        return requests.stream()
                .map(request -> new ItemRequestDetailsDto(
                        request.getId(),
                        request.getDescription(),
                        request.getCreated(),
                        itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }
}

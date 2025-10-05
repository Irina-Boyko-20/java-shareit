package ru.practicum.shareit.booking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

/**
 * Сервисный класс, выступающий клиентом для взаимодействия с API бронирований.
 * Наследуется от {@link BaseClient} и предназначен для выполнения HTTP-запросов к эндпоинтам
 * сервера ShareIt, связанным с бронированиями. Предоставляет методы для создания,
 * подтверждения и получения бронирований, поддерживая операции как для пользователей,
 * создающих бронирования, так и для владельцев.
 *
 * <p>Клиент настраивается с использованием базового URL из настроек приложения
 * ({@code shareit-server.url}) и использует {@link RestTemplate} для выполнения запросов.
 * Все запросы содержат ID пользователя в заголовках для аутентификации.</p>
 */
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    /**
     * Конструктор, создающий новый экземпляр BookingClient с указанным URL сервера и билдером RestTemplate.
     * RestTemplate настраивается с обработчиком URI-шаблонов, указывающим на эндпоинты бронирований,
     * и использует HttpComponentsClientHttpRequestFactory для фабрики запросов.
     */
    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создает новый запрос на бронирование для указанного пользователя.
     * Отправляет POST-запрос к API бронирований с деталями бронирования.
     *
     * @param userId  ID пользователя, делающего запрос на бронирование
     * @param request объект передачи данных бронирования, содержащий детали, такие как ID предмета и даты
     */
    public ResponseEntity<Object> create(long userId, BookingDto request) {
        return post("", userId, request);
    }

    /**
     * Подтверждает или отклоняет запрос на бронирование.
     * Отправляет PATCH-запрос для обновления статуса подтверждения указанного бронирования.
     *
     * @param userId    ID пользователя (владельца), выполняющего подтверждение
     * @param bookingId ID бронирования для подтверждения или отклонения
     * @param approved  true для подтверждения бронирования, false для отклонения
     */
    public ResponseEntity<Object> approve(long userId, long bookingId, boolean approved) {
        Map<String, Object> params = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, params, null);
    }

    /**
     * Получает бронирование по его ID для указанного пользователя (того, кто создал бронирование).
     * Отправляет GET-запрос для получения деталей бронирования.
     *
     * @param userId    ID пользователя (того, кто создал бронирование)
     * @param bookingId ID бронирования для получения
     */
    public ResponseEntity<Object> getByBooker(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    /**
     * Получает все бронирования для указанного владельца, отфильтрованные по состоянию.
     * Отправляет GET-запрос для получения бронирований, принадлежащих пользователю.
     *
     * @param userId ID владельца (пользователя, чьи бронирования нужно получить)
     * @param state  состояние бронирований для фильтрации (например, ALL, CURRENT, PAST)
     */
    public ResponseEntity<Object> getByOwner(long userId, BookingState state) {
        return get("/owner?state={state}", userId, Map.of("state", state));
    }

    /**
     * Получает все бронирования для указанного пользователя (того, кто создал бронирования), отфильтрованные по состоянию.
     * Отправляет GET-запрос для получения всех бронирований, созданных пользователем.
     *
     * @param userId ID пользователя (того, кто создал бронирования), чьи бронирования нужно получить
     * @param state  состояние бронирований для фильтрации (например, ALL, CURRENT, PAST)
     */
    public ResponseEntity<Object> getAll(long userId, BookingState state) {
        return get("?state={state}", userId, Map.of("state", state));
    }
}

package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

/**
 * Клиент для взаимодействия с сервером shareit-server по эндпоинтам запросов на предметы (item requests).
 * Наследуется от {@link BaseClient} и использует {@link RestTemplate} для отправки HTTP-запросов.
 * Базовый URL для запросов формируется как serverUrl + "/requests".
 */
@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    /**
     * Конструктор для создания клиента.
     * Инициализирует {@link RestTemplate} с базовым URL (serverUrl + "/requests"),
     * используя {@link HttpComponentsClientHttpRequestFactory} для обработки запросов.
     *
     * @param serverUrl URL сервера shareit-server, получаемый из конфигурации.
     * @param builder   Строитель {@link RestTemplate} для настройки клиента.
     */
    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Добавляет новый запрос на предмет от имени указанного пользователя.
     *
     * @param userId  ID пользователя, от имени которого создается запрос.
     * @param request DTO с данными нового запроса на предмет.
     */
    public ResponseEntity<Object> add(long userId, NewItemRequestDto request) {
        return post("", userId, request);
    }

    /**
     * Получает список запросов на предметы, созданных указанным пользователем-запрашивателем.
     *
     * @param requestorId ID пользователя-запрашивателя.
     */
    public ResponseEntity<Object> getByRequestor(long requestorId) {
        return get("", requestorId);
    }

    /**
     * Получает все запросы на предметы, доступные для указанного пользователя.
     *
     * @param userId ID пользователя, для которого запрашиваются все запросы.
     */
    public ResponseEntity<Object> getAll(long userId) {
        return get("/all", userId);
    }

    /**
     * Получает запрос на предмет по его ID.
     *
     * @param requestId ID запроса на предмет.
     */
    public ResponseEntity<Object> getById(long requestId) {
        return get("/" + requestId);
    }
}

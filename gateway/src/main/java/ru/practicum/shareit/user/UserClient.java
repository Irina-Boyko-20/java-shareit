package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

/**
 * Сервис-клиент для взаимодействия с API пользователей внешнего сервиса ShareIt.
 * <p>
 * Этот класс расширяет {@link BaseClient} и предоставляет методы для выполнения HTTP-запросов
 * к эндпоинтам пользователей (с префиксом "/users"). Он использует {@link RestTemplate} для отправки
 * запросов на сервер, URL которого задается через конфигурацию ({@code shareit-server.url}).
 * </p>
 * <p>
 * Все методы возвращают {@link ResponseEntity<Object>}, содержащий ответ от сервера (например,
 * данные пользователя или статус операции).
 * </p>
 */
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    /**
     * Конструктор для создания экземпляра {@link UserClient}.
     * <p>
     * Инициализирует базовый {@link RestTemplate} с URI-шаблоном, включающим базовый URL сервера
     * и префикс "/users". Использует {@link HttpComponentsClientHttpRequestFactory} для обработки
     * HTTP-запросов.
     * </p>
     *
     * @param serverUrl базовый URL сервера ShareIt, к которому будут отправляться запросы.
     * @param builder   строитель {@link RestTemplate} для настройки клиента.
     */
    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создает нового пользователя путем отправки POST-запроса на сервер.
     * <p>
     * Запрос отправляется на корневой путь (""), что соответствует созданию пользователя.
     * Тело запроса содержит данные пользователя в формате {@link UserDto}.
     * </p>
     *
     * @param request объект {@link UserDto} с данными для создания пользователя
     *                (например, имя, email и т.д.).
     */
    public ResponseEntity<Object> create(UserDto request) {
        return post("", request);
    }

    /**
     * Обновляет данные существующего пользователя путем отправки PATCH-запроса на сервер.
     * <p>
     * Запрос отправляется на путь "/{userId}", где {userId} - идентификатор пользователя.
     * Тело запроса содержит обновляемые данные в формате {@link UserUpdateRequestDto}.
     * </p>
     *
     * @param userId  уникальный идентификатор пользователя, данные которого нужно обновить.
     * @param request объект {@link UserUpdateRequestDto} с обновляемыми данными пользователя
     *                (например, новое имя или email).
     */
    public ResponseEntity<Object> update(long userId, UserUpdateRequestDto request) {
        return patch("/" + userId, request);
    }

    /**
     * Получает данные пользователя по его идентификатору путем отправки GET-запроса на сервер.
     * <p>
     * Запрос отправляется на путь "/{userId}", где {userId} - идентификатор пользователя.
     * </p>
     *
     * @param userId уникальный идентификатор пользователя, данные которого нужно получить.
     */
    public ResponseEntity<Object> getById(long userId) {
        return get("/" + userId);
    }

    /**
     * Удаляет пользователя по его идентификатору путем отправки DELETE-запроса на сервер.
     * <p>
     * Запрос отправляется на путь "/{userId}", где {userId} - идентификатор пользователя.
     * </p>
     *
     * @param userId уникальный идентификатор пользователя, которого нужно удалить.
     */
    public ResponseEntity<Object> delete(long userId) {
        return delete("/" + userId);
    }
}

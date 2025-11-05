package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

/**
 * Клиент для взаимодействия с API предметов (items) в приложении ShareIt.
 * Этот класс наследуется от {@link BaseClient} и предоставляет методы для выполнения HTTP-запросов
 * к серверу ShareIt для операций с предметами, такими как создание, обновление, получение, поиск и удаление.
 * Использует RestTemplate для отправки запросов на сервер, указанный в конфигурации.
 */
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    /**
     * Конструктор для инициализации клиента.
     * Настраивает RestTemplate с базовым URL сервера ShareIt и фабрикой запросов.
     *
     * @param serverUrl URL сервера ShareIt, полученный из конфигурации (например, shareit-server.url).
     * @param builder   Строитель RestTemplate для настройки клиента.
     */
    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создает новый предмет для указанного владельца.
     *
     * @param ownerId ID владельца предмета.
     * @param request DTO с данными для создания предмета.
     */
    public ResponseEntity<Object> create(long ownerId, ItemDto request) {
        return post("", ownerId, request);
    }

    /**
     * Обновляет существующий предмет.
     *
     * @param itemId  ID предмета для обновления.
     * @param ownerId ID владельца предмета (для проверки прав).
     * @param request DTO с данными для обновления предмета.
     */
    public ResponseEntity<Object> update(long itemId, long ownerId, ItemUpdateDto request) {
        return patch("/" + itemId, ownerId, request);
    }

    /**
     * Получает предмет по его ID для указанного пользователя.
     *
     * @param itemId ID предмета.
     * @param userId ID пользователя, запрашивающего предмет (для проверки прав доступа).
     * @return ResponseEntity с данными предмета или ошибкой.
     */
    public ResponseEntity<Object> getById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    /**
     * Получает список предметов, принадлежащих указанному владельцу.
     *
     * @param ownerId ID владельца предметов.
     * @return ResponseEntity со списком предметов или ошибкой.
     */
    public ResponseEntity<Object> getItemsByOwner(long ownerId) {
        return get("", ownerId);
    }

    /**
     * Ищет предметы по текстовому запросу.
     *
     * @param text Текст для поиска (например, в названии или описании предмета).
     */
    public ResponseEntity<Object> searchItems(String text) {
        return get("/search?text={text}", Map.of("text", text));
    }

    /**
     * Удаляет предмет по его ID.
     *
     * @param itemId ID предмета для удаления.
     */
    public ResponseEntity<Object> delete(long itemId) {
        return delete("/" + itemId);
    }

    /**
     * Добавляет комментарий к предмету от имени пользователя.
     *
     * @param itemId  ID предмета, к которому добавляется комментарий.
     * @param userId  ID пользователя, оставляющего комментарий.
     * @param comment DTO с данными комментария.
     */
    public ResponseEntity<Object> addComment(long itemId, long userId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}

package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

/**
 * REST-контроллер для управления пользователями в приложении.
 * Этот контроллер выступает в роли шлюза (gateway), проксируя запросы к сервису {@link UserClient},
 * который взаимодействует с внешним API или сервисом пользователей.
 * Контроллер поддерживает создание, обновление, получение и удаление пользователей,
 * с валидацией входных данных и логированием операций.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    /**
     * Создает нового пользователя на основе предоставленных данных.
     * Выполняет валидацию входного DTO с использованием группы {@code Create}.
     * Логирует запрос для отладки.
     *
     * @param request DTO с данными для создания пользователя, валидируемый по группе {@code Create}
     * @return {@link ResponseEntity} с результатом операции (например, созданный пользователь или ошибка)
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@Validated({Create.class}) @RequestBody UserDto request) {
        log.info("Gateway: create user request={}", request);
        return userClient.create(request);
    }

    /**
     * Обновляет данные существующего пользователя по его идентификатору.
     * Выполняет валидацию входного DTO с использованием группы {@code Update}.
     * Логирует идентификатор пользователя и запрос для отладки.
     *
     * @param userId идентификатор пользователя, данные которого нужно обновить
     * @param request DTO с данными для обновления пользователя, валидируемый по группе {@code Update}
     * @return {@link ResponseEntity} с результатом операции (например, обновленный пользователь или ошибка)
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Validated({Update.class}) @PathVariable Long userId,
                                             @RequestBody UserUpdateRequestDto request) {
        log.info("Gateway: update userId={}, request={}", userId, request);
        return userClient.update(userId, request);
    }

    /**
     * Получает данные пользователя по его идентификатору.
     * Логирует идентификатор пользователя для отладки.
     *
     * @param userId идентификатор пользователя, данные которого нужно получить
     * @return {@link ResponseEntity} с данными пользователя или ошибкой, если пользователь не найден
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        log.info("Gateway: get user userId={}", userId);
        return userClient.getById(userId);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     * Логирует идентификатор пользователя для отладки.
     *
     * @param userId идентификатор пользователя, которого нужно удалить
     * @return {@link ResponseEntity} с результатом операции (например, подтверждение удаления или ошибка)
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Gateway: delete userId={}", userId);
        return userClient.delete(userId);
    }
}

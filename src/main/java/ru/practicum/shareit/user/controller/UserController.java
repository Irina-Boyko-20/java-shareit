package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

/**
 * REST-контроллер для управления пользователями.
 * Предоставляет API для создания, обновления, получения и удаления пользователей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    /**
     * Создает нового пользователя.
     *
     * @param userDto DTO с данными пользователя для создания.
     * @return DTO с данными созданного пользователя.
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.create(userDto), HttpStatus.CREATED);
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param id Идентификатор пользователя.
     * @param userUpdate DTO с данными для обновления.
     * @return DTO с обновленными данными пользователя.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@Validated({Update.class}) @PathVariable Long id,
                                                      @RequestBody UserUpdateRequestDto userUpdate) {
        return ResponseEntity.ok(userService.update(id, userUpdate));
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return DTO с данными пользователя.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}

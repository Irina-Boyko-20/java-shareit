package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.exception.EmailExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

/**
 * Сервисный интерфейс для работы с пользователями.
 */
public interface UserService {

    /**
     * Создает нового пользователя.
     *
     * @param userDto DTO с данными пользователя.
     * @return DTO созданного пользователя.
     */
    UserResponseDto create(UserDto userDto);

    /**
     * Обновляет данные пользователя.
     *
     * @param id         Идентификатор пользователя.
     * @param userUpdate DTO с данными для обновления.
     * @return DTO обновленного пользователя.
     */
    UserResponseDto update(Long id, UserUpdateRequestDto userUpdate);

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return DTO пользователя.
     */
    UserResponseDto findById(Long id);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     */
    void delete(Long id);

    /**
     * Проверяет существование пользователя по идентификатору и возвращает сущность.
     *
     * @param id Идентификатор пользователя.
     * @return сущность пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     */
    User userExists(Long id);

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email Электронная почта.
     * @throws EmailExistsException если email уже используется.
     */
    void emailExists(String email);
}

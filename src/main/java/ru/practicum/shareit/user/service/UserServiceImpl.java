package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.exception.EmailExistsException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

/**
 * Реализация сервиса для работы с пользователями.
 * Содержит бизнес-логику для создания, обновления, поиска и удаления пользователей.
 * Использует транзакции для обеспечения целостности данных.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Создает нового пользователя на основе предоставленных данных.
     * Преобразует DTO в сущность, сохраняет в репозитории и возвращает DTO ответа.
     *
     * @param userDto DTO с данными пользователя для создания.
     * @return DTO с данными созданного пользователя.
     */
    @Override
    public UserResponseDto create(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    /**
     * Обновляет данные существующего пользователя.
     * Проверяет существование пользователя и уникальность email перед обновлением.
     *
     * @param id         Идентификатор пользователя для обновления.
     * @param userUpdate DTO с данными для обновления.
     * @return DTO с обновленными данными пользователя.
     */
    @Override
    @Transactional
    public UserResponseDto update(Long id, UserUpdateRequestDto userUpdate) {
        User user = userExists(id);
        emailExists(userUpdate.email());

        userMapper.updateUserRequest(user, userUpdate);
        return userMapper.toUserDto(userRepository.save(user));
    }

    /**
     * Находит пользователя по идентификатору и возвращает его данные.
     *
     * @param id Идентификатор пользователя.
     * @return DTO с данными найденного пользователя.
     */
    @Override
    public UserResponseDto findById(Long id) {
        return userMapper.toUserDto(userExists(id));
    }

    /**
     * Удаляет пользователя по идентификатору после проверки его существования.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    @Override
    public void delete(Long id) {
        userExists(id);
        userRepository.deleteById(id);
    }

    /**
     * Проверяет существование пользователя по идентификатору и возвращает сущность.
     * Выбрасывает исключение, если пользователь не найден.
     *
     * @param id Идентификатор пользователя.
     * @return сущность пользователя.
     * @throws UserNotFoundException если пользователь с указанным ID не существует.
     */
    @Override
    public User userExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Проверяет уникальность email.
     * Выбрасывает исключение, если email уже используется другим пользователем.
     *
     * @param email Электронная почта для проверки.
     * @throws EmailExistsException если email уже существует.
     */
    @Override
    public void emailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException(email);
        }
    }
}

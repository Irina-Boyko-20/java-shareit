package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    Collection<UserDto> findAllUsers();

    UserDto findById(Long id);

    void delete(Long id);

    User userExists(Long id);
}

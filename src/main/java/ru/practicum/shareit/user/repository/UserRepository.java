package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user);

    Collection<User> findAllUsers();

    Optional<User> findById(Long id);

    void delete(Long id);
}

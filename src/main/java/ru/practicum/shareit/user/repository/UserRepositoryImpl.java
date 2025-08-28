package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.NonExistentEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public User create(User user) {
        if (validateEmail(user.getId(), user.getEmail())) {
            throw new NonExistentEmailException(user.getEmail());
        }
        user.setId(getNextId());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (validateEmail(user.getId(), user.getEmail())) {
            throw new NonExistentEmailException(user.getEmail());
        }

        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public Collection<User> findAllUsers() {
        return storage.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public boolean validateEmail(Long id, String email) {
        return storage.values().stream()
                .anyMatch(u -> !u.getId().equals(id) && u.getEmail().equalsIgnoreCase(email));
    }

    private long getNextId() {
        long currentMaxId = storage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Репозиторий для работы с сущностью User.
 * Расширяет JpaRepository для CRUD операций.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Проверяет, существует ли пользователь с указанной электронной почтой.
     *
     * @param email Электронная почта.
     * @return true, если пользователь с таким email существует.
     */
    boolean existsByEmail(String email);
}

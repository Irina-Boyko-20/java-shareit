package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

/**
 * Репозиторий для работы с комментариями.
 * <p>
 * Предоставляет стандартные методы JPA для CRUD-операций с комментариями.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}

package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link ItemRequest}.
 * Расширяет {@link JpaRepository} для базовых операций CRUD.
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    /**
     * Находит все запросы предметов, созданные пользователем с указанным идентификатором,
     * отсортированные по дате создания в порядке убывания.
     *
     * @param requestorId идентификатор пользователя-запрашивающего
     */
    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Long requestorId);

    /**
     * Находит все запросы предметов, отсортированные по дате создания в порядке убывания.
     */
    List<ItemRequest> findAllByOrderByCreatedDesc();

    /**
     * Проверяет, существует ли запрос предмета с указанным описанием.
     *
     * @param description описание запроса предмета
     */
    boolean existsByDescription(String description);
}

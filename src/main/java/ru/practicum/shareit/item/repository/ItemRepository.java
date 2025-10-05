package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Репозиторий для работы с предметами.
 * <p>
 * Предоставляет стандартные методы JPA и кастомные запросы для предметов.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Находит все предметы по ID владельца.
     *
     * @param userId ID владельца.
     * @return Список предметов.
     */
    List<Item> findByOwnerId(Long userId);

    /**
     * Находит доступные предметы, содержащие текст в имени или описании.
     *
     * @param name Текст для поиска.
     * @return Список доступных предметов.
     */
    @Query("from Item as it " +
            "where it.available = true " +
            "and (lower(it.name) like lower(concat('%',?1,'%')) " +
            "or lower(it.description) like lower(concat('%',?1,'%')))")
    List<Item> findAvailableItem(String name);
}

package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с бронированиями.
 * <p>
 * Предоставляет методы для поиска и фильтрации бронирований в базе данных.
 * </p>
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Находит все бронирования пользователя, отсортированные по дате начала по убыванию.
     *
     * @param userId идентификатор пользователя.
     * @return список бронирований.
     */
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    /**
     * Находит текущие бронирования пользователя.
     *
     * @param userId идентификатор пользователя.
     * @param start  дата начала.
     * @param end    дата окончания.
     * @param SORT   сортировка.
     * @return список бронирований.
     */
    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(Long userId,
                                                                 LocalDateTime start,
                                                                 LocalDateTime end,
                                                                 Sort sort);

    /**
     * Находит будущие бронирования пользователя.
     *
     * @param userId идентификатор пользователя.
     * @param start  дата начала.
     * @param SORT   сортировка.
     * @return список бронирований.
     */
    List<Booking> findAllByBookerIdAndStartIsAfter(Long userId, LocalDateTime start, Sort sort);

    /**
     * Находит прошедшие бронирования пользователя.
     *
     * @param userId идентификатор пользователя.
     * @param end    дата окончания.
     * @param sort   сортировка.
     * @return список бронирований.
     */
    List<Booking> findAllByBookerIdAndEndIsBefore(Long userId, LocalDateTime end, Sort sort);

    /**
     * Находит бронирования пользователя по статусу.
     *
     * @param userId идентификатор пользователя.
     * @param status статус бронирования.
     * @return список бронирований.
     */
    List<Booking> findAllByBookerIdAndStatus(Long userId, Status status);

    /**
     * Находит бронирование для отзыва.
     *
     * @param itemId     идентификатор предмета.
     * @param reviewerId идентификатор рецензента.
     * @param end        дата окончания.
     * @return опциональное бронирование.
     */
    Optional<Booking> findByItemIdAndBookerIdAndEndBefore(Long itemId, Long reviewerId, LocalDateTime end);

    /**
     * Находит последнее прошедшее бронирование предмета.
     *
     * @param itemId идентификатор предмета.
     * @param start  дата начала.
     * @param end    дата окончания.
     * @return опциональное бронирование.
     */
    Optional<Booking> findFirstByItemIdAndStartBeforeAndEndBeforeOrderByEndDesc(Long itemId,
                                                                                LocalDateTime start,
                                                                                LocalDateTime end);

    /**
     * Находит ближайшее будущее бронирование предмета.
     *
     * @param itemId идентификатор предмета.
     * @param start  дата начала.
     * @return опциональное бронирование.
     */
    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start);
}

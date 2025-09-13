package ru.practicum.shareit.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Сущность бронирования предмета.
 * <p>
 * Представляет бронирование в системе, включая даты, предмет, бронирующего пользователя и статус.
 * Используется для хранения данных в базе данных.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "bookings")
public class Booking {

    /**
     * Уникальный идентификатор бронирования.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    Long id;

    /**
     * Дата и время начала бронирования.
     */
    @Column(name = "start_date", nullable = false)
    LocalDateTime start;

    /**
     * Дата и время окончания бронирования.
     */
    @Column(name = "end_date", nullable = false)
    LocalDateTime end;

    /**
     * Предмет, который бронируется.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    /**
     * Пользователь, который бронирует предмет.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    User booker;

    /**
     * Статус бронирования.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;
}

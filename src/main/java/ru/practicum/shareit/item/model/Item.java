package ru.practicum.shareit.item.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность предмета (item).
 * <p>
 * Представляет предмет для аренды в базе данных.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "items")
public class Item {

    /** Уникальный идентификатор предмета. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    /** Название предмета. */
    @Column(name = "name", nullable = false)
    String name;

    /** Описание предмета. */
    @Column(name = "description", nullable = false)
    String description;

    /** Доступность предмета для аренды. */
    @Column(name = "is_available", nullable = false)
    Boolean available;

    /** Владелец предмета. */
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    /** Владелец предмета. */
    @ManyToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;

    /** Список комментариев к предмету. */
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @Builder.Default
    List<Comment> comments = new ArrayList<>();
}

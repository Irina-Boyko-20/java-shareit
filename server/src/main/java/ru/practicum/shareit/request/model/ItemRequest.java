package ru.practicum.shareit.request.model;

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
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий запрос на предметы.
 * <p>
 * Запрос на предметы содержит описание запроса, ссылку на пользователя-запросчика (requestor),
 * дату и время создания, а также список связанных предметов (items).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "requests")
public class ItemRequest {

    /**
     * Уникальный идентификатор запроса на предметы.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;

    /**
     * Описание запроса на предметы.
     */
    @Column(name = "description", nullable = false)
    String description;

    /**
     * Пользователь, который создал запрос (запросчик).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    User requestor;

    /**
     * Дата и время создания запроса.
     */
    @Column(name = "create_date", nullable = false)
    LocalDateTime created;

    /**
     * Список предметов, связанных с этим запросом.
     */
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Item> items = new ArrayList<>();
}

package ru.practicum.shareit.item.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Create;

import java.time.LocalDateTime;

/**
 * Сущность комментария к предмету.
 * <p>
 * Представляет комментарий пользователя к предмету в базе данных.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    /** Уникальный идентификатор комментария. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    Long id;

    /** Предмет, к которому относится комментарий. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    /** Текст комментария. Не может быть пустым. */
    @NotBlank(groups = {Create.class})
    @Column(name = "text", nullable = false)
    String text;

    /** Имя автора комментария. */
    @Column(name = "author_name", nullable = false)
    String authorName;

    /** Время создания комментария. По умолчанию - текущее время. */
    @Builder.Default
    @Column(name = "create_date", nullable = false)
    LocalDateTime created = LocalDateTime.now();
}

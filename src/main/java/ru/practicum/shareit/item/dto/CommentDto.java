package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Create;

/**
 * DTO для создания комментария к предмету.
 * <p>
 * Используется для передачи текста комментария от клиента.
 * Поле text валидируется: не может быть пустым и имеет максимальную длину 1500 символов.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    /**
     * Текст комментария.
     * Не может быть пустым или состоять только из пробелов (валидация для группы Create).
     * Максимальная длина - 1500 символов.
     */
    @NotBlank(groups = {Create.class})
    @Size(max = 1500)
    String text;
}

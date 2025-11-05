package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import ru.practicum.shareit.validation.Create;

/**
 * DTO для создания нового запроса на предмет (ItemRequest).
 *
 * @param description описание запроса на предмет; должно быть непустым и не состоять только из пробелов
 */
public record NewItemRequestDto(@NotBlank(groups = {Create.class}) String description) {
}

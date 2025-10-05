package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Create;

import java.time.LocalDateTime;

/**
 * DTO для создания и передачи данных бронирования.
 * <p>
 * Этот класс используется для валидации входных данных при создании бронирования.
 * Поля валидируются с помощью аннотаций Jakarta Validation.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    /**
     * ID бронирования (необязательно для создания, заполняется автоматически).
     */
    Long id;

    /**
     * Дата и время начала бронирования.
     * <p>
     * Должна быть в будущем или настоящем времени.
     * </p>
     */
    @NotNull(groups = {Create.class})
    @FutureOrPresent
    LocalDateTime start;

    /**
     * Дата и время окончания бронирования.
     * <p>
     * Должна быть в будущем или настоящем времени и позже начала.
     * </p>
     */
    @NotNull(groups = {Create.class})
    @FutureOrPresent
    LocalDateTime end;

    /**
     * ID вещи, которую бронируют.
     */
    @NotNull(groups = {Create.class})
    Long itemId;
}

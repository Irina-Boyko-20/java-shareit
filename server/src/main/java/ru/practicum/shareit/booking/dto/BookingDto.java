package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * DTO для создания и передачи данных бронирования.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    /**
     * ID бронирования.
     */
    Long id;

    /**
     * Дата и время начала бронирования.
     */
    LocalDateTime start;

    /**
     * Дата и время окончания бронирования.
     */
    LocalDateTime end;

    /**
     * ID вещи, которую бронируют.
     */
    Long itemId;
}

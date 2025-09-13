package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.validation.Create;

import java.util.List;

/**
 * REST-контроллер для управления бронированиями.
 * <p>
 * Этот контроллер обрабатывает HTTP-запросы для создания, обновления, получения и фильтрации бронирований.
 * Все методы требуют заголовка {@code X-Sharer-User-Id} для идентификации пользователя.
 * Контроллер взаимодействует с {@link BookingService} для выполнения бизнес-логики.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    /**
     * Название заголовка для передачи ID пользователя.
     */
    private final String USER_ID_HEADER = "X-Sharer-User-Id";

    /**
     * Сервис для работы с бронированиями.
     */
    private final BookingService bookingService;

    /**
     * Создает новое бронирование.
     *
     * @param bookerId ID пользователя, создающего бронирование (из заголовка {@code X-Sharer-User-Id}).
     * @param bookingDto DTO с данными бронирования (валидируется на создание).
     * @return Ответ с созданным бронированием и статусом 201 (Created).
     */
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestHeader(USER_ID_HEADER) Long bookerId,
            @Validated({Create.class}) @RequestBody BookingDto bookingDto
    ) {
        return new ResponseEntity<>(bookingService.create(bookerId, bookingDto), HttpStatus.CREATED);
    }

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param userId ID пользователя (владельца вещи, из заголовка {@code X-Sharer-User-Id}).
     * @param bookingId ID бронирования.
     * @param approved Флаг подтверждения (true - подтвердить, false - отклонить).
     * @return Ответ с обновленным бронированием и статусом 200 (OK).
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> approveBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                                             @PathVariable Long bookingId,
                                                             @RequestParam boolean approved) {
        return new ResponseEntity<>(bookingService.approve(userId, bookingId, approved), HttpStatus.OK);
    }

    /**
     * Получает бронирование по ID (доступно бронирующему или владельцу вещи).
     *
     * @param bookerId ID пользователя (из заголовка {@code X-Sharer-User-Id}).
     * @param bookingId ID бронирования.
     * @return Ответ с данными бронирования и статусом 200 (OK).
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBookingByBooker(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                                                 @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingByBooker(bookerId, bookingId));
    }

    /**
     * Получает список бронирований для владельца вещей с фильтрацией по состоянию.
     *
     * @param ownerId ID владельца (из заголовка {@code X-Sharer-User-Id}).
     * @param state Состояние фильтрации (ALL, PAST, FUTURE и т.д., по умолчанию ALL).
     * @return Ответ со списком бронирований и статусом 200 (OK).
     */
    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getBookingByOwner(
            @RequestHeader(USER_ID_HEADER) Long ownerId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        return ResponseEntity.ok(bookingService.getBookingByOwner(ownerId, state));
    }

    /**
     * Получает список всех бронирований пользователя с фильтрацией по состоянию.
     *
     * @param userId ID пользователя (из заголовка {@code X-Sharer-User-Id}).
     * @param state Состояние фильтрации (ALL, PAST, FUTURE и т.д., по умолчанию ALL).
     * @return Ответ со списком бронирований и статусом 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookingUserById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        return ResponseEntity.ok(bookingService.getAllBookingUserById(userId, state));
    }
}

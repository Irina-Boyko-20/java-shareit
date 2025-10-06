package ru.practicum.shareit.booking;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.validation.Create;

/**
 * Контроллер для управления бронированиями.
 * <p>
 * Обрабатывает HTTP-запросы, связанные с созданием, подтверждением и получением бронирований.
 * Все методы используют заголовок {@code X-Sharer-User-Id} для идентификации пользователя.
 * Контроллер взаимодействует с {@link BookingClient} для выполнения бизнес-логики.
 */
@RestController
@RequestMapping(path = "/bookings")

@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private static final String BOOKING_ID = "/{bookingId}";
    private final BookingClient bookingClient;

    /**
     * Создает новое бронирование.
     *
     * @param bookerId ID пользователя, делающего бронирование (из заголовка запроса {@code X-Sharer-User-Id})
     * @param request  DTO с данными для создания бронирования, валидируется по группе {@code Create.class}
     * @return ответ с результатом создания бронирования
     */
    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(userIdHeader) long bookerId,
                                                @Validated({Create.class}) @RequestBody BookingDto request) {
        log.info("Gateway: create booking userId={}, request={}", bookerId, request);
        return bookingClient.create(bookerId, request);
    }

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param userId    ID пользователя, выполняющего подтверждение (из заголовка {@code X-Sharer-User-Id})
     * @param bookingId ID бронирования
     * @param approved  флаг утверждения бронирования: {@code true} — подтверждение, {@code false} — отклонение
     * @return ответ с результатом операции подтверждения
     */
    @PatchMapping(BOOKING_ID)
    public ResponseEntity<Object> approveBooking(@RequestHeader(userIdHeader) Long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        log.info("Gateway: approve booking bookingId={}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.approve(userId, bookingId, approved);
    }

    /**
     * Получает информацию о бронировании по ID для пользователя, сделавшего бронирование.
     *
     * @param bookerId  ID пользователя-букера (из заголовка {@code X-Sharer-User-Id})
     * @param bookingId ID бронирования
     * @return ответ с информацией о бронировании
     */
    @GetMapping(BOOKING_ID)
    public ResponseEntity<Object> getBookingByBooker(@RequestHeader(userIdHeader) Long bookerId,
                                                     @PathVariable Long bookingId) {
        log.info("Gateway: get booking bookingId={}, bookerId={}", bookingId, bookerId);
        return bookingClient.getByBooker(bookerId, bookingId);
    }

    /**
     * Получает список бронирований для владельца вещей с возможностью фильтрации по состоянию.
     *
     * @param ownerId ID владельца (из заголовка {@code X-Sharer-User-Id})
     * @param state   состояние бронирований для фильтрации (по умолчанию {@code ALL})
     * @return ответ со списком бронирований
     * @throws IllegalArgumentException если передано неизвестное значение параметра {@code state}
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(
            @RequestHeader(userIdHeader) Long ownerId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        BookingState stateParam = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state - %s ".formatted(state)));
        log.info("Gateway: get bookings by owner state={}, ownerId={}", state, ownerId);
        return bookingClient.getByOwner(ownerId, stateParam);
    }

    /**
     * Получает список всех бронирований пользователя с возможностью фильтрации по состоянию.
     *
     * @param userId ID пользователя (из заголовка {@code X-Sharer-User-Id})
     * @param state  состояние бронирований для фильтрации (по умолчанию {@code ALL})
     * @return ответ со списком бронирований
     * @throws IllegalArgumentException если передано неизвестное значение параметра {@code state}
     */
    @GetMapping
    public ResponseEntity<Object> getAllBooking(
            @RequestHeader(userIdHeader) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state
    ) {
        BookingState stateParam = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state - %s ".formatted(state)));
        log.info("Gateway: get all bookings state={}, userId={}", stateParam, userId);
        return bookingClient.getAll(userId, stateParam);
    }
}

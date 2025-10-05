package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.InvalidOwnerBooking;
import ru.practicum.shareit.booking.exception.ValidationDateException;
import ru.practicum.shareit.booking.exception.StatusChangeNotAllowedException;
import ru.practicum.shareit.booking.exception.InvalidBookingBookerOrOwner;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.filterStrategy.BookingsFilterContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.NullOrEmptyException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления бронированиями.
 * <p>
 * Обрабатывает бизнес-логику создания, подтверждения, поиска и фильтрации бронирований.
 * Использует транзакции для обеспечения целостности данных.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    /**
     * Создает новое бронирование.
     *
     * @param bookerId   идентификатор пользователя, создающего бронирование.
     * @param bookingDto DTO с данными бронирования.
     * @return DTO с информацией о созданном бронировании.
     * @throws NullOrEmptyException       если available не был указан.
     * @throws ValidationDateException    если дата начала позже даты окончания.
     */
    @Override
    @Transactional
    public BookingResponseDto create(Long bookerId, BookingDto bookingDto) {
        User booker = userService.userExists(bookerId);
        Item item = itemService.itemExists(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new NullOrEmptyException("The Available field cannot be null");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationDateException();
        }

        Booking booking = bookingRepository.save(bookingMapper.toBooking(bookingDto, item, booker));

        return bookingMapper.toBookingDto(booking);
    }

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param userId    идентификатор пользователя, пытающегося подтвердить бронирование (владелец предмета).
     * @param bookingId идентификатор бронирования.
     * @param approved  true для подтверждения, false для отклонения.
     * @return DTO с обновленной информацией о бронировании.
     * @throws InvalidOwnerBooking           если пользователь не является владельцем предмета.
     * @throws StatusChangeNotAllowedException если статус бронирования уже изменен и не может быть обновлен.
     */
    @Override
    @Transactional
    public BookingResponseDto approve(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingExists(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new InvalidOwnerBooking(bookingId);
        }

        if (booking.getStatus() != Status.WAITING) {
            throw new StatusChangeNotAllowedException(booking.getStatus());
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        booking = bookingRepository.save(booking);

        return bookingMapper.toBookingDto(booking);
    }

    /**
     * Получает бронирование по идентификатору бронирующего или владельца предмета.
     *
     * @param bookerId  идентификатор пользователя, запрашивающего бронирование.
     * @param bookingId идентификатор бронирования.
     * @return DTO с информацией о бронировании.
     * @throws InvalidBookingBookerOrOwner если пользователь не является ни бронирующим, ни владельцем предмета.
     */
    @Override
    public BookingResponseDto getBookingByBooker(Long bookerId, Long bookingId) {
        Booking booking = bookingExists(bookingId);

        if (!booking.getBooker().getId().equals(bookerId) && !booking.getItem().getOwner().getId().equals(bookerId)) {
            throw new InvalidBookingBookerOrOwner(bookerId, bookingId);
        }

        return bookingMapper.toBookingDto(booking);
    }

    /**
     * Получает список бронирований для владельца предметов с фильтрацией по состоянию.
     *
     * @param ownerId идентификатор владельца предметов.
     * @param value   строковое представление состояния для фильтрации.
     * @return список DTO бронирований; может быть пустым.
     * @throws ru.practicum.shareit.booking.exception.StateNotFoundException если передано некорректное состояние.
     */
    @Override
    @Transactional
    public List<BookingResponseDto> getBookingByOwner(Long ownerId, String value) {
        State state = State.stateExists(value);
        userService.userExists(ownerId);
        List<Booking> bookings = BookingsFilterContext.filterBookings(ownerId, state, bookingRepository);

        return bookings.isEmpty() ? Collections.emptyList() : bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает список бронирований пользователя с фильтрацией по состоянию.
     *
     * @param userId идентификатор пользователя.
     * @param value  строковое представление состояния для фильтрации.
     * @return список DTO бронирований; может быть пустым.
     * @throws ru.practicum.shareit.booking.exception.StateNotFoundException если передано некорректное состояние.
     */
    @Override
    @Transactional
    public List<BookingResponseDto> getAllBookingUserById(Long userId, String value) {
        State state = State.stateExists(value);
        userService.userExists(userId);
        List<Booking> bookings = BookingsFilterContext.filterBookings(userId, state, bookingRepository);

        return bookings.isEmpty() ? Collections.emptyList() : bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    /**
     * Проверяет существование бронирования по идентификатору.
     *
     * @param bookingId идентификатор бронирования.
     * @return объект бронирования.
     * @throws BookingNotFoundException если бронирование с указанным идентификатором не найдено.
     */
    @Override
    public Booking bookingExists(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }
}

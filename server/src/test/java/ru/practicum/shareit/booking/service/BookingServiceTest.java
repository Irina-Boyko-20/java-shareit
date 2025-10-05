package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.exception.NullOrEmptyException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingServiceImpl bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    private User owner;
    private User booker;
    private Item item1;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(new User(1L, "Owner", "owner@test.com"));
        booker = userRepository.save(new User(2L, "Booker", "booker@test.com"));
        item1 = itemRepository.save(new Item("Pallet jack", "Hydraulic pallet jack", true, owner));

        booking = bookingRepository.save(new Booking(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item1,
                booker,
                Status.WAITING
        ));
    }

    @Test
    void create_shouldCreate() {
        BookingDto request = new BookingDto(
                null,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4),
                item1.getId()
        );

        BookingResponseDto response = bookingService.create(booker.getId(), request);

        assertThat(response).isNotNull();
        assertThat(response.item().id()).isEqualTo(item1.getId());
        assertThat(response.booker().id()).isEqualTo(booker.getId());
        assertThat(response.status()).isEqualTo(Status.WAITING.toString());
    }

    @Test
    void create_shouldThrowWhenItemNotAvailable() {
        item1.setAvailable(false);
        itemRepository.save(item1);

        BookingDto request = new BookingDto(
                null,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4),
                item1.getId()
        );

        assertThatThrownBy(() -> bookingService.create(booker.getId(), request))
                .isInstanceOf(NullOrEmptyException.class)
                .hasMessageContaining("Available");
    }

    @Test
    void create_shouldThrowWhenStartAfterEnd() {
        BookingDto request = new BookingDto(
                null,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(3),
                item1.getId()
        );

        assertThatThrownBy(() -> bookingService.create(booker.getId(), request))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void approve_shouldApprove() {
        BookingResponseDto response = bookingService.approve(owner.getId(), booking.getId(), true);

        assertThat(response.status()).isEqualTo(Status.APPROVED.toString());

        Booking updated = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void approve_shouldRejectBooking() {
        BookingResponseDto response = bookingService.approve(owner.getId(), booking.getId(), false);

        assertThat(response.status()).isEqualTo(Status.REJECTED.toString());

        Booking updated = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Status.REJECTED);
    }

    @Test
    void approve_shouldThrowWhenNotOwner() {
        User otherUser = userRepository.save(new User(null, "Other", "other@test.com"));

        assertThatThrownBy(() -> bookingService.approve(otherUser.getId(), booking.getId(), true))
                .isInstanceOf(InvalidOwnerBooking.class)
                .hasMessageContaining(String.valueOf(booking.getId()));
    }

    @Test
    void approve_shouldThrowWhenStatusNotWaiting() {
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);

        assertThatThrownBy(() -> bookingService.approve(owner.getId(), booking.getId(), true))
                .isInstanceOf(StatusChangeNotAllowedException.class)
                .hasMessageContaining(Status.APPROVED.toString());
    }

    @Test
    void getBookingByBooker_shouldReturnBookingForBooker() {
        BookingResponseDto response = bookingService.getBookingByBooker(booker.getId(), booking.getId());

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(booking.getId());
        assertThat(response.booker().id()).isEqualTo(booker.getId());
    }


    @Test
    void getBookingByBooker_shouldReturnBookingForOwner() {
        BookingResponseDto response = bookingService.getBookingByBooker(owner.getId(), booking.getId());

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(booking.getId());
    }

    @Test
    void getBookingByBooker_shouldThrowWhenUserNotRelated() {
        User stranger = userRepository.save(new User(null, "Stranger", "stranger@test.com"));

        assertThatThrownBy(() -> bookingService.getBookingByBooker(stranger.getId(), booking.getId()))
                .isInstanceOf(InvalidBookingBookerOrOwner.class)
                .hasMessageContaining(String.valueOf(stranger.getId()))
                .hasMessageContaining(String.valueOf(booking.getId()));
    }

    @Test
    void getBookingByOwner_shouldReturnList() {
        List<BookingResponseDto> bookings = bookingService.getBookingByOwner(owner.getId(), "ALL");

        assertThat(bookings).isNotEmpty();
    }

    @Test
    void getBookingByOwner_shouldReturnListWhenCurrent() {
        List<BookingResponseDto> bookings = bookingService.getBookingByOwner(owner.getId(), "CURRENT");

        assertThat(bookings).isEmpty();
    }


    @Test
    void getBookingByOwner_shouldThrowWhenInvalidState() {
        assertThatThrownBy(() -> bookingService.getBookingByOwner(owner.getId(), "INVALID"))
                .isInstanceOf(StateNotFoundException.class);
    }

    @Test
    void getAllBookingUserById_shouldReturnList() {
        List<BookingResponseDto> bookings = bookingService.getAllBookingUserById(booker.getId(), "ALL");

        assertThat(bookings).isNotEmpty();
    }

    @Test
    void getAllBookingUserById_shouldThrowWhenInvalidState() {
        assertThatThrownBy(() -> bookingService.getAllBookingUserById(booker.getId(), "INVALID"))
                .isInstanceOf(StateNotFoundException.class);
    }

    @Test
    void bookingExists_shouldReturnBooking() {
        Booking found = bookingService.bookingExists(booking.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(booking.getId());
    }

    @Test
    void bookingExists_shouldThrowWhenNotFound() {
        assertThatThrownBy(() -> bookingService.bookingExists(999L))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining("999");
    }
}

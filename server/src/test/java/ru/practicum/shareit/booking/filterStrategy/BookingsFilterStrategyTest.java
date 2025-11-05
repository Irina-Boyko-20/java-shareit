package ru.practicum.shareit.booking.filterStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@ExtendWith(MockitoExtension.class)
public class BookingsFilterStrategyTest {

    @Mock
    private BookingRepository repository;

    private Booking booking1;
    private Booking booking2;
    private User owner;
    private User booker;
    private Item item1;
    private Long userId = 1L;
    private LocalDateTime now;
    private Sort sort;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        sort = Sort.by(Sort.Direction.DESC, "start");
        owner = User.builder().name("Mike").email("mike.@email.ru").build();
        booker = User.builder().name("Nike").email("nike.@email.ru").build();
        item1 = Item.builder()
                .name("Pallet jack")
                .description("Hydraulic pallet jack")
                .available(true)
                .owner(owner)
                .build();

        booking1 = Booking.builder()
                .start(now.minusDays(3))
                .end(now.minusDays(5))
                .item(item1)
                .booker(booker)
                .status(WAITING).build();

        booking2 = Booking.builder()
                .start(now.minusDays(6))
                .end(now.minusDays(9))
                .item(item1)
                .booker(booker)
                .status(APPROVED).build();
    }

    @Test
    void testAllBookingsStrategy_owner() {
        AllBookingsStrategy strategy = new AllBookingsStrategy();
        when(repository.findByItemOwnerIdOrderByStartDesc(userId)).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findByItemOwnerIdOrderByStartDesc(userId);
    }

    @Test
    void testAllBookingsStrategy_booker() {
        AllBookingsStrategy strategy = new AllBookingsStrategy();
        when(repository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking1, booking2));

        List<Booking> result = strategy.filter(userId, repository, false);

        assertThat(result).hasSize(2);
        verify(repository).findAllByBookerIdOrderByStartDesc(userId);
    }

    @Test
    void testCurrentBookingsStrategy_owner() {
        CurrentBookingsStrategy strategy = new CurrentBookingsStrategy();
        when(repository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(eq(userId),
                any(LocalDateTime.class), any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                eq(userId), any(LocalDateTime.class), any(LocalDateTime.class), eq(sort)
        );
    }

    @Test
    void testCurrentBookingsStrategy_booker() {
        CurrentBookingsStrategy strategy = new CurrentBookingsStrategy();
        when(repository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(eq(userId),
                any(LocalDateTime.class), any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, false);

        assertThat(result).hasSize(1);
        verify(repository).findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
                eq(userId), any(LocalDateTime.class), any(LocalDateTime.class), eq(sort)
        );
    }

    @Test
    void testFutureBookingsStrategy_owner() {
        FutureBookingsStrategy strategy = new FutureBookingsStrategy();
        when(repository.findAllByItemOwnerIdAndStartIsAfter(eq(userId),
                any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findAllByItemOwnerIdAndStartIsAfter(eq(userId), any(LocalDateTime.class), eq(sort));
    }

    @Test
    void testFutureBookingsStrategy_booker() {
        FutureBookingsStrategy strategy = new FutureBookingsStrategy();
        when(repository.findAllByBookerIdAndStartIsAfter(eq(userId),
                any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, false);

        assertThat(result).hasSize(1);
        verify(repository).findAllByBookerIdAndStartIsAfter(eq(userId), any(LocalDateTime.class), eq(sort));
    }

    @Test
    void testPastBookingsStrategy_owner() {
        PastBookingsStrategy strategy = new PastBookingsStrategy();
        when(repository.findAllByItemOwnerIdAndEndIsBefore(eq(userId),
                any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findAllByItemOwnerIdAndEndIsBefore(eq(userId), any(LocalDateTime.class), eq(sort));
    }

    @Test
    void testPastBookingsStrategy_booker() {
        PastBookingsStrategy strategy = new PastBookingsStrategy();
        when(repository.findAllByBookerIdAndEndIsBefore(eq(userId),
                any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, false);

        assertThat(result).hasSize(1);
        verify(repository).findAllByBookerIdAndEndIsBefore(eq(userId), any(LocalDateTime.class), eq(sort));
    }

    @Test
    void testRejectedBookingsStrategy_owner() {
        RejectedBookingsStrategy strategy = new RejectedBookingsStrategy();
        when(repository.findAllByItemOwnerIdAndStatus(userId, Status.REJECTED)).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findAllByItemOwnerIdAndStatus(userId, Status.REJECTED);
    }

    @Test
    void testRejectedBookingsStrategy_booker() {
        RejectedBookingsStrategy strategy = new RejectedBookingsStrategy();
        when(repository.findAllByBookerIdAndStatus(userId, Status.REJECTED)).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, false);

        assertThat(result).hasSize(1);
        verify(repository).findAllByBookerIdAndStatus(userId, Status.REJECTED);
    }

    @Test
    void testWaitingBookingsStrategy_owner() {
        WaitingBookingsStrategy strategy = new WaitingBookingsStrategy();
        when(repository.findAllByItemOwnerIdAndStatus(userId, Status.WAITING)).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findAllByItemOwnerIdAndStatus(userId, Status.WAITING);
    }

    @Test
    void testWaitingBookingsStrategy_booker() {
        WaitingBookingsStrategy strategy = new WaitingBookingsStrategy();
        when(repository.findAllByBookerIdAndStatus(userId, Status.WAITING)).thenReturn(List.of(booking1));

        List<Booking> result = strategy.filter(userId, repository, false);

        assertThat(result).hasSize(1);
        verify(repository).findAllByBookerIdAndStatus(userId, Status.WAITING);
    }

    @Test
    void testFilterBookings_all_owner() {
        when(repository.findByItemOwnerIdOrderByStartDesc(userId)).thenReturn(List.of(booking1));

        List<Booking> result = BookingsFilterContext.filterBookings(userId, State.ALL, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findByItemOwnerIdOrderByStartDesc(userId);
    }

    @Test
    void testFilterBookings_current_booker() {
        when(repository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(eq(userId),
                any(LocalDateTime.class), any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = BookingsFilterContext.filterBookings(userId, State.CURRENT, repository, false);

        assertThat(result).hasSize(1);
        verify(repository).findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
                eq(userId), any(LocalDateTime.class), any(LocalDateTime.class), eq(sort)
        );
    }

    @Test
    void testFilterBookings_past_owner() {
        when(repository.findAllByItemOwnerIdAndEndIsBefore(eq(userId),
                any(LocalDateTime.class), eq(sort))).thenReturn(List.of(booking1));

        List<Booking> result = BookingsFilterContext.filterBookings(userId, State.PAST, repository, true);

        assertThat(result).hasSize(1);
        verify(repository).findAllByItemOwnerIdAndEndIsBefore(eq(userId), any(LocalDateTime.class), eq(sort));
    }
}

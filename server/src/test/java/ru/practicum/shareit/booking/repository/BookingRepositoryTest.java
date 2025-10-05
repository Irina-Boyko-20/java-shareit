package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private Item anotherItem;
    private LocalDateTime now;
    private Booking pastBooking;
    private Booking currentBooking;
    private Booking futureBooking;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new User(1L, "Owner", "owner@test.com"));
        booker = userRepository.save(new User(2L, "Booker", "booker@test.com"));

        item = itemRepository.save(new Item("Pallet jack", "Hydraulic pallet jack", true, owner));
        anotherItem = itemRepository.save(new Item("AnotherItem", "anotherDescription", true, owner));

        now = LocalDateTime.now();

        pastBooking = bookingRepository.save(new Booking(
                null, now.minusDays(10), now.minusDays(5), item, booker, Status.APPROVED));

        currentBooking = bookingRepository.save(new Booking(
                null, now.minusDays(1), now.plusDays(1), item, booker, Status.APPROVED));

        futureBooking = bookingRepository.save(new Booking(
                null, now.plusDays(1), now.plusDays(2), item, booker, Status.WAITING));
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindAllByBookerIdOrderByStartDesc() {
        List<Booking> result = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId());

        assertThat(result).hasSize(3);
        assertThat(result).extracting(Booking::getId)
                .containsExactly(futureBooking.getId(), currentBooking.getId(), pastBooking.getId());
    }

    @Test
    void testFindByItemOwnerIdOrderByStartDesc() {
        List<Booking> result = bookingRepository.findByItemOwnerIdOrderByStartDesc(owner.getId());

        assertThat(result).hasSize(3);
        assertThat(result).extracting(Booking::getId)
                .containsExactly(futureBooking.getId(), currentBooking.getId(), pastBooking.getId());
    }

    @Test
    void testFindAllByBookerIdAndStartIsBeforeAndEndIsAfter() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
                booker.getId(), now, now, sort);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(currentBooking);
    }

    @Test
    void testFindAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                owner.getId(), now, now, sort);

        assertThat(result).hasSize(1);  // Только currentBooking подходит
        assertThat(result.getFirst()).isEqualTo(currentBooking);
        assertThat(result).extracting(Booking::getId)
                .containsExactly(currentBooking.getId());
    }

    @Test
    void testFindAllByBookerIdAndStartIsAfter() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartIsAfter(
                booker.getId(), now, sort);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(futureBooking);
    }

    @Test
    void testFindAllByItemOwnerIdAndStartIsAfter() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(
                owner.getId(), now, sort);

        assertThat(result).isNotEmpty();
    }

    @Test
    void testFindAllByBookerIdAndEndIsBefore() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> result = bookingRepository.findAllByBookerIdAndEndIsBefore(
                booker.getId(), now, sort);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(pastBooking);
    }

    @Test
    void testFindAllByItemOwnerIdAndEndIsBefore() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndEndIsBefore(
                owner.getId(), now, sort);

        assertThat(result).isNotEmpty();
    }

    @Test
    void testFindAllByBookerIdAndStatus() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStatus(booker.getId(), Status.WAITING);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(futureBooking);
    }

    @Test
    void testFindAllByItemOwnerIdAndStatus() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStatus(owner.getId(), Status.WAITING);

        assertThat(result).isNotEmpty();
    }

    @Test
    void testFindByItemIdAndBookerIdAndEndBefore() {
        Optional<Booking> result = bookingRepository.findByItemIdAndBookerIdAndEndBefore(
                item.getId(), booker.getId(), now);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(pastBooking);
    }

    @Test
    void testFindFirstByItemIdAndStartBeforeAndEndBeforeOrderByEndDesc() {
        Optional<Booking> result = bookingRepository.findFirstByItemIdAndStartBeforeAndEndBeforeOrderByEndDesc(
                item.getId(), now, now);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(pastBooking);
    }

    @Test
    void testFindFirstByItemIdAndStartAfterOrderByStartAsc() {
        Optional<Booking> result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(
                item.getId(), now);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(futureBooking);
    }

    @Test
    void testFindByItemIdAndBookerIdAndEndBefore_noResult() {
        Optional<Booking> result = bookingRepository.findByItemIdAndBookerIdAndEndBefore(
                anotherItem.getId(), booker.getId(), now);

        assertThat(result).isEmpty();
    }

    @Test
    void testFindFirstByItemIdAndStartBeforeAndEndBeforeOrderByEndDesc_noResult() {
        Optional<Booking> result = bookingRepository.findFirstByItemIdAndStartBeforeAndEndBeforeOrderByEndDesc(
                anotherItem.getId(), now.minusDays(20), now.minusDays(15));

        assertThat(result).isEmpty();
    }

    @Test
    void testFindFirstByItemIdAndStartAfterOrderByStartAsc_noResult() {
        Optional<Booking> result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(
                anotherItem.getId(), now.plusDays(20));

        assertThat(result).isEmpty();
    }
}

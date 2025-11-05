package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {
    @Test
    void testNoArgsConstructor() {
        Booking booking = new Booking();
        assertNotNull(booking);
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1L, "Mike", "mike@test.com");
        user.setId(1L);

        Item item = new Item("Pallet jack", "Hydraulic pallet jack", true, user);
        item.setId(1L);

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(3);

        Booking booking = new Booking(1L, start, end, item, user, Status.WAITING);

        assertNotNull(booking);
        assertEquals(item, booking.getItem());
        assertEquals(user, booking.getBooker());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(Status.WAITING, booking.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Booking booking = new Booking();

        booking.setId(1L);
        assertEquals(1L, booking.getId());

        User user = new User(1L, "Mike", "mike@test.com");
        booking.setBooker(user);
        assertEquals(user, booking.getBooker());

        Item item = new Item("Pallet jack", "Hydraulic pallet jack", true, user);
        booking.setItem(item);
        assertEquals(item, booking.getItem());

        LocalDateTime start = LocalDateTime.now();
        booking.setStart(start);
        assertEquals(start, booking.getStart());

        LocalDateTime end = start.plusDays(1);
        booking.setEnd(end);
        assertEquals(end, booking.getEnd());

        booking.setStatus(Status.APPROVED);
        assertEquals(Status.APPROVED, booking.getStatus());
    }

    @Test
    void testApprove() {
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);

        booking.approve();
        assertEquals(Status.APPROVED, booking.getStatus());
    }

    @Test
    void testReject() {
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);

        booking.reject();
        assertEquals(Status.REJECTED, booking.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        Booking booking3 = new Booking();
        booking3.setId(2L);

        assertEquals(booking1, booking2);
        assertEquals(booking2, booking1);

        assertEquals(booking1.hashCode(), booking2.hashCode());

        assertNotEquals(booking1, booking3);

        assertNotEquals(null, booking1);
    }

    @Test
    void testToString() {
        Booking booking = new Booking();
        booking.setId(1L);

        String toString = booking.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Booking"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("item=null"));
        assertTrue(toString.contains("booker=null"));
    }
}

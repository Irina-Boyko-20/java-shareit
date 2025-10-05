package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestTest {

    @Test
    void testNoArgsConstructor() {
        ItemRequest request = new ItemRequest();
        assertNotNull(request);
    }

    @Test
    void testAllArgsConstructor() {
        User requestor = User.builder()
                .name("Requestor")
                .email("requestor@test.com")
                .build();
        ItemRequest request = ItemRequest.builder()
                .description("Need a pallet jack")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();

        assertEquals("Need a pallet jack", request.getDescription());
        assertEquals(requestor, request.getRequestor());
    }

    @Test
    void testSettersAndGetters() {
        ItemRequest request = new ItemRequest();

        request.setId(1L);
        assertEquals(1L, request.getId());

        request.setDescription("Need a hammer");
        assertEquals("Need a hammer", request.getDescription());

        User requestor = User.builder()
                .name("Requestor")
                .email("requestor@test.com")
                .build();
        request.setRequestor(requestor);
        assertEquals(requestor, request.getRequestor());

        LocalDateTime created = LocalDateTime.now();
        request.setCreated(created);
        assertEquals(created, request.getCreated());
    }

    @Test
    void testEqualsAndHashCode() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);

        ItemRequest request3 = new ItemRequest();
        request3.setId(2L);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(null, request1);
    }

    @Test
    void testToString() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need saw");

        String toString = request.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ItemRequest"));
        assertTrue(toString.contains("description=Need saw"));
        assertTrue(toString.contains("requestor="));
    }
}

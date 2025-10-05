package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testAllArgsConstructor() {
        User user = User.builder()
                .name("Mike")
                .email("mike@email.com")
                .build();

        assertEquals("Mike", user.getName());
        assertEquals("mike@email.com", user.getEmail());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        user.setId(1L);
        assertEquals(1L, user.getId());

        user.setName("Mike");
        assertEquals("Mike", user.getName());

        user.setEmail("mike@email.com");
        assertEquals("mike@email.com", user.getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        User user3 = new User();
        user3.setId(2L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
        assertNotEquals(null, user1);
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(1L);
        user.setName("Mike");
        user.setEmail("mike@email.com");

        String toString = user.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("name=Mike"));
        assertTrue(toString.contains("email=mike@email.com"));
    }
}

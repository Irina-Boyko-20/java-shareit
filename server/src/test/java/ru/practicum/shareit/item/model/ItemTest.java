package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    @Test
    void testNoArgsConstructor() {
        Item item = new Item();
        assertNotNull(item);
    }

    @Test
    void testAllArgsConstructor() {
        User owner = new User(1L, "Owner", "owner@test.com");
        Item item = new Item("Pallet jack", "Hydraulic pallet jack", true, owner);

        assertEquals("Pallet jack", item.getName());
        assertEquals("Hydraulic pallet jack", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
    }

    @Test
    void testSettersAndGetters() {
        Item item = new Item();

        item.setId(1L);
        assertEquals(1L, item.getId());

        item.setName("Hammer");
        assertEquals("Hammer", item.getName());

        item.setDescription("Old hammer");
        assertEquals("Old hammer", item.getDescription());

        item.setAvailable(false);
        assertFalse(item.getAvailable());

        User owner = new User(2L, "Owner", "owner@test.com");
        item.setOwner(owner);
        assertEquals(owner, item.getOwner());

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Need a saw")
                .requestor(owner)
                .created(LocalDateTime.now())
                .build();

        item.setRequest(request);
        assertEquals(request, item.getRequest());

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setText("Good");
        comments.add(comment);
        item.setComments(comments);
        assertEquals(1, item.getComments().size());
        assertEquals("Good", item.getComments().getFirst().getText());
    }

    @Test
    void testEqualsAndHashCode() {
        Item item1 = new Item();
        item1.setId(1L);

        Item item2 = new Item();
        item2.setId(1L);

        Item item3 = new Item();
        item3.setId(2L);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1, item3);
        assertNotEquals(null, item1);
    }

    @Test
    void testToString() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Pallet jack");
        item.setAvailable(true);

        String toString = item.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Item"));
        assertTrue(toString.contains("name=Pallet jack"));
        assertTrue(toString.contains("available=true"));
        assertTrue(toString.contains("owner="));
        assertFalse(toString.contains("request="));
        assertTrue(toString.contains("comments="));
    }
}

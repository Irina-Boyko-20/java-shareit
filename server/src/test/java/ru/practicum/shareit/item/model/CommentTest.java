package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {
    @Test
    void testNoArgsConstructor() {
        Comment comment = new Comment();
        assertNotNull(comment);
    }

    @Test
    void testSettersAndGetters() {
        Comment comment = new Comment();

        comment.setId(1L);
        assertEquals(1L, comment.getId());

        comment.setText("Good pallet jack");
        assertEquals("Good pallet jack", comment.getText());

        comment.setAuthorName("Mike");
        assertEquals("Mike", comment.getAuthorName());

        LocalDateTime created = LocalDateTime.now();
        comment.setCreated(created);
        assertEquals(created, comment.getCreated());

        User author = new User(1L, "Mike", "mike@test.com");

        Item item = new Item("Pallet jack", "Hydraulic pallet jack", true, author);
        comment.setItem(item);
        assertEquals(item, comment.getItem());
    }

    @Test
    void testEqualsAndHashCode() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        Comment comment3 = new Comment();
        comment3.setId(2L);

        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());
        assertNotEquals(comment1, comment3);
        assertNotEquals(null, comment1);
    }

    @Test
    void testToString() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setAuthorName("Mike");

        String toString = comment.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Comment"));
        assertTrue(toString.contains("item="));
        assertTrue(toString.contains("text=Test comment"));
        assertTrue(toString.contains("authorName=Mike"));
    }
}

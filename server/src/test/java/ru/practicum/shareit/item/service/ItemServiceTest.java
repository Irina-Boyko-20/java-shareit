package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.exception.InvalidOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemServiceTest {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private EntityManager entityManager;

    private User owner;
    private User booker;
    private User anotherUser;
    private Item item;
    private ItemRequest itemRequest;
    private Booking pastBooking;
    private Booking futureBooking;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(new User(1L, "Owner", "owner@test.com"));
        booker = userRepository.save(new User(2L, "Booker", "booker@test.com"));
        anotherUser = userRepository.save(new User(3L, "Another", "another@test.com"));

        item = itemRepository.save(new Item("Pallet jack", "Hydraulic pallet jack", true, owner));

        LocalDateTime now = LocalDateTime.now();

        itemRequest = itemRequestRepository.save(ItemRequest.builder()
                .id(1L)
                .description("Need a pallet jack")
                .requestor(booker)
                .created(now)
                .build());

        pastBooking = bookingRepository.save(new Booking(
                1L, now.minusDays(5), now.minusDays(3), item, booker, Status.APPROVED
        ));

        futureBooking = bookingRepository.save(new Booking(
                2L, now.plusDays(1), now.plusDays(3), item, booker, Status.WAITING
        ));
    }

    @Test
    void create_shouldCreate() {
        ItemDto request = new ItemDto(1L, "Hammer", "Old hammer", true, null);

        ItemResponseDto result = itemService.create(owner.getId(), request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isPositive();
        assertThat(result.name()).isEqualTo("Hammer");
        assertThat(result.description()).isEqualTo("Old hammer");
        assertThat(result.available()).isTrue();
    }

    @Test
    void create_shouldCreateItemWithRequest() {
        ItemDto request = new ItemDto(1L, "Saw", "Hand saw for wood", true, itemRequest.getId());

        ItemResponseDto result = itemService.create(owner.getId(), request);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Saw");
    }

    @Test
    void create_shouldThrowWhenUserNotFound() {
        ItemDto request = new ItemDto(1L, "Test", "Desc", true, null);

        assertThatThrownBy(() -> itemService.create(999L, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void update_shouldUpdate() {
        ItemUpdateRequestDto request = new ItemUpdateRequestDto("Updated Pallet jack", "Updated description", false);

        ItemResponseDto result = itemService.update(owner.getId(), item.getId(), request);

        assertThat(result.name()).isEqualTo("Updated Pallet jack");
        assertThat(result.description()).isEqualTo("Updated description");
        assertThat(result.available()).isFalse();
    }

    @Test
    void update_shouldUpdatePartialFields() {
        ItemUpdateRequestDto request = new ItemUpdateRequestDto("New Name", null, null);

        ItemResponseDto result = itemService.update(owner.getId(), item.getId(), request);

        assertThat(result.name()).isEqualTo("New Name");
        assertThat(result.description()).isEqualTo("Hydraulic pallet jack");
        assertThat(result.available()).isTrue();
    }

    @Test
    void update_shouldThrowWhenNotOwner() {
        ItemUpdateRequestDto request = new ItemUpdateRequestDto("New Name", "Desc", true);

        assertThatThrownBy(() -> itemService.update(anotherUser.getId(), item.getId(), request))
                .isInstanceOf(InvalidOwnerException.class)
                .hasMessage("User with id = %d is not the owner of item with id = %d".formatted(anotherUser.getId(), item.getId()));
    }

    @Test
    void update_shouldThrowWhenItemNotFound() {
        ItemUpdateRequestDto request = new ItemUpdateRequestDto("Name", "Desc", true);

        assertThatThrownBy(() -> itemService.update(owner.getId(), 999L, request))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void findById_shouldReturnWithBookingsForOwner() {
        ItemDetailedResponseDto result = itemService.getById(item.getId(), owner.getId());

        assertThat(result.id()).isEqualTo(item.getId());
        assertThat(result.name()).isEqualTo("Pallet jack");
        assertThat(result.lastBooking()).isNotNull();
        assertThat(result.nextBooking()).isNotNull();
    }

    @Test
    void findById_shouldReturnWithoutBookingsForOtherUser() {
        ItemDetailedResponseDto result = itemService.getById(item.getId(), anotherUser.getId());

        assertThat(result.id()).isEqualTo(item.getId());
        assertThat(result.name()).isEqualTo("Pallet jack");
        assertThat(result.lastBooking()).isNull();
        assertThat(result.nextBooking()).isNull();
    }

    @Test
    void findById_shouldThrowWhenItemNotFound() {
        assertThatThrownBy(() -> itemService.getById(999L, owner.getId()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Item with id = %d not found".formatted(999L));
    }

    @Test
    void getAllItemsOwner_shouldReturnEmptyListWhenNoItems() {
        User userWithoutItems = userRepository.save(new User(4L, "NoItems", "noitems@test.com"));

        List<ItemResponseDto> result = itemService.getAllItemsOwner(userWithoutItems.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void searchItems_shouldReturnMatchingItems() {
        List<ItemResponseDto> result = itemService.searchItems("pallet jack");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Pallet jack");
    }

    @Test
    void searchItems_shouldReturnEmptyListWhenNoMatches() {
        List<ItemResponseDto> result = itemService.searchItems("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void searchItems_shouldReturnEmptyListWhenBlankText() {
        List<ItemResponseDto> result = itemService.searchItems("   ");

        assertThat(result).isEmpty();
    }

    @Test
    void searchItems_shouldNotReturnUnavailableItems() {
        List<ItemResponseDto> result = itemService.searchItems("pallet jack");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Pallet jack");
    }

    @Test
    void itemExists_shouldReturnItem() {
        Item result = itemService.itemExists(item.getId());

        assertThat(result.getId()).isEqualTo(item.getId());
        assertThat(result.getName()).isEqualTo("Pallet jack");
    }

    @Test
    void itemExists_shouldThrowWhenNotFound() {
        assertThatThrownBy(() -> itemService.itemExists(999L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Item with id = %d not found".formatted(999L));
    }

    @Test
    void addComment_shouldAddComment() {
        CommentDto commentRequest = new CommentDto("Good pallet jack");

        CommentResponseDto result = itemService.addComment(item.getId(), booker.getId(), commentRequest);

        assertThat(result).isNotNull();
        assertThat(result.text()).isEqualTo("Good pallet jack");
        assertThat(result.authorName()).isEqualTo("Booker");
    }

    @Test
    void addComment_shouldThrowWhenItemNotFound() {
        CommentDto commentRequest = new CommentDto("Comment");

        assertThatThrownBy(() -> itemService.addComment(999L, booker.getId(), commentRequest))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void addComment_shouldThrowWhenUserNotFound() {
        CommentDto commentRequest = new CommentDto("Comment");

        assertThatThrownBy(() -> itemService.addComment(item.getId(), 999L, commentRequest))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getPersonalItems_shouldIncludeComments() {
        CommentDto commentRequest = new CommentDto("Good pallet jack");
        itemService.addComment(item.getId(), booker.getId(), commentRequest);

        entityManager.flush();
        entityManager.clear();

        ItemDetailedResponseDto result = itemService.getById(item.getId(), owner.getId());

        assertThat(result.comments()).hasSize(1);
        assertThat(result.comments().getFirst().text()).isEqualTo("Good pallet jack");
    }
}

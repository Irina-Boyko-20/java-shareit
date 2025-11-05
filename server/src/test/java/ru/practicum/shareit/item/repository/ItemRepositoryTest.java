package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User owner;
    private Item item1;
    private Item item2;
    private Item item3;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(new User(1L, "Owner", "owner@test.com"));

        item1 = itemRepository.save(new Item("Pallet jack", "Hydraulic pallet jack", true, owner));
        item2 = itemRepository.save(new Item("Hammer", "Old hammer", false, owner));
        item3 = itemRepository.save(new Item("Saw", "Hand saw for wood", true, owner));
    }

    @Test
    void findByOwnerId_shouldReturnItemsForSpecificOwner() {
        User anotherUser = userRepository.save(new User(2L, "Another", "another@test.com"));

        List<Item> result = itemRepository.findByOwnerId(owner.getId());

        assertThat(result).hasSize(3);
        assertThat(result).extracting(Item::getId)
                .containsExactlyInAnyOrder(item1.getId(), item2.getId(), item3.getId());
        assertThat(result).noneMatch(item -> item.getOwner().getId().equals(anotherUser.getId()));
    }

    @Test
    void findAvailableItem_shouldReturnOnlyAvailableItemsMatchingText() {
        List<Item> result = itemRepository.findAvailableItem("pallet jack");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Pallet jack");
        assertThat(result.getFirst().getAvailable()).isTrue();

        List<Item> result2 = itemRepository.findAvailableItem("saw");
        assertThat(result2).hasSize(1);
        assertThat(result2.getFirst().getName()).isEqualTo("Saw");
        assertThat(result2.getFirst().getAvailable()).isTrue();

        List<Item> result3 = itemRepository.findAvailableItem("hammer");
        assertThat(result3).isEmpty();

        List<Item> result4 = itemRepository.findAvailableItem("hydraulic");
        assertThat(result4).hasSize(1);
        assertThat(result4.getFirst().getDescription()).contains("Hydraulic");

        List<Item> result5 = itemRepository.findAvailableItem("nonexistent");
        assertThat(result5).isEmpty();
    }

    @Test
    void findByRequestIdIn_shouldReturnItemsByRequestIds() {
        ItemRequest request1 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a pallet jack")
                .requestor(owner)
                .created(LocalDateTime.now().minusDays(1))
                .build());
        ItemRequest request2 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a hammer")
                .requestor(owner)
                .created(LocalDateTime.now().minusDays(2))
                .build());

        item1.setRequest(request1);
        item2.setRequest(request2);
        itemRepository.saveAll(List.of(item1, item2));

        List<Item> result = itemRepository.findByRequestIdIn(List.of(request1.getId(), request2.getId()));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(i -> i.getRequest().getId())
                .containsExactlyInAnyOrder(request1.getId(), request2.getId());
    }
}

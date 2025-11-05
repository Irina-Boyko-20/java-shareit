package ru.practicum.shareit.request.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User requestor;
    private User anotherRequestor;
    private ItemRequest request1;
    private ItemRequest request2;
    private ItemRequest request3;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        requestor = userRepository.save(User.builder()
                .name("Requestor")
                .email("requestor@test.com")
                .build());
        anotherRequestor = userRepository.save(User.builder()
                .name("AnotherRequester")
                .email("another@test.com")
                .build());

        LocalDateTime now = LocalDateTime.now();
        request1 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a pallet jack")
                .requestor(requestor)
                .created(now)
                .build());

        request2 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a hammer")
                .requestor(requestor)
                .created(now.plusDays(2))
                .build());

        request3 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a saw")
                .requestor(anotherRequestor)
                .created(now.plusDays(3))
                .build());

        item1 = Item.builder()
                .name("Pallet jack")
                .description("Hydraulic pallet jack")
                .available(true)
                .owner(requestor)
                .request(request1)
                .build();
        item1 = itemRepository.save(item1);

        item2 = Item.builder()
                .name("Hammer")
                .description("Old hammer")
                .available(true)
                .owner(anotherRequestor)
                .request(request3)
                .build();
        item2 = itemRepository.save(item2);
    }

    @Test
    void findAllByRequestorId_shouldReturnRequestsForSpecificRequestorWithItemsFetched() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestor.getId());

        assertThat(requests).hasSize(2);
        assertThat(requests.get(0).getId()).isEqualTo(request2.getId());
        assertThat(requests.get(1).getId()).isEqualTo(request1.getId());
        assertThat(requests).allMatch(request -> request.getRequestor().getId().equals(requestor.getId()));

        assertThat(requests.get(0).getItems()).isNotNull();

        List<ItemRequest> anotherRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(anotherRequestor.getId());
        assertThat(anotherRequests).hasSize(1);
        assertThat(anotherRequests.getFirst().getId()).isEqualTo(request3.getId());

        List<ItemRequest> emptyRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(999L);
        assertThat(emptyRequests).isEmpty();
    }

    @Test
    void findAllByOrderByCreatedDesc_shouldReturnRequestsInDescOrder() {
        List<ItemRequest> requests = itemRequestRepository.findAllByOrderByCreatedDesc();

        assertThat(requests).hasSize(3);
        assertThat(requests.get(0).getId()).isEqualTo(request3.getId());
        assertThat(requests.get(1).getId()).isEqualTo(request2.getId());
        assertThat(requests.get(2).getId()).isEqualTo(request1.getId());
    }

    @Test
    void existsByDescription_shouldReturnTrueIfExists() {
        boolean exists = itemRequestRepository.existsByDescription("Need a pallet jack");
        assertThat(exists).isTrue();

        boolean notExists = itemRequestRepository.existsByDescription("Need a drill");
        assertThat(notExists).isFalse();
    }
}

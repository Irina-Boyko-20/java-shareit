package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDetailsDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemRequestServiceTest {
    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User requestor;
    private User anotherRequestor;
    private User itemOwner;
    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;
    private ItemRequest request1;
    private ItemRequest request2;
    private ItemRequest request3;
    private ItemRequest request4;

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

        itemOwner = userRepository.save(User.builder()
                .name("Owner")
                .email("owner@test.com")
                .build());

        LocalDateTime now = LocalDateTime.now();
        request1 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a pallet jack")
                .requestor(requestor)
                .created(now.minusDays(1))
                .build());

        request2 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a hammer")
                .requestor(requestor)
                .created(now.minusDays(2))
                .build());

        request3 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a saw")
                .requestor(anotherRequestor)
                .created(now.minusDays(3))
                .build());

        request4 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need a hammer drill")
                .requestor(requestor)
                .created(now.minusDays(4))
                .build());

        item1 = Item.builder()
                .name("Pallet jack")
                .description("Hydraulic pallet jack")
                .available(true)
                .owner(itemOwner)
                .request(request1)
                .build();
        item1 = itemRepository.save(item1);

        item2 = Item.builder()
                .name("Hammer")
                .description("Old hammer")
                .available(true)
                .owner(itemOwner)
                .request(request1)
                .build();
        item2 = itemRepository.save(item2);

        item3 = Item.builder()
                .name("Saw")
                .description("Electric saw")
                .available(true)
                .owner(itemOwner)
                .request(request2)
                .build();
        item3 = itemRepository.save(item3);
    }

    @Test
    void addRequest_shouldAddRequest() {
        ItemRequestDto requestDto = new ItemRequestDto(5L, "Need new request", LocalDateTime.now());

        ItemRequestDto result = itemRequestService.addRequest(requestor.getId(), requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Need new request");

        List<ItemRequest> allRequests = itemRequestRepository.findAll();
        assertThat(allRequests).hasSize(5);
        assertThat(allRequests).extracting(ItemRequest::getDescription)
                .contains("Need new request");
    }

    @Test
    void addRequest_shouldThrowWhenUserNotFound() {
        ItemRequestDto requestDto = new ItemRequestDto(4L, "New request", LocalDateTime.now());

        assertThatThrownBy(() -> itemRequestService.addRequest(999L, requestDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getRequestByRequestor_shouldReturnRequestsInDescOrder() {
        List<ItemRequestDetailsDto> result = itemRequestService.getRequestByRequestor(requestor.getId());

        assertThat(result).hasSize(3);

        assertThat(result).extracting(ItemRequestDetailsDto::id)
                .containsExactly(request1.getId(), request2.getId(), request4.getId());

        assertThat(result.getFirst().items()).hasSize(2);
        assertThat(result.get(0).items()).extracting(ItemShortDto::name)
                .containsExactlyInAnyOrder("Pallet jack", "Hammer");
        assertThat(result.get(0).items()).extracting(ItemShortDto::ownerId)
                .allMatch(id -> id.equals(itemOwner.getId()));

        assertThat(result.get(2).items()).isEmpty();
    }

    @Test
    void getRequestByRequestor_shouldReturnEmptyListWhenNoRequests() {
        List<ItemRequestDetailsDto> result = itemRequestService.getRequestByRequestor(itemOwner.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void getRequestByRequestor_shouldThrowWhenUserNotFound() {
        assertThatThrownBy(() -> itemRequestService.getRequestByRequestor(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllRequests_shouldReturnAllRequestsInDescOrder() {
        List<ItemRequestDto> result = itemRequestService.getAllRequests();

        assertThat(result).hasSize(4);  // Все запросы
        assertThat(result).extracting(ItemRequestDto::getId)
                .containsExactly(request1.getId(), request2.getId(), request3.getId(), request4.getId());
    }

    @Test
    void getRequestById_shouldReturnRequestWithItems() {
        ItemRequestDetailsDto result = itemRequestService.getRequestById(request1.getId());

        assertThat(result.id()).isEqualTo(request1.getId());
        assertThat(result.description()).isEqualTo("Need a pallet jack");
        assertThat(result.items()).hasSize(2);
        assertThat(result.items()).extracting(ItemShortDto::name)
                .containsExactlyInAnyOrder("Pallet jack", "Hammer");
    }

    @Test
    void getRequestById_shouldReturnRequestWithoutItems() {
        ItemRequestDetailsDto result = itemRequestService.getRequestById(request3.getId());

        assertThat(result.id()).isEqualTo(request3.getId());
        assertThat(result.description()).isEqualTo("Need a saw");
        assertThat(result.items()).isEmpty();
    }

    @Test
    void getRequestById_shouldThrowWhenRequestNotFound() {
        assertThatThrownBy(() -> itemRequestService.getRequestById(999L))
                .isInstanceOf(RequestNotFoundException.class)
                .hasMessage("Request with id = 999 not found");
    }

    @Test
    void mapToDtoWithItems_shouldGroupItemsByRequest() {
        List<ItemRequest> requests = Arrays.asList(request1, request2);

        List<ItemRequestDetailsDto> result = itemRequestService.getRequestByRequestor(requestor.getId());

        assertThat(result).hasSize(3);

        ItemRequestDetailsDto request1Dto = result.stream()
                .filter(dto -> dto.id().equals(request1.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(request1Dto.items()).hasSize(2);

        ItemRequestDetailsDto request2Dto = result.stream()
                .filter(dto -> dto.id().equals(request2.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(request2Dto.items()).hasSize(1);
        assertThat(request2Dto.items().getFirst().name()).isEqualTo("Saw");
    }
}

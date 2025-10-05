package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDetailedResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ShareItServer.class)
public class ItemMapperTest {

    @Autowired
    private ItemMapper mapper;

    @Test
    void toItemDto_shouldMapCorrectly() {
        User owner = User.builder().id(1L).name("Owner").build();
        ItemRequest request = ItemRequest.builder().id(2L).build();
        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(owner)
                .request(request)
                .build();

        ItemResponseDto result = mapper.toItemDto(item);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(item.getId());
        assertThat(result.name()).isEqualTo(item.getName());
        assertThat(result.description()).isEqualTo(item.getDescription());
        assertThat(result.available()).isEqualTo(item.getAvailable());
        assertThat(result.requestId()).isEqualTo(item.getRequest().getId());
    }

    @Test
    void toItemDto_shouldHandleNullRequest() {
        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

        ItemResponseDto result = mapper.toItemDto(item);

        assertThat(result.requestId()).isNull();
    }

    @Test
    void toItemDto_shouldHandleNull() {
        ItemResponseDto result = mapper.toItemDto(null);

        assertThat(result).isNull();
    }

    @Test
    void toItem_shouldMapCorrectly() {
        ItemDto itemDto = new ItemDto(1L, "Test Item", "Test Description", true, null);

        Item result = mapper.toItem(itemDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(itemDto.getId());
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(result.getOwner()).isNull();
        assertThat(result.getRequest()).isNull();
        assertThat(result.getComments()).isEmpty();
    }

    @Test
    void toItem_shouldHandleNull() {
        Item result = mapper.toItem(null);

        assertThat(result).isNull();
    }

    @Test
    void updateItemFromRequest_shouldUpdateNonNullFields() {
        User owner = User.builder().id(1L).name("Owner").build();
        ItemRequest request = ItemRequest.builder().id(2L).build();
        Item item = Item.builder()
                .id(1L)
                .name("Old Name")
                .description("Old Description")
                .available(false)
                .owner(owner)
                .request(request)
                .build();

        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto("New Name", null, true);

        mapper.updateItemFromRequest(item, updateDto);

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("New Name");
        assertThat(item.getDescription()).isEqualTo("Old Description");
        assertThat(item.getAvailable()).isEqualTo(true);
        assertThat(item.getOwner()).isEqualTo(owner);
        assertThat(item.getRequest()).isEqualTo(request);
        assertThat(item.getComments()).isEmpty();
    }

    @Test
    void updateItemFromRequest_shouldIgnoreNullValues() {
        Item item = Item.builder()
                .id(1L)
                .name("Old Name")
                .description("Old Description")
                .available(false)
                .build();

        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto(null, null, null);

        mapper.updateItemFromRequest(item, updateDto);

        assertThat(item.getName()).isEqualTo("Old Name");
        assertThat(item.getDescription()).isEqualTo("Old Description");
        assertThat(item.getAvailable()).isEqualTo(false);
    }

    @Test
    void updateItemFromRequest_shouldHandleNull() {
        Item item = Item.builder()
                .id(1L)
                .name("Old Name")
                .description("Old Description")
                .available(false)
                .build();

        mapper.updateItemFromRequest(item, null);

        assertThat(item.getName()).isEqualTo("Old Name");
        assertThat(item.getDescription()).isEqualTo("Old Description");
        assertThat(item.getAvailable()).isEqualTo(false);
    }


    @Test
    void toDetailedResponseDto_shouldMapCorrectly() {
        User owner = User.builder().id(1L).name("Owner").build();
        ItemRequest request = ItemRequest.builder().id(2L).build();
        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(owner)
                .request(request)
                .build();

        Booking lastBooking = Booking.builder()
                .id(10L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .status(Status.APPROVED)
                .build();
        Booking nextBooking = Booking.builder()
                .id(11L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Status.WAITING)
                .build();

        ItemDetailedResponseDto result = mapper.toDetailedResponseDto(item, lastBooking, nextBooking);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(item.getId());
        assertThat(result.name()).isEqualTo(item.getName());
        assertThat(result.description()).isEqualTo(item.getDescription());
        assertThat(result.available()).isEqualTo(item.getAvailable());
        assertThat(result.requestId()).isEqualTo(item.getRequest().getId());
        assertThat(result.lastBooking()).isEqualTo(lastBooking);
        assertThat(result.nextBooking()).isEqualTo(nextBooking);
    }

    @Test
    void toDetailedResponseDto_shouldHandleNullBookings() {
        User owner = new User(1L, "Mike", "mike@email.ru");
        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .owner(owner)
                .available(true)
                .build();

        ItemDetailedResponseDto result = mapper.toDetailedResponseDto(item, null, null);

        assertThat(result.lastBooking()).isNull();
        assertThat(result.nextBooking()).isNull();
        assertThat(result.requestId()).isNull(); // Нет request
    }

    @Test
    void toDetailedResponseDto_shouldHandleNullsBookings() {
        ItemDetailedResponseDto result = mapper.toDetailedResponseDto(null, null, null);

        assertThat(result).isNull();
    }

    @Test
    void mapToItemDto_shouldMapListCorrectly() {
        ItemRequest request = ItemRequest.builder().id(2L).build();
        List<Item> items = List.of(
                Item.builder().id(1L).name("Item 1").description("Desc 1").available(true).request(request).build(),
                Item.builder().id(2L).name("Item 2").description("Desc 2").available(false).build()
        );

        List<ItemResponseDto> result = mapper.mapToItemDto(items);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).requestId()).isEqualTo(2L);
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).requestId()).isNull();
    }

    @Test
    void mapToItemDto_shouldHandleEmptyList() {
        List<Item> items = List.of();

        List<ItemResponseDto> result = mapper.mapToItemDto(items);

        assertThat(result).isEmpty();
    }

    @Test
    void mapToItemDto_shouldHandleNull() {
        List<ItemResponseDto> result = mapper.mapToItemDto(null);

        assertThat(result).isNull();
    }
}

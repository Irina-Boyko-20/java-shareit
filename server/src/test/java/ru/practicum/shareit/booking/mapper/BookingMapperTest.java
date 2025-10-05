package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {
    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);

    @Test
    void toBooking_shouldMapCorrectly() {
        BookingDto dto = new BookingDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 1L);

        Item item = Item.builder().id(1L).name("Test Item").build();
        User booker = User.builder().id(1L).name("Test User").build();

        Booking result = mapper.toBooking(dto, item, booker);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
        assertThat(result.getStart()).isEqualTo(dto.getStart());
        assertThat(result.getEnd()).isEqualTo(dto.getEnd());
        assertThat(result.getItem()).isEqualTo(item);
        assertThat(result.getBooker()).isEqualTo(booker);
    }

    @Test
    void toBooking_shouldReturnNullWhenBookingNull() {
        Booking result = mapper.toBooking(null, null, null);
        assertThat(result).isNull();
    }

    @Test
    void toBooking_shouldHandlePartialNulls() {
        Item item = Item.builder().id(1L).name("Test Item").build();
        User booker = User.builder().id(1L).name("Test User").build();

        Booking result = mapper.toBooking(null, item, booker);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
        assertThat(result.getStart()).isNull();
        assertThat(result.getEnd()).isNull();
        assertThat(result.getItem()).isEqualTo(item);
        assertThat(result.getBooker()).isEqualTo(booker);
    }

    @Test
    void toBookingDto_shouldMapCorrectly() {
        Item item = Item.builder().id(1L).name("Test Item").build();
        User booker = User.builder().id(1L).name("Test User").build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Status.APPROVED)
                .item(item)
                .booker(booker)
                .build();

        BookingResponseDto result = mapper.toBookingDto(booking);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(booking.getId());
        assertThat(result.start()).isEqualTo(booking.getStart());
        assertThat(result.end()).isEqualTo(booking.getEnd());
        assertThat(result.status()).isEqualTo(booking.getStatus().toString());

        assertThat(result.item().id()).isEqualTo(item.getId());
        assertThat(result.item().name()).isEqualTo(item.getName());
        assertThat(result.booker().id()).isEqualTo(booker.getId());
    }

    @Test
    void toBookingDto_shouldReturnNullWhenBookingNull() {
        BookingResponseDto result = mapper.toBookingDto(null);
        assertThat(result).isNull();
    }


}

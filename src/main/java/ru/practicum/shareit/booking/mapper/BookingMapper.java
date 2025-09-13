package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * Маппер для преобразования объектов бронирований между DTO и моделью.
 * <p>
 * Использует MapStruct для автоматической генерации кода маппинга. Игнорирует не сопоставленные поля
 * и использует Spring в качестве модели компонентов.
 * </p>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    /**
     * Преобразует BookingDto в Booking, игнорируя ID и устанавливая статус WAITING.
     *
     * @param bookingDto DTO бронирования для преобразования.
     * @param item       предмет бронирования.
     * @param booker     пользователь, создающий бронирование.
     * @return объект Booking.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "WAITING")
    Booking toBooking(BookingDto bookingDto, Item item, User booker);

    /**
     * Преобразует Booking в BookingResponseDto.
     *
     * @param booking объект Booking для преобразования.
     * @return DTO ответа бронирования.
     */
    BookingResponseDto toBookingDto(Booking booking);
}

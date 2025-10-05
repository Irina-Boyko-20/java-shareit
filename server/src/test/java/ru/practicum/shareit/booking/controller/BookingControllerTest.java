package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private final String USER_ID_HEADER = "X-Sharer-User-Id";
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;

    private BookingDto newBookingRequest;
    private BookingResponseDto bookingDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(3);

        newBookingRequest = new BookingDto(1L, start, end, 1L);

        bookingDto = new BookingResponseDto(
                1L,
                new BookingResponseDto.ItemResponseDto(1L, "Pallet jack"),
                start,
                end,
                new BookingResponseDto.UserResponseDto(1L),
                "WAITING"
        );
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        when(bookingService.create(eq(1L), any(BookingDto.class)))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.item.id").value(1L))
                .andExpect(jsonPath("$.item.name").value("Pallet jack"))
                .andExpect(jsonPath("$.booker.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void approveBooking_shouldReturnApprovedBooking() throws Exception {
        BookingResponseDto approvedBooking = new BookingResponseDto(
                1L,
                new BookingResponseDto.ItemResponseDto(1L, "Pallet jack"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                new BookingResponseDto.UserResponseDto(1L),
                "APPROVED"
        );

        when(bookingService.approve(1L, 1L, true))
                .thenReturn(approvedBooking);

        mockMvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingByBooker_shouldReturnGetBookings() throws Exception {
        when(bookingService.getBookingByBooker(1L, 1L))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.item.name").value("Pallet jack"));
    }

    @Test
    void getBookingsByOwner_shouldReturnGetBookings() throws Exception {
        when(bookingService.getBookingByOwner(1L, "CURRENT"))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAllBookingsUserById_shouldReturnGetBookings() throws Exception {
        when(bookingService.getAllBookingUserById(1L, "ALL"))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }
}

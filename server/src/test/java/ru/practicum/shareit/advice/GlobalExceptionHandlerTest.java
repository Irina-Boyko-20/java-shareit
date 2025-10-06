package ru.practicum.shareit.advice;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.InvalidBookerException;
import ru.practicum.shareit.item.exception.InvalidOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NullOrEmptyException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.exception.EmailExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    public void testValidationException() throws Exception {
        Mockito.when(bookingService.create(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new ValidationException("Start date must be before end date"));

        String json = "{ \"itemId\": 1, \"start\": \"2024-01-10T10:00:00\", \"end\": \"2024-01-09T10:00:00\" }";

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorMessages").value("Start date must be before end date"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testInvalidOwner() throws Exception {
        Mockito.when(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenThrow(new InvalidOwnerBooking(123L));

        mockMvc.perform(patch("/bookings/123?approved=true")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.errorMessages").value("Confirmation or rejection of a " +
                        "booking request - %d can only be done by the owner of the item".formatted(123L)))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testNotAuthorizedBooker() throws Exception {
        Mockito.when(bookingService.getBookingByBooker(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new InvalidBookingBookerOrOwner(5L, 10L));

        mockMvc.perform(get("/bookings/10")
                        .header("X-Sharer-User-Id", "5"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.errorMessages").value("User with id - %d ".formatted(5) +
                        "access is not allowed. Obtaining data about a specific booking - %d can ".formatted(10) +
                        "be done either by the booking author or by the item owner."))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleBookingNotFoundException() throws Exception {
        Mockito.when(bookingService.getBookingByBooker(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new BookingNotFoundException(999L));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Booking with id = 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleStateNotFoundException() throws Exception {
        Mockito.when(bookingService.getBookingByOwner(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new StateNotFoundException("INVALID"));

        mockMvc.perform(get("/bookings/owner?state=INVALID")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorMessages").value("Unknown state - INVALID"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleStatusChangeNotAllowedException() throws Exception {
        Mockito.when(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenThrow(new StatusChangeNotAllowedException(Status.REJECTED));

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorMessages").value("When the status is %s, ".formatted(Status.REJECTED) +
                        "changes are not allowed."))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleItemNotFoundException() throws Exception {
        Mockito.when(itemService.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new ItemNotFoundException(999L));

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorMessages").value("Item with id = 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleNullOrEmptyException() throws Exception {
        Mockito.when(itemService.create(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new NullOrEmptyException("The Available field cannot be null"));

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Item\",\"description\":\"Description\",\"available\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorMessages").value("The Available field cannot be null"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleInvalidOwnerException() throws Exception {
        Mockito.when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new InvalidOwnerException(2L, 123L));

        mockMvc.perform(patch("/items/123")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Item\",\"description\":\"Description\",\"available\":true}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.errorMessages").value("User with id = 2 is not the owner" +
                        " of item with id = 123"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleInvalidBookerException() throws Exception {
        Mockito.when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new InvalidBookerException(2L, 123L));

        mockMvc.perform(post("/items/123/comment")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Test text\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorMessages").value("The user with id = 2 did not rent" +
                        " the item with id = 123"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleRequestNotFoundException() throws Exception {
        Mockito.when(itemRequestService.getRequestById(Mockito.anyLong()))
                .thenThrow(new RequestNotFoundException(999L));

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorMessages").value("Request with id = 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleEmailExistsException() throws Exception {
        Mockito.when(userService.create(Mockito.any()))
                .thenThrow(new EmailExistsException("test@example.com"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test User\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorMessages").value("This email already exists test@example.com"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testHandleUserNotFoundException() throws Exception {
        Mockito.when(userService.findById(Mockito.anyLong()))
                .thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorMessages").value("User with id = 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}

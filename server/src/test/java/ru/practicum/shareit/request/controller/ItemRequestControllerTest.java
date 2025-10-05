package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDetailsDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private MockMvc mockMvc;

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private ObjectMapper objectMapper;

    private ItemRequestDto itemRequestDto;
    private ItemRequestDetailsDto itemRequestWithInfoDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        itemRequestDto = new ItemRequestDto(1L, "Need a pallet jack", LocalDateTime.now());
        itemRequestWithInfoDto = new ItemRequestDetailsDto(1L, "Need a pallet jack", LocalDateTime.now(), List.of());
    }

    @Test
    void addRequest_shouldReturnAddRequest() throws Exception {
        when(itemRequestService.addRequest(eq(1L), any(ItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestWithInfoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a pallet jack"));
    }

    @Test
    void getByRequestorId_shouldReturnListOfRequests() throws Exception {
        when(itemRequestService.getRequestByRequestor(1L))
                .thenReturn(List.of(itemRequestWithInfoDto));

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Need a pallet jack"));
    }

    @Test
    void getAllRequests_shouldReturnListOfRequests() throws Exception {
        when(itemRequestService.getAllRequests())
                .thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Need a pallet jack"));
    }

    @Test
    void getRequestById_shouldReturnRequest() throws Exception {
        when(itemRequestService.getRequestById(1L))
                .thenReturn(itemRequestWithInfoDto);

        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a pallet jack"));
    }
}

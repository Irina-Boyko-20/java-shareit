package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    private static final String userIdHeader = "X-Sharer-User-Id";
    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private ObjectMapper objectMapper;

    private ItemDto newItemRequest;
    private ItemResponseDto itemDto;
    private UserResponseDto owner;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        objectMapper = new ObjectMapper();

        owner = new UserResponseDto(1L, "Garry", "garry@mail.ru");
        newItemRequest = new ItemDto(1L, "Pallet jack", "Hydraulic pallet jack", true, null);
        itemDto = new ItemResponseDto(1L, "Pallet jack", "Hydraulic pallet jack", true, owner, null);
    }

    @Test
    void createItem_shouldReturnCreatedItem() throws Exception {
        when(itemService.create(eq(1L), any(ItemDto.class)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(userIdHeader, String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItemRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pallet jack"));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        when(itemService.update(eq(1L), eq(1L), any(ItemUpdateRequestDto.class)))
                .thenReturn(itemDto);

        ItemUpdateRequestDto updateItemRequest =
                new ItemUpdateRequestDto("Updated Pallet jack", "5-ton pallet jack", false);

        mockMvc.perform(patch("/items/1")
                        .header(userIdHeader, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pallet jack"));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        when(itemService.getById(1L, 1L))
                .thenReturn(new ItemDetailedResponseDto(
                        1L, "Pallet jack", "Hydraulic pallet jack",
                        true, owner, null, null, null, List.of()
                ));

        mockMvc.perform(get("/items/1")
                        .header(userIdHeader, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pallet jack"));
    }

    @Test
    void getAllItemsOwner_shouldReturnList() throws Exception {
        when(itemService.getAllItemsOwner(1L))
                .thenReturn(List.of(new ItemResponseDto(
                        1L, "Pallet jack", "Hydraulic pallet jack",
                        true, owner, null
                )));

        mockMvc.perform(get("/items")
                        .header(userIdHeader, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Pallet jack"));
    }

    @Test
    void searchItems_shouldReturnFoundItems() throws Exception {
        when(itemService.searchItems("pallet"))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "pallet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void addComment_shouldReturnCreatedComment() throws Exception {
        CommentDto newCommentRequest = new CommentDto("Good Pallet jack");
        CommentResponseDto commentDto = new CommentResponseDto(1L, "Good Pallet jack", "Mike", LocalDateTime.now());

        when(itemService.addComment(1L, 1L, newCommentRequest))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header(userIdHeader, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Good Pallet jack"))
                .andExpect(jsonPath("$.authorName").value("Mike"));
    }
}

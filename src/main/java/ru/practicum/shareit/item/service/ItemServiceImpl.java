package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.item.dto.ItemDetailedResponseDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.InvalidBookerException;
import ru.practicum.shareit.item.exception.NullOrEmptyException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с предметами.
 * <p>
 * Содержит бизнес-логику для управления предметами и комментариями.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    public final ItemRepository itemRepository;
    public final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    public final ItemMapper itemMapper;
    public final CommentMapper commentMapper;

    /**
     * Создает новый предмет.
     *
     * @param ownerId ID владельца предмета
     * @param itemDto DTO с данными предмета
     * @return DTO созданного предмета
     * @throws NullOrEmptyException если поле available равно null
     */
    @Override
    public ItemResponseDto createItem(Long ownerId, ItemDto itemDto) {
        User owner = userService.userExists(ownerId);
        if (itemDto.getAvailable() == null) {
            throw new NullOrEmptyException("The Available field cannot be null");
        }

        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    /**
     * Обновляет предмет.
     *
     * @param ownerId ID владельца
     * @param itemId ID предмета
     * @param itemUpdate DTO с обновленными данными
     * @return DTO обновленного предмета
     * @throws ItemNotFoundException если предмет не найден или пользователь не владелец
     */
    @Override
    @Transactional
    public ItemResponseDto updateItem(Long ownerId, Long itemId, ItemUpdateRequestDto itemUpdate) {
        Item item = itemExists(itemId);
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new ItemNotFoundException(ownerId);
        }

        itemMapper.updateItemFromRequest(item, itemUpdate);
        item = itemRepository.save(item);

        return itemMapper.toItemDto(item);
    }

    /**
     * Получает подробную информацию о предмете.
     * Если запрос делает владелец предмета, возвращаются последние и следующие бронирования.
     *
     * @param itemId ID предмета
     * @param bookerId ID пользователя, делающего запрос
     * @return подробный DTO предмета
     * @throws ItemNotFoundException если предмет не найден
     */
    @Override
    @Transactional(readOnly = true)
    public ItemDetailedResponseDto getById(Long itemId, Long bookerId) {
        Item item = itemExists(itemId);
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (item.getOwner() != null && item.getOwner().getId().equals(bookerId)) {
            lastBooking = getLastBooking(itemId);
            nextBooking = getNextBooking(itemId);
        }

        return itemMapper.toDetailedResponseDto(item, lastBooking, nextBooking);
    }

    /**
     * Получает все предметы владельца.
     *
     * @param ownerId ID владельца
     * @return список DTO предметов
     * @throws RuntimeException если владелец не найден
     */
    @Override
    public List<ItemResponseDto> getAllItemsOwner(Long ownerId) {
        userService.userExists(ownerId);

        return itemMapper.mapToItemDto(itemRepository.findByOwnerId(ownerId));
    }

    /**
     * Ищет доступные предметы по тексту (в имени или описании).
     * Возвращает пустой список, если текст пустой или состоит из пробелов.
     *
     * @param text текст для поиска
     * @return список DTO предметов
     */
    @Override
    public List<ItemResponseDto> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemRepository.findAvailableItem(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаляет предмет по ID.
     *
     * @param itemId ID предмета
     */
    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    /**
     * Проверяет наличие предмета и возвращает его.
     *
     * @param id ID предмета
     * @return сущность предмета
     * @throws ItemNotFoundException если предмет не найден
     */
    @Override
    public Item itemExists(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    /**
     * Добавляет комментарий к предмету.
     * Пользователь должен был арендовать предмет ранее.
     *
     * @param itemId ID предмета
     * @param authorId ID автора комментария
     * @param commentDto DTO комментария
     * @return DTO созданного комментария
     * @throws InvalidBookerException если пользователь не арендовал предмет
     */
    @Override
    public CommentResponseDto addComment(Long itemId, Long authorId, CommentDto commentDto) {
        boolean hasBooked = bookingRepository.findByItemIdAndBookerIdAndEndBefore(
                itemId, authorId, LocalDateTime.now()
        ).isPresent();
        if (!hasBooked) {
            throw new InvalidBookerException(authorId, itemId);
        }

        User author = userService.userExists(authorId);
        Item item = itemExists(itemId);
        Comment comment = commentMapper.toComment(item, author.getName(), commentDto.getText());

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    /**
     * Получает последнее завершённое бронирование предмета.
     *
     * @param itemId ID предмета
     * @return последнее бронирование или null
     */
    private Booking getLastBooking(Long itemId) {
        LocalDateTime now = LocalDateTime.now();

        return bookingRepository.findFirstByItemIdAndStartBeforeAndEndBeforeOrderByEndDesc(itemId, now, now)
                .orElse(null);
    }

    /**
     * Получает следующее предстоящее бронирование предмета.
     *
     * @param itemId ID предмета
     * @return следующее бронирование или null
     */
    private Booking getNextBooking(Long itemId) {
        LocalDateTime now = LocalDateTime.now();

        return bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId, now)
                .orElse(null);
    }
}

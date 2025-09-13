package ru.practicum.shareit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.StateNotFoundException;
import ru.practicum.shareit.booking.exception.InvalidBookingBookerOrOwner;
import ru.practicum.shareit.booking.exception.InvalidOwnerBooking;
import ru.practicum.shareit.booking.exception.StatusChangeNotAllowedException;
import ru.practicum.shareit.booking.exception.ValidationDateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.InvalidBookerException;
import ru.practicum.shareit.item.exception.InvalidOwnerException;
import ru.practicum.shareit.item.exception.NullOrEmptyException;
import ru.practicum.shareit.user.exception.EmailExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для REST API.
 * <p>
 * Перехватывает и обрабатывает различные исключения, возникающие в приложении,
 * и возвращает клиенту структурированные ответы с HTTP-статусами и сообщениями об ошибках.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    private static final int HTTP_STATUS_FORBIDDEN = 403;
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    private static final int HTTP_STATUS_CONFLICT = 409;

    /**
     * Обработка ошибок валидации параметров запроса.
     * <p>
     * Перехватывает исключения, связанные с некорректными данными в запросе,
     * и возвращает список сообщений об ошибках валидации с HTTP-статусом 400.
     *
     * @param ex исключение валидации параметров
     * @return ответ с HTTP 400 и списком сообщений об ошибках
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleExceptions(
            final MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_BAD_REQUEST);

        // Собираем все сообщения ошибок в список
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errorMessages", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключения, когда бронирование не найдено.
     * <p>
     * Перехватывает случаи, когда запрашиваемое бронирование отсутствует в системе,
     * и возвращает HTTP-статус 404 с сообщением об ошибке.
     *
     * @param ex исключение отсутствия бронирования
     * @return ответ с HTTP 404 и сообщением об ошибке
     */
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookingNotFoundException(
            final BookingNotFoundException ex
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_NOT_FOUND);
        body.put("error", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработка исключения, когда состояние бронирования не найдено.
     * <p>
     * Перехватывает случаи, когда указано некорректное состояние бронирования,
     * и возвращает HTTP-статус 404 с сообщением об ошибке.
     *
     * @param ex исключение отсутствия состояния
     * @return ответ с HTTP 404 и сообщением об ошибке
     */
    @ExceptionHandler(StateNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStateNotFoundException(
            final StateNotFoundException ex
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_NOT_FOUND);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработка исключения, когда пользователь не является бронирующим или владельцем.
     * <p>
     * Перехватывает случаи, когда пользователь пытается выполнить действие,
     * доступное только бронирующему или владельцу, и возвращает HTTP-статус 403.
     *
     * @param ex исключение недействительного бронирующего или владельца
     * @return ответ с HTTP 403 и сообщением об ошибке
     */
    @ExceptionHandler(InvalidBookingBookerOrOwner.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBookerOrOwnerException(
            final InvalidBookingBookerOrOwner ex
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_FORBIDDEN);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    /**
     * Обработка исключения, когда пользователь не является владельцем бронирования.
     * <p>
     * Перехватывает случаи, когда пользователь пытается изменить бронирование,
     * принадлежащее другому пользователю, и возвращает HTTP-статус 403.
     *
     * @param ex исключение недействительного владельца
     * @return ответ с HTTP 403 и сообщением об ошибке
     */
    @ExceptionHandler(InvalidOwnerBooking.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOwnerException(
            final InvalidOwnerBooking ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_FORBIDDEN);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    /**
     * Обработка исключения, когда изменение статуса бронирования не разрешено.
     * <p>
     * Перехватывает случаи, когда попытка изменить статус бронирования
     * противоречит бизнес-правилам, и возвращает HTTP-статус 409.
     *
     * @param ex исключение недопустимого изменения статуса
     * @return ответ с HTTP 409 и сообщением об ошибке
     */
    @ExceptionHandler(StatusChangeNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStatusException(
            final StatusChangeNotAllowedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_CONFLICT);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Обработка исключения валидации дат бронирования.
     * <p>
     * Перехватывает случаи, когда даты бронирования некорректны (например, конец раньше начала),
     * и возвращает HTTP-статус 400 с сообщением об ошибке.
     *
     * @param ex исключение валидации дат
     * @return ответ с HTTP 400 и сообщением об ошибке
     */
    @ExceptionHandler(ValidationDateException.class)
    public ResponseEntity<Map<String, Object>> handleValidationDateException(
            final ValidationDateException ex
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_BAD_REQUEST);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключения, когда бронирующий недействителен.
     * <p>
     * Перехватывает случаи, когда пользователь не может бронировать предмет,
     * и возвращает HTTP-статус 400 с сообщением об ошибке.
     *
     * @param ex исключение недействительного бронирующего
     * @return ответ с HTTP 400 и сообщением об ошибке
     */
    @ExceptionHandler(InvalidBookerException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBookerException(
            final InvalidBookerException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_BAD_REQUEST);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключения, когда владелец недействителен.
     * <p>
     * Перехватывает случаи, когда пользователь не является владельцем предмета,
     * и возвращает HTTP-статус 403 с сообщением об ошибке.
     *
     * @param ex исключение недействительного владельца
     * @return ответ с HTTP 403 и сообщением об ошибке
     */
    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOwnerException(
            final InvalidOwnerException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_FORBIDDEN);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    /**
     * Обработка исключения, когда предмет не найден.
     * <p>
     * Перехватывает случаи, когда запрашиваемый предмет отсутствует в системе,
     * и возвращает HTTP-статус 404 с сообщением об ошибке.
     *
     * @param ex исключение отсутствия предмета
     * @return ответ с HTTP 404 и сообщением об ошибке
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleItemNotFoundException(
            final ItemNotFoundException ex
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_NOT_FOUND);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработка исключения, когда данные пустые или null.
     * <p>
     * Перехватывает случаи, когда обязательные поля отсутствуют или пустые,
     * и возвращает HTTP-статус 400 с сообщением об ошибке.
     *
     * @param ex исключение пустых или null данных
     * @return ответ с HTTP 400 и сообщением об ошибке
     */
    @ExceptionHandler(NullOrEmptyException.class)
    public ResponseEntity<Map<String, Object>> handleNullOrEmptyException(
            final NullOrEmptyException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_BAD_REQUEST);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключения, когда email уже существует.
     * <p>
     * Перехватывает случаи, когда при создании пользователя указан уже занятый email,
     * и возвращает HTTP-статус 409 с сообщением об ошибке.
     *
     * @param ex исключение существующего email
     * @return ответ с HTTP 409 и сообщением об ошибке
     */
    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Map<String, Object>> handleExistsEmailException(
            final EmailExistsException ex
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_CONFLICT);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Обработка исключения, когда пользователь не найден.
     * <p>
     * Перехватывает случаи, когда запрашиваемый пользователь отсутствует в системе,
     * и возвращает HTTP-статус 404 с сообщением об ошибке.
     *
     * @param ex исключение отсутствия пользователя
     * @return ответ с HTTP 404 и сообщением об ошибке
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(
            final UserNotFoundException ex
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_NOT_FOUND);
        body.put("errorMessages", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}

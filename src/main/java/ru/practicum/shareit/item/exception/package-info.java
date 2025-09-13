/**
 * Пакет исключений для предметов (items) в приложении ShareIt.
 * <p>
 * Этот пакет содержит кастомные исключения, связанные с бизнес-логикой предметов,
 * такие как ошибки доступа, отсутствия данных или некорректные действия пользователя.
 * Исключения наследуются от RuntimeException и используются для обработки ошибок в сервисах и контроллерах.
 * <p>
 * Основные классы:
 * - {@link ru.practicum.shareit.item.exception.ItemNotFoundException} - исключение при отсутствии предмета.
 * - {@link ru.practicum.shareit.item.exception.InvalidOwnerException} - исключение при некорректном владельце.
 */
package ru.practicum.shareit.item.exception;
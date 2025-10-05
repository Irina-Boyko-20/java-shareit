/**
 * Пакет DTO (Data Transfer Objects) для предметов (items) в приложении ShareIt.
 * <p>
 * Этот пакет содержит классы для передачи данных между слоями приложения,
 * включая запросы и ответы для предметов, комментариев и связанных сущностей.
 * DTO используются для валидации входных данных и сериализации/десериализации JSON.
 * <p>
 * Основные классы:
 * - {@link ru.practicum.shareit.item.dto.ItemDto} - DTO для создания предмета.
 * - {@link ru.practicum.shareit.item.dto.ItemResponseDto} - DTO для ответа с информацией о предмете.
 * - {@link ru.practicum.shareit.item.dto.CommentDto} - DTO для создания комментария.
 */
package ru.practicum.shareit.item.dto;
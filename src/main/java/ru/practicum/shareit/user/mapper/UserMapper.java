package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;

/**
 * Mapper для преобразования между сущностью User и DTO.
 * Использует MapStruct для генерации реализации.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует DTO пользователя в сущность.
     *
     * @param userDto DTO пользователя.
     * @return сущность User.
     */
    User toUser(UserDto userDto);

    /**
     * Преобразует сущность пользователя в DTO для ответа.
     *
     * @param user сущность User.
     * @return DTO пользователя.
     */
    UserResponseDto toUserDto(User user);

    /**
     * Обновляет сущность пользователя данными из DTO обновления.
     * Игнорирует поле id.
     *
     * @param user          сущность пользователя для обновления.
     * @param updateRequest DTO с новыми данными.
     */
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserRequest(@MappingTarget User user, UserUpdateRequestDto updateRequest);
}

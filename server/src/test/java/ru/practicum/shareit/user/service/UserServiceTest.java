package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.exception.EmailExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    private User existingUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        existingUser = userRepository.save(User.builder()
                .name("Mike")
                .email("mike@email.com")
                .build());
    }

    @Test
    void create_shouldCreateUser() {
        UserDto request = new UserDto(2L, "Nike", "nike@email.com");

        UserResponseDto result = userService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isPositive();
        assertThat(result.name()).isEqualTo("Nike");
        assertThat(result.email()).isEqualTo("nike@email.com");

        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).extracting(User::getEmail)
                .contains("nike@email.com");
    }

    @Test
    void create_shouldThrowWhenEmailAlreadyExists() {
        UserDto request = new UserDto(2L, "Nike", "mike@email.com");

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(EmailExistsException.class)
                .hasMessage("This email already exists mike@email.com");
    }

    @Test
    void create_shouldHandleLongNamesAndEmails() {
        String longName = "A".repeat(50);
        String longEmail = "test@" + "a".repeat(45) + ".com";

        if (longEmail.length() <= 50) {
            UserDto request = new UserDto(4L, longName, longEmail);

            UserResponseDto result = userService.create(request);

            assertThat(result.name()).isEqualTo(longName);
            assertThat(result.email()).isEqualTo(longEmail);
        }
    }

    @Test
    void update_shouldUpdateAllFields() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("Updated Name", "updated@test.com");

        UserResponseDto result = userService.update(existingUser.getId(), request);

        assertThat(result.name()).isEqualTo("Updated Name");
        assertThat(result.email()).isEqualTo("updated@test.com");

        User updatedUser = userRepository.findById(existingUser.getId()).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@test.com");
    }

    @Test
    void update_shouldUpdatePartialFields() {
        UserUpdateRequestDto nameOnlyRequest = new UserUpdateRequestDto("New Mike", null);
        UserResponseDto nameResult = userService.update(existingUser.getId(), nameOnlyRequest);

        assertThat(nameResult.name()).isEqualTo("New Mike");
        assertThat(nameResult.email()).isEqualTo("mike@email.com");

        UserUpdateRequestDto emailOnlyRequest = new UserUpdateRequestDto(null, "newmike@email.com");
        UserResponseDto emailResult = userService.update(existingUser.getId(), emailOnlyRequest);

        assertThat(emailResult.name()).isEqualTo("New Mike");
        assertThat(emailResult.email()).isEqualTo("newmike@email.com");
    }

    @Test
    void update_shouldThrowWhenEmailAlreadyExists() {
        User anotherUser = userRepository.save(
                User.builder()
                        .name("Name")
                        .email("another@test.com")
                        .build()
        );

        UserUpdateRequestDto request = new UserUpdateRequestDto("Name", "another@test.com");

        assertThatThrownBy(() -> userService.update(existingUser.getId(), request))
                .isInstanceOf(EmailExistsException.class)
                .hasMessage("This email already exists another@test.com");
    }

    @Test
    void update_shouldNotThrowWhenEmailNotChanged() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("New Mike", "mike@email.com");

        assertThatCode(() -> userService.update(existingUser.getId(), request))
                .doesNotThrowAnyException();

        UserResponseDto result = userService.update(existingUser.getId(), request);
        assertThat(result.email()).isEqualTo("mike@email.com");
        assertThat(result.name()).isEqualTo("New Mike");
    }

    @Test
    void update_shouldThrowWhenUserNotFound() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("Name", "email@test.com");

        assertThatThrownBy(() -> userService.update(999L, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id = 999 not found");
    }

    @Test
    void update_shouldWorkWithSameEmailDifferentCase() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("Name", "EXISTING@test.com");

        try {
            UserResponseDto result = userService.update(existingUser.getId(), request);
            assertThat(result.email()).isEqualTo("EXISTING@test.com");
        } catch (EmailExistsException e) {
            assertThat(e.getMessage()).contains("Email already exists");
        }
    }

    @Test
    void findById_shouldReturnUser() {
        UserResponseDto result = userService.findById(existingUser.getId());

        assertThat(result.id()).isEqualTo(existingUser.getId());
        assertThat(result.name()).isEqualTo("Mike");
        assertThat(result.email()).isEqualTo("mike@email.com");
    }

    @Test
    void findById_shouldThrowWhenUserNotFound() {
        assertThatThrownBy(() -> userService.findById(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id = 999 not found");
    }

    @Test
    void delete_shouldDeleteUser() {
        userService.delete(existingUser.getId());

        assertThat(userRepository.existsById(existingUser.getId())).isFalse();

        assertThatThrownBy(() -> userService.findById(existingUser.getId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void delete_shouldThrowWhenUserNotFound() {
        assertThatThrownBy(() -> userService.delete(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id = 999 not found");
    }

    @Test
    void userExists_shouldReturnUser() {
        User result = userService.userExists(existingUser.getId());

        assertThat(result.getId()).isEqualTo(existingUser.getId());
        assertThat(result.getName()).isEqualTo("Mike");
        assertThat(result.getEmail()).isEqualTo("mike@email.com");
    }

    @Test
    void userExists_shouldThrowWhenUserNotFound() {
        assertThatThrownBy(() -> userService.userExists(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id = 999 not found");
    }
}

package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmail() {
        User user = userRepository.save(User.builder()
                .name("Mike")
                .email("mike@email.com")
                .build());

        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        assertTrue(existsByEmail);
    }
}

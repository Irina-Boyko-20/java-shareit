package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения ShareIt.
 * <p>
 * При запуске приложения инициализируются все необходимые бины, контроллеры, сервисы и репозитории,
 * обеспечивая работу REST API для управления пользователями, вещами, бронированиями и запросами.
 */
@SpringBootApplication
public class ShareItServer {

    public static void main(String[] args) {
        SpringApplication.run(ShareItServer.class, args);
    }
}

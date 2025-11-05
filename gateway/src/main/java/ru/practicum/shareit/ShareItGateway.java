package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения ShareItGateway.
 * <p>
 * Точка входа для запуска Spring Boot приложения, выполняющего роль шлюза (gateway).
 * Запускает контекст Spring и инициализирует все необходимые компоненты.
 * </p>
 */
@SpringBootApplication
public class ShareItGateway {
    public static void main(String[] args) {
        SpringApplication.run(ShareItGateway.class, args);
    }
}

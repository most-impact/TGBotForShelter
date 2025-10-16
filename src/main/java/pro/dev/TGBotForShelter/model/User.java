package pro.dev.TGBotForShelter.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Модель пользователя телеграм-бота
 */
@Entity
@Table(name = "users")
@Data
public class User {

    /**
     * Уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Telegram ID пользователя
     */
    @Column(nullable = false, unique = true)
    private Long telegramId;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Username в Telegram
     */
    private String username;

    /**
     * Номер телефона
     */
    private String phoneNumber;

    /**
     * Email
     */
    private String email;

    /**
     * Дата регистрации
     */
    @Column(nullable = false)
    private LocalDateTime registeredAt;

    /**
     * Является ли пользователь волонтером
     */
    @Column(nullable = false)
    private boolean isVolunteer = false;
}

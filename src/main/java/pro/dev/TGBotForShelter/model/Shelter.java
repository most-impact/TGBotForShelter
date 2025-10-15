package pro.dev.TGBotForShelter.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель приюта для животных
 */
@Entity
@Table(name = "shelters")
@Data
public class Shelter {

    /**
     * Уникальный идентификатор приюта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип приюта (для кошек или собак)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShelterType type;

    /**
     * Название приюта
     */
    @Column(nullable = false)
    private String name;

    /**
     * Адрес приюта
     */
    @Column(nullable = false)
    private String address;

    /**
     * Расписание работы
     */
    @Column(length = 1000)
    private String schedule;

    /**
     * Контактные данные охраны
     */
    private String securityContact;

    /**
     * Правила техники безопасности
     */
    @Column(length = 2000)
    private String safetyRules;

    /**
     * Информация о приюте
     */
    @Column(length = 2000)
    private String information;
}

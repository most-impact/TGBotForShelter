package pro.dev.TGBotForShelter.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель питомца
 */
@Entity
@Table(name = "pets")
@Data
public class Pet {

    /**
     * Уникальный идентификатор питомца
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Кличка питомца
     */
    @Column(nullable = false)
    private String name;

    /**
     * Тип животного
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShelterType type;

    /**
     * Порода
     */
    private String breed;

    /**
     * Возраст (в годах)
     */
    private Integer age;

    /**
     * Описание
     */
    @Column(length = 1000)
    private String description;

    /**
     * Имеет ли ограниченные возможности
     */
    @Column(nullable = false)
    private boolean hasDisabilities = false;
}
package pro.dev.TGBotForShelter.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Модель усыновителя животного
 */
@Entity
@Table(name = "adopters")
@Data
public class Adopter {

    /**
     * Уникальный идентификатор усыновителя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Приют, из которого взято животное
     */
    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    /**
     * Питомец
     */
    @OneToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    /**
     * Дата усыновления
     */
    @Column(nullable = false)
    private LocalDateTime adoptionDate;
}
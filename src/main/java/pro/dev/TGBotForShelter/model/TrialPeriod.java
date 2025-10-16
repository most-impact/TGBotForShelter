package pro.dev.TGBotForShelter.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Модель испытательного срока
 */
@Entity
@Table(name = "trial_periods")
@Data
public class TrialPeriod {

    /**
     * Уникальный идентификатор испытательного срока
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Усыновитель
     */
    @OneToOne
    @JoinColumn(name = "adopter_id", nullable = false)
    private Adopter adopter;

    /**
     * Дата начала испытательного срока
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * Дата окончания испытательного срока
     */
    @Column(nullable = false)
    private LocalDate endDate;

    /**
     * Статус испытательного срока
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrialPeriodStatus status = TrialPeriodStatus.ACTIVE;

    /**
     * Количество пропущенных отчетов
     */
    @Column(nullable = false)
    private int missedReports = 0;
}
package pro.dev.TGBotForShelter.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Модель ежедневного отчета о питомце
 */
@Entity
@Table(name = "daily_reports")
@Data
public class DailyReport {

    /**
     * Уникальный идентификатор отчета
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Испытательный срок
     */
    @ManyToOne
    @JoinColumn(name = "trial_period_id", nullable = false)
    private TrialPeriod trialPeriod;

    /**
     * Дата отчета
     */
    @Column(nullable = false)
    private LocalDate reportDate;

    /**
     * Путь к фото питомца
     */
    private String photoPath;

    /**
     * Рацион питомца
     */
    @Column(length = 1000)
    private String diet;

    /**
     * Общее самочувствие
     */
    @Column(length = 1000)
    private String wellBeing;

    /**
     * Изменения в поведении
     */
    @Column(length = 1000)
    private String behaviorChanges;

    /**
     * Время создания отчета
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Отчет проверен волонтером
     */
    @Column(nullable = false)
    private boolean reviewed = false;

    /**
     * Отчет заполнен некачественно
     */
    @Column(nullable = false)
    private boolean poorQuality = false;
}
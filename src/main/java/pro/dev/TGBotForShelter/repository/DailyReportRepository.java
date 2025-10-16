package pro.dev.TGBotForShelter.repository;


import pro.dev.TGBotForShelter.model.DailyReport;
import pro.dev.TGBotForShelter.model.TrialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с ежедневными отчетами
 */
@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

    /**
     * Находит отчет по испытательному сроку и дате
     *
     * @param trialPeriod испытательный срок
     * @param reportDate дата отчета
     * @return отчет, если найден
     */
    Optional<DailyReport> findByTrialPeriodAndReportDate(TrialPeriod trialPeriod, LocalDate reportDate);

    /**
     * Находит все отчеты по испытательному сроку
     *
     * @param trialPeriod испытательный срок
     * @return список отчетов
     */
    List<DailyReport> findAllByTrialPeriod(TrialPeriod trialPeriod);

    /**
     * Находит все отчеты по ID испытательного срока
     *
     * @param trialPeriodId идентификатор испытательного срока
     * @return список отчетов
     */
    List<DailyReport> findAllByTrialPeriodId(Long trialPeriodId);

    /**
     * Находит отчеты по дате отчета
     *
     * @param reportDate дата отчета
     * @return список отчетов
     */
    List<DailyReport> findAllByReportDate(LocalDate reportDate);

    /**
     * Находит отчеты за период
     *
     * @param startDate начальная дата
     * @param endDate конечная дата
     * @return список отчетов
     */
    List<DailyReport> findAllByReportDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Находит непроверенные отчеты
     *
     * @param reviewed флаг проверки
     * @return список отчетов
     */
    List<DailyReport> findAllByReviewed(boolean reviewed);

    /**
     * Находит отчеты низкого качества
     *
     * @param poorQuality флаг низкого качества
     * @return список отчетов
     */
    List<DailyReport> findAllByPoorQuality(boolean poorQuality);

    /**
     * Находит непроверенные отчеты, созданные до указанной даты
     *
     * @param createdBefore дата создания
     * @param reviewed флаг проверки
     * @return список отчетов
     */
    List<DailyReport> findAllByCreatedAtBeforeAndReviewed(LocalDateTime createdBefore, boolean reviewed);

    /**
     * Находит отчеты по испытательному сроку и статусу проверки
     *
     * @param trialPeriod испытательный срок
     * @param reviewed флаг проверки
     * @return список отчетов
     */
    List<DailyReport> findAllByTrialPeriodAndReviewed(TrialPeriod trialPeriod, boolean reviewed);

    /**
     * Подсчитывает количество отчетов по испытательному сроку
     *
     * @param trialPeriod испытательный срок
     * @return количество отчетов
     */
    long countByTrialPeriod(TrialPeriod trialPeriod);

    /**
     * Подсчитывает количество отчетов низкого качества по испытательному сроку
     *
     * @param trialPeriod испытательный срок
     * @param poorQuality флаг низкого качества
     * @return количество отчетов
     */
    long countByTrialPeriodAndPoorQuality(TrialPeriod trialPeriod, boolean poorQuality);

    /**
     * Проверяет существование отчета за определенную дату по испытательному сроку
     *
     * @param trialPeriod испытательный срок
     * @param reportDate дата отчета
     * @return true, если отчет существует
     */
    boolean existsByTrialPeriodAndReportDate(TrialPeriod trialPeriod, LocalDate reportDate);

    /**
     * Находит отчеты для проверки волонтером (созданные после 21:00 предыдущего дня)
     *
     * @param cutoffTime время отсечки
     * @return список отчетов
     */
    @Query("SELECT dr FROM DailyReport dr WHERE dr.reviewed = false " +
            "AND dr.createdAt < :cutoffTime ORDER BY dr.createdAt ASC")
    List<DailyReport> findReportsForVolunteerReview(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Находит последний отчет по испытательному сроку
     *
     * @param trialPeriodId идентификатор испытательного срока
     * @return последний отчет, если найден
     */
    @Query("SELECT dr FROM DailyReport dr WHERE dr.trialPeriod.id = :trialPeriodId " +
            "ORDER BY dr.reportDate DESC LIMIT 1")
    Optional<DailyReport> findLatestReportByTrialPeriodId(@Param("trialPeriodId") Long trialPeriodId);

    /**
     * Находит отчеты по ID приюта через связь с испытательным сроком и усыновителем
     *
     * @param shelterId идентификатор приюта
     * @return список отчетов
     */
    @Query("SELECT dr FROM DailyReport dr " +
            "JOIN dr.trialPeriod tp " +
            "JOIN tp.adopter a " +
            "WHERE a.shelter.id = :shelterId")
    List<DailyReport> findAllByShelterId(@Param("shelterId") Long shelterId);
}
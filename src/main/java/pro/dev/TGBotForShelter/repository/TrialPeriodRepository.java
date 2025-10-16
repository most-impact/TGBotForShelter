package pro.dev.TGBotForShelter.repository;


import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.model.TrialPeriod;
import pro.dev.TGBotForShelter.model.TrialPeriodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с испытательными сроками
 */
@Repository
public interface TrialPeriodRepository extends JpaRepository<TrialPeriod, Long> {

    /**
     * Находит испытательный срок по усыновителю
     *
     * @param adopter усыновитель
     * @return испытательный срок, если найден
     */
    Optional<TrialPeriod> findByAdopter(Adopter adopter);

    /**
     * Находит испытательный срок по ID усыновителя
     *
     * @param adopterId идентификатор усыновителя
     * @return испытательный срок, если найден
     */
    Optional<TrialPeriod> findByAdopterId(Long adopterId);

    /**
     * Находит все испытательные сроки по статусу
     *
     * @param status статус испытательного срока
     * @return список испытательных сроков
     */
    List<TrialPeriod> findAllByStatus(TrialPeriodStatus status);

    /**
     * Находит активные испытательные сроки, которые заканчиваются в указанную дату
     *
     * @param endDate дата окончания
     * @param status статус
     * @return список испытательных сроков
     */
    List<TrialPeriod> findAllByEndDateAndStatus(LocalDate endDate, TrialPeriodStatus status);

    /**
     * Находит активные испытательные сроки, которые заканчиваются до указанной даты
     *
     * @param endDate дата окончания
     * @param status статус
     * @return список испытательных сроков
     */
    List<TrialPeriod> findAllByEndDateBeforeAndStatus(LocalDate endDate, TrialPeriodStatus status);

    /**
     * Находит активные испытательные сроки с количеством пропущенных отчетов больше указанного
     *
     * @param missedReports количество пропущенных отчетов
     * @param status статус
     * @return список испытательных сроков
     */
    List<TrialPeriod> findAllByMissedReportsGreaterThanAndStatus(int missedReports, TrialPeriodStatus status);

    /**
     * Находит все активные испытательные сроки
     *
     * @return список активных испытательных сроков
     */
    @Query("SELECT tp FROM TrialPeriod tp WHERE tp.status = 'ACTIVE' AND tp.endDate >= :currentDate")
    List<TrialPeriod> findAllActiveTrialPeriods(@Param("currentDate") LocalDate currentDate);

    /**
     * Находит испытательные сроки по ID приюта через связь с усыновителем
     *
     * @param shelterId идентификатор приюта
     * @return список испытательных сроков
     */
    @Query("SELECT tp FROM TrialPeriod tp JOIN tp.adopter a WHERE a.shelter.id = :shelterId")
    List<TrialPeriod> findAllByShelterId(@Param("shelterId") Long shelterId);

    /**
     * Находит испытательные сроки, требующие внимания волонтера
     *
     * @param missedReportsThreshold порог пропущенных отчетов
     * @param currentDate текущая дата
     * @return список испытательных сроков
     */
    @Query("SELECT tp FROM TrialPeriod tp WHERE tp.status = 'ACTIVE' " +
            "AND (tp.missedReports >= :missedReportsThreshold OR tp.endDate <= :currentDate)")
    List<TrialPeriod> findTrialPeriodsRequiringAttention(
            @Param("missedReportsThreshold") int missedReportsThreshold,
            @Param("currentDate") LocalDate currentDate);
}
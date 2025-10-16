package pro.dev.TGBotForShelter.service;


import pro.dev.TGBotForShelter.model.DailyReport;
import pro.dev.TGBotForShelter.model.TrialPeriod;
import pro.dev.TGBotForShelter.repository.DailyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Сервис для работы с ежедневными отчетами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;
    private final TrialPeriodService trialPeriodService;

    private static final LocalTime VOLUNTEER_REVIEW_TIME = LocalTime.of(21, 0);



    /**
     * Создает новый отчет
     *
     * @param dailyReport данные отчета
     * @return идентификатор созданного отчета
     */
    @Transactional
    public Long createDailyReport(DailyReport dailyReport) {
        TrialPeriod trialPeriod = trialPeriodService.getTrialPeriod(dailyReport.getTrialPeriod().getId());

        if (dailyReportRepository.existsByTrialPeriodAndReportDate(trialPeriod, dailyReport.getReportDate())) {
            throw new IllegalArgumentException("Отчет за " + dailyReport.getReportDate() + " уже существует");
        }

        dailyReport.setTrialPeriod(trialPeriod);
        dailyReport.setCreatedAt(LocalDateTime.now());
        dailyReport.setReviewed(false);
        dailyReport.setPoorQuality(false);

        DailyReport savedReport = dailyReportRepository.save(dailyReport);

        trialPeriodService.resetMissedReports(trialPeriod.getId());

        return savedReport.getId();
    }

    /**
     * Получает отчет по идентификатору
     *
     * @param id идентификатор отчета
     * @return данные отчета
     */
    public DailyReport getDailyReport(Long id) {
        return dailyReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Отчет с id " + id + " не найден"));
    }

    /**
     * Получает все отчеты
     *
     * @param trialPeriodId фильтр по испытательному сроку (опционально)
     * @param reviewed фильтр по статусу проверки (опционально)
     * @param shelterId фильтр по приюту (опционально)
     * @return список отчетов
     */
    public List<DailyReport> getAllDailyReports(Long trialPeriodId, Boolean reviewed, Long shelterId) {
        if (trialPeriodId != null) {
            if (reviewed != null) {
                TrialPeriod trialPeriod = trialPeriodService.getTrialPeriod(trialPeriodId);
                return dailyReportRepository.findAllByTrialPeriodAndReviewed(trialPeriod, reviewed);
            }
            return dailyReportRepository.findAllByTrialPeriodId(trialPeriodId);
        }
        if (reviewed != null) {
            return dailyReportRepository.findAllByReviewed(reviewed);
        }
        if (shelterId != null) {
            return dailyReportRepository.findAllByShelterId(shelterId);
        }
        return dailyReportRepository.findAll();
    }

    /**
     * Получает отчеты для проверки волонтером
     *
     * @return список непроверенных отчетов
     */
    public List<DailyReport> getReportsForVolunteerReview() {
        LocalDateTime cutoffTime = LocalDateTime.of(LocalDate.now(), VOLUNTEER_REVIEW_TIME);
        return dailyReportRepository.findReportsForVolunteerReview(cutoffTime);
    }

    /**
     * Получает последний отчет по испытательному сроку
     *
     * @param trialPeriodId идентификатор испытательного срока
     * @return последний отчет
     */
    public DailyReport getLatestReport(Long trialPeriodId) {
        return dailyReportRepository.findLatestReportByTrialPeriodId(trialPeriodId)
                .orElseThrow(() -> new IllegalArgumentException("Отчеты для испытательного срока с id " + trialPeriodId + " не найдены"));
    }

    /**
     * Отмечает отчет как проверенный
     *
     * @param id идентификатор отчета
     * @param poorQuality отчет низкого качества
     * @return обновленные данные отчета
     */
    @Transactional
    public DailyReport reviewReport(Long id, boolean poorQuality) {
        DailyReport report = getDailyReport(id);
        report.setReviewed(true);
        report.setPoorQuality(poorQuality);
        return dailyReportRepository.save(report);
    }

    /**
     * Обновляет данные отчета
     *
     * @param id идентификатор отчета
     * @param dailyReport новые данные отчета
     * @return обновленные данные отчета
     */
    @Transactional
    public DailyReport updateDailyReport(Long id, DailyReport dailyReport) {
        DailyReport existingReport = getDailyReport(id);

        if (dailyReport.getPhotoPath() != null) {
            existingReport.setPhotoPath(dailyReport.getPhotoPath());
        }
        if (dailyReport.getDiet() != null) {
            existingReport.setDiet(dailyReport.getDiet());
        }
        if (dailyReport.getWellBeing() != null) {
            existingReport.setWellBeing(dailyReport.getWellBeing());
        }
        if (dailyReport.getBehaviorChanges() != null) {
            existingReport.setBehaviorChanges(dailyReport.getBehaviorChanges());
        }

        return dailyReportRepository.save(existingReport);
    }

    /**
     * Удаляет отчет
     *
     * @param id идентификатор отчета
     */
    @Transactional
    public void deleteDailyReport(Long id) {
        if (!dailyReportRepository.existsById(id)) {
            throw new IllegalArgumentException("Отчет с id " + id + " не найден");
        }
        dailyReportRepository.deleteById(id);
    }

    /**
     * Проверяет наличие отчета за сегодня
     *
     * @param trialPeriodId идентификатор испытательного срока
     * @return true, если отчет существует
     */
    public boolean hasTodayReport(Long trialPeriodId) {
        TrialPeriod trialPeriod = trialPeriodService.getTrialPeriod(trialPeriodId);
        return dailyReportRepository.existsByTrialPeriodAndReportDate(trialPeriod, LocalDate.now());
    }
}
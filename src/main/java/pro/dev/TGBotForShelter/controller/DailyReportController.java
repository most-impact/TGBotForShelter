package pro.dev.TGBotForShelter.controller;

import pro.dev.TGBotForShelter.model.DailyReport;
import pro.dev.TGBotForShelter.service.DailyReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления ежедневными отчетами
 */
@RestController
@RequestMapping("/api/daily-reports")
@RequiredArgsConstructor
@Tag(name = "Daily Reports", description = "API для управления ежедневными отчетами о питомцах")
public class DailyReportController {

    private final DailyReportService dailyReportService;



    /**
     * Создает новый отчет
     *
     * @param dailyReport данные отчета
     * @return идентификатор созданного отчета
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать отчет", description = "Создает новый ежедневный отчет о питомце")
    public Long createDailyReport(@RequestBody DailyReport dailyReport) {
        return dailyReportService.createDailyReport(dailyReport);
    }

    /**
     * Получает отчет по идентификатору
     *
     * @param id идентификатор отчета
     * @return данные отчета
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить отчет по ID", description = "Возвращает данные отчета по идентификатору")
    public DailyReport getDailyReport(@PathVariable Long id) {
        return dailyReportService.getDailyReport(id);
    }

    /**
     * Получает все отчеты
     *
     * @param trialPeriodId фильтр по испытательному сроку (опционально)
     * @param reviewed фильтр по статусу проверки (опционально)
     * @param shelterId фильтр по приюту (опционально)
     * @return список отчетов
     */
    @GetMapping
    @Operation(summary = "Получить все отчеты", description = "Возвращает список всех отчетов с возможностью фильтрации")
    public List<DailyReport> getAllDailyReports(
            @RequestParam(required = false) Long trialPeriodId,
            @RequestParam(required = false) Boolean reviewed,
            @RequestParam(required = false) Long shelterId) {
        return dailyReportService.getAllDailyReports(trialPeriodId, reviewed, shelterId);
    }

    /**
     * Получает отчеты для проверки волонтером
     *
     * @return список непроверенных отчетов
     */
    @GetMapping("/for-review")
    @Operation(summary = "Получить отчеты для проверки", description = "Возвращает отчеты, ожидающие проверки волонтером")
    public List<DailyReport> getReportsForVolunteerReview() {
        return dailyReportService.getReportsForVolunteerReview();
    }

    /**
     * Получает последний отчет по испытательному сроку
     *
     * @param trialPeriodId идентификатор испытательного срока
     * @return последний отчет
     */
    @GetMapping("/latest/{trialPeriodId}")
    @Operation(summary = "Получить последний отчет", description = "Возвращает последний отчет по испытательному сроку")
    public DailyReport getLatestReport(@PathVariable Long trialPeriodId) {
        return dailyReportService.getLatestReport(trialPeriodId);
    }

    /**
     * Проверяет наличие отчета за сегодня
     *
     * @param trialPeriodId идентификатор испытательного срока
     * @return true, если отчет существует
     */
    @GetMapping("/has-today/{trialPeriodId}")
    @Operation(summary = "Проверить наличие отчета за сегодня", description = "Проверяет, был ли отправлен отчет сегодня")
    public boolean hasTodayReport(@PathVariable Long trialPeriodId) {
        return dailyReportService.hasTodayReport(trialPeriodId);
    }

    /**
     * Отмечает отчет как проверенный
     *
     * @param id идентификатор отчета
     * @param poorQuality отчет низкого качества
     * @return обновленные данные отчета
     */
    @PatchMapping("/{id}/review")
    @Operation(summary = "Проверить отчет", description = "Отмечает отчет как проверенный волонтером")
    public DailyReport reviewReport(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean poorQuality) {
        return dailyReportService.reviewReport(id, poorQuality);
    }

    /**
     * Обновляет данные отчета
     *
     * @param id идентификатор отчета
     * @param dailyReport новые данные отчета
     * @return обновленные данные отчета
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить отчет", description = "Обновляет данные существующего отчета")
    public DailyReport updateDailyReport(@PathVariable Long id, @RequestBody DailyReport dailyReport) {
        return dailyReportService.updateDailyReport(id, dailyReport);
    }

    /**
     * Удаляет отчет
     *
     * @param id идентификатор отчета
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить отчет", description = "Удаляет отчет из системы")
    public void deleteDailyReport(@PathVariable Long id) {
        dailyReportService.deleteDailyReport(id);
    }
}

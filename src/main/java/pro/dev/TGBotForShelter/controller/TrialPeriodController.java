package pro.dev.TGBotForShelter.controller;

import pro.dev.TGBotForShelter.model.TrialPeriod;
import pro.dev.TGBotForShelter.model.TrialPeriodStatus;
import pro.dev.TGBotForShelter.service.TrialPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления испытательными сроками
 */
@RestController
@RequestMapping("/api/trial-periods")
@RequiredArgsConstructor
@Tag(name = "Trial Periods", description = "API для управления испытательными сроками")
public class TrialPeriodController {

    private final TrialPeriodService trialPeriodService;


    /**
     * Создает новый испытательный срок
     *
     * @param trialPeriod данные испытательного срока
     * @return идентификатор созданного испытательного срока
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать испытательный срок", description = "Регистрирует новый испытательный срок в системе")
    public Long createTrialPeriod(@RequestBody TrialPeriod trialPeriod) {
        return trialPeriodService.createTrialPeriod(trialPeriod);
    }

    /**
     * Получает испытательный срок по идентификатору
     *
     * @param id идентификатор испытательного срока
     * @return данные испытательного срока
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить испытательный срок по ID", description = "Возвращает данные испытательного срока по идентификатору")
    public TrialPeriod getTrialPeriod(@PathVariable Long id) {
        return trialPeriodService.getTrialPeriod(id);
    }

    /**
     * Получает испытательный срок по ID усыновителя
     *
     * @param adopterId идентификатор усыновителя
     * @return данные испытательного срока
     */
    @GetMapping("/adopter/{adopterId}")
    @Operation(summary = "Получить испытательный срок по ID усыновителя", description = "Возвращает испытательный срок усыновителя")
    public TrialPeriod getTrialPeriodByAdopterId(@PathVariable Long adopterId) {
        return trialPeriodService.getTrialPeriodByAdopterId(adopterId);
    }

    /**
     * Получает все испытательные сроки
     *
     * @param status фильтр по статусу (опционально)
     * @param shelterId фильтр по приюту (опционально)
     * @return список испытательных сроков
     */
    @GetMapping
    @Operation(summary = "Получить все испытательные сроки", description = "Возвращает список всех испытательных сроков с возможностью фильтрации")
    public List<TrialPeriod> getAllTrialPeriods(
            @RequestParam(required = false) TrialPeriodStatus status,
            @RequestParam(required = false) Long shelterId) {
        return trialPeriodService.getAllTrialPeriods(status, shelterId);
    }

    /**
     * Получает активные испытательные сроки
     *
     * @return список активных испытательных сроков
     */
    @GetMapping("/active")
    @Operation(summary = "Получить активные испытательные сроки", description = "Возвращает список активных испытательных сроков")
    public List<TrialPeriod> getActiveTrialPeriods() {
        return trialPeriodService.getActiveTrialPeriods();
    }

    /**
     * Получает испытательные сроки, требующие внимания
     *
     * @return список испытательных сроков
     */
    @GetMapping("/requiring-attention")
    @Operation(summary = "Получить испытательные сроки, требующие внимания", description = "Возвращает испытательные сроки с пропущенными отчетами или истекающим сроком")
    public List<TrialPeriod> getTrialPeriodsRequiringAttention() {
        return trialPeriodService.getTrialPeriodsRequiringAttention();
    }

    /**
     * Увеличивает счетчик пропущенных отчетов
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @PatchMapping("/{id}/increment-missed-reports")
    @Operation(summary = "Увеличить счетчик пропущенных отчетов", description = "Увеличивает количество пропущенных отчетов на 1")
    public TrialPeriod incrementMissedReports(@PathVariable Long id) {
        return trialPeriodService.incrementMissedReports(id);
    }

    /**
     * Сбрасывает счетчик пропущенных отчетов
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @PatchMapping("/{id}/reset-missed-reports")
    @Operation(summary = "Сбросить счетчик пропущенных отчетов", description = "Сбрасывает количество пропущенных отчетов до 0")
    public TrialPeriod resetMissedReports(@PathVariable Long id) {
        return trialPeriodService.resetMissedReports(id);
    }

    /**
     * Завершает испытательный срок успешно
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @PatchMapping("/{id}/pass")
    @Operation(summary = "Завершить испытательный срок успешно", description = "Отмечает испытательный срок как пройденный")
    public TrialPeriod passTrialPeriod(@PathVariable Long id) {
        return trialPeriodService.passTrialPeriod(id);
    }

    /**
     * Продлевает испытательный срок на 14 дней
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @PatchMapping("/{id}/extend-14")
    @Operation(summary = "Продлить испытательный срок на 14 дней", description = "Продлевает испытательный срок на 14 дней")
    public TrialPeriod extendTrialPeriod14Days(@PathVariable Long id) {
        return trialPeriodService.extendTrialPeriod14Days(id);
    }

    /**
     * Продлевает испытательный срок на 30 дней
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @PatchMapping("/{id}/extend-30")
    @Operation(summary = "Продлить испытательный срок на 30 дней", description = "Продлевает испытательный срок на 30 дней")
    public TrialPeriod extendTrialPeriod30Days(@PathVariable Long id) {
        return trialPeriodService.extendTrialPeriod30Days(id);
    }

    /**
     * Отмечает испытательный срок как не пройденный
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @PatchMapping("/{id}/fail")
    @Operation(summary = "Отметить испытательный срок как не пройденный", description = "Отмечает, что испытательный срок не пройден")
    public TrialPeriod failTrialPeriod(@PathVariable Long id) {
        return trialPeriodService.failTrialPeriod(id);
    }

    /**
     * Обновляет данные испытательного срока
     *
     * @param id идентификатор испытательного срока
     * @param trialPeriod новые данные испытательного срока
     * @return обновленные данные испытательного срока
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить испытательный срок", description = "Обновляет данные существующего испытательного срока")
    public TrialPeriod updateTrialPeriod(@PathVariable Long id, @RequestBody TrialPeriod trialPeriod) {
        return trialPeriodService.updateTrialPeriod(id, trialPeriod);
    }

    /**
     * Удаляет испытательный срок
     *
     * @param id идентификатор испытательного срока
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить испытательный срок", description = "Удаляет испытательный срок из системы")
    public void deleteTrialPeriod(@PathVariable Long id) {
        trialPeriodService.deleteTrialPeriod(id);
    }
}

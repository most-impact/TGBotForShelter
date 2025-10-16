package pro.dev.TGBotForShelter.controller;

import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.service.AdopterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления усыновителями
 */
@RestController
@RequestMapping("/api/adopters")
@RequiredArgsConstructor
@Tag(name = "Adopters", description = "API для управления усыновителями животных")
public class AdopterController {

    private final AdopterService adopterService;


    /**
     * Создает нового усыновителя
     *
     * @param adopter данные усыновителя
     * @return идентификатор созданного усыновителя
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать усыновителя", description = "Регистрирует нового усыновителя в системе")
    public Long createAdopter(@RequestBody Adopter adopter) {
        return adopterService.createAdopter(adopter);
    }

    /**
     * Получает усыновителя по идентификатору
     *
     * @param id идентификатор усыновителя
     * @return данные усыновителя
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить усыновителя по ID", description = "Возвращает данные усыновителя по идентификатору")
    public Adopter getAdopter(@PathVariable Long id) {
        return adopterService.getAdopter(id);
    }

    /**
     * Получает усыновителя по ID пользователя
     *
     * @param userId идентификатор пользователя
     * @return данные усыновителя
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить усыновителя по ID пользователя", description = "Возвращает данные усыновителя по идентификатору пользователя")
    public Adopter getAdopterByUserId(@PathVariable Long userId) {
        return adopterService.getAdopterByUserId(userId);
    }

    /**
     * Получает всех усыновителей
     *
     * @param shelterId фильтр по приюту (опционально)
     * @return список усыновителей
     */
    @GetMapping
    @Operation(summary = "Получить всех усыновителей", description = "Возвращает список всех усыновителей с возможностью фильтрации")
    public List<Adopter> getAllAdopters(@RequestParam(required = false) Long shelterId) {
        return adopterService.getAllAdopters(shelterId);
    }

    /**
     * Получает недавних усыновителей
     *
     * @param daysAgo количество дней назад
     * @return список недавних усыновителей
     */
    @GetMapping("/recent")
    @Operation(summary = "Получить недавних усыновителей", description = "Возвращает усыновителей за последние N дней")
    public List<Adopter> getRecentAdopters(@RequestParam(defaultValue = "30") int daysAgo) {
        return adopterService.getRecentAdopters(daysAgo);
    }

    /**
     * Обновляет данные усыновителя
     *
     * @param id идентификатор усыновителя
     * @param adopter новые данные усыновителя
     * @return обновленные данные усыновителя
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить усыновителя", description = "Обновляет данные существующего усыновителя")
    public Adopter updateAdopter(@PathVariable Long id, @RequestBody Adopter adopter) {
        return adopterService.updateAdopter(id, adopter);
    }

    /**
     * Удаляет усыновителя
     *
     * @param id идентификатор усыновителя
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить усыновителя", description = "Удаляет усыновителя из системы")
    public void deleteAdopter(@PathVariable Long id) {
        adopterService.deleteAdopter(id);
    }
}


package pro.dev.TGBotForShelter.controller;

import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.ShelterType;
import pro.dev.TGBotForShelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления приютами
 */
@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
@Tag(name = "Shelters", description = "API для управления приютами")
public class ShelterController {

    private final ShelterService shelterService;



    /**
     * Создает новый приют
     *
     * @param shelter данные приюта
     * @return идентификатор созданного приюта
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать приют", description = "Регистрирует новый приют в системе")
    public Long createShelter(@RequestBody Shelter shelter) {
        return shelterService.createShelter(shelter);
    }

    /**
     * Получает приют по идентификатору
     *
     * @param id идентификатор приюта
     * @return данные приюта
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить приют по ID", description = "Возвращает данные приюта по идентификатору")
    public Shelter getShelter(@PathVariable Long id) {
        return shelterService.getShelter(id);
    }

    /**
     * Получает все приюты
     *
     * @param type фильтр по типу приюта (опционально)
     * @return список приютов
     */
    @GetMapping
    @Operation(summary = "Получить все приюты", description = "Возвращает список всех приютов с возможностью фильтрации по типу")
    public List<Shelter> getAllShelters(@RequestParam(required = false) ShelterType type) {
        return shelterService.getAllShelters(type);
    }

    /**
     * Обновляет данные приюта
     *
     * @param id идентификатор приюта
     * @param shelter новые данные приюта
     * @return обновленные данные приюта
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить приют", description = "Обновляет данные существующего приюта")
    public Shelter updateShelter(@PathVariable Long id, @RequestBody Shelter shelter) {
        return shelterService.updateShelter(id, shelter);
    }

    /**
     * Удаляет приют
     *
     * @param id идентификатор приюта
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить приют", description = "Удаляет приют из системы")
    public void deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
    }
}

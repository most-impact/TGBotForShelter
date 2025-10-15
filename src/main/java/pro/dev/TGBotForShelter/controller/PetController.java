package pro.dev.TGBotForShelter.controller;

import pro.dev.TGBotForShelter.model.Pet;
import pro.dev.TGBotForShelter.model.ShelterType;
import pro.dev.TGBotForShelter.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления питомцами
 */
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "API для управления питомцами")
public class PetController {

    private final PetService petService;

    /**
     * Создает нового питомца
     *
     * @param pet данные питомца
     * @return идентификатор созданного питомца
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать питомца", description = "Регистрирует нового питомца в системе")
    public Long createPet(@RequestBody Pet pet) {
        return petService.createPet(pet);
    }

    /**
     * Получает питомца по идентификатору
     *
     * @param id идентификатор питомца
     * @return данные питомца
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить питомца по ID", description = "Возвращает данные питомца по идентификатору")
    public Pet getPet(@PathVariable Long id) {
        return petService.getPet(id);
    }

    /**
     * Получает всех питомцев
     *
     * @param type фильтр по типу животного (опционально)
     * @param hasDisabilities фильтр по наличию ограничений (опционально)
     * @return список питомцев
     */
    @GetMapping
    @Operation(summary = "Получить всех питомцев", description = "Возвращает список всех питомцев с возможностью фильтрации")
    public List<Pet> getAllPets(
            @RequestParam(required = false) ShelterType type,
            @RequestParam(required = false) Boolean hasDisabilities) {
        return petService.getAllPets(type, hasDisabilities);
    }

    /**
     * Ищет питомцев по кличке
     *
     * @param name кличка для поиска
     * @return список найденных питомцев
     */
    @GetMapping("/search")
    @Operation(summary = "Поиск питомцев по кличке", description = "Ищет питомцев по части клички")
    public List<Pet> searchPetsByName(@RequestParam String name) {
        return petService.searchPetsByName(name);
    }

    /**
     * Получает молодых питомцев
     *
     * @param type тип животного
     * @param maxAge максимальный возраст
     * @return список молодых питомцев
     */
    @GetMapping("/young")
    @Operation(summary = "Получить молодых питомцев", description = "Возвращает питомцев младше указанного возраста")
    public List<Pet> getYoungPets(
            @RequestParam ShelterType type,
            @RequestParam Integer maxAge) {
        return petService.getYoungPets(type, maxAge);
    }

    /**
     * Обновляет данные питомца
     *
     * @param id идентификатор питомца
     * @param pet новые данные питомца
     * @return обновленные данные питомца
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить питомца", description = "Обновляет данные существующего питомца")
    public Pet updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        return petService.updatePet(id, pet);
    }

    /**
     * Удаляет питомца
     *
     * @param id идентификатор питомца
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить питомца", description = "Удаляет питомца из системы")
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }
}

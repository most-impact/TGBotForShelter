package pro.dev.TGBotForShelter.service;


import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.ShelterType;
import pro.dev.TGBotForShelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с приютами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShelterService {

    private final ShelterRepository shelterRepository;


    /**
     * Создает новый приют
     *
     * @param shelter данные приюта
     * @return идентификатор созданного приюта
     */
    @Transactional
    public Long createShelter(Shelter shelter) {
        if (shelterRepository.existsByType(shelter.getType())) {
            throw new IllegalArgumentException("Приют с типом " + shelter.getType() + " уже существует");
        }
        Shelter savedShelter = shelterRepository.save(shelter);
        return savedShelter.getId();
    }

    /**
     * Получает приют по идентификатору
     *
     * @param id идентификатор приюта
     * @return данные приюта
     */
    public Shelter getShelter(Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Приют с id " + id + " не найден"));
    }

    /**
     * Получает приют по типу
     *
     * @param type тип приюта
     * @return данные приюта
     */
    public Shelter getShelterByType(ShelterType type) {
        return shelterRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("Приют типа " + type + " не найден"));
    }

    /**
     * Получает все приюты
     *
     * @param type фильтр по типу (опционально)
     * @return список приютов
     */
    public List<Shelter> getAllShelters(ShelterType type) {
        if (type != null) {
            return shelterRepository.findAllByType(type);
        }
        return shelterRepository.findAll();
    }

    /**
     * Обновляет данные приюта
     *
     * @param id идентификатор приюта
     * @param shelter новые данные приюта
     * @return обновленные данные приюта
     */
    @Transactional
    public Shelter updateShelter(Long id, Shelter shelter) {
        Shelter existingShelter = getShelter(id);

        if (shelter.getName() != null) {
            existingShelter.setName(shelter.getName());
        }
        if (shelter.getAddress() != null) {
            existingShelter.setAddress(shelter.getAddress());
        }
        if (shelter.getSchedule() != null) {
            existingShelter.setSchedule(shelter.getSchedule());
        }
        if (shelter.getSecurityContact() != null) {
            existingShelter.setSecurityContact(shelter.getSecurityContact());
        }
        if (shelter.getSafetyRules() != null) {
            existingShelter.setSafetyRules(shelter.getSafetyRules());
        }
        if (shelter.getInformation() != null) {
            existingShelter.setInformation(shelter.getInformation());
        }

        return shelterRepository.save(existingShelter);
    }

    /**
     * Удаляет приют
     *
     * @param id идентификатор приюта
     */
    @Transactional
    public void deleteShelter(Long id) {
        if (!shelterRepository.existsById(id)) {
            throw new IllegalArgumentException("Приют с id " + id + " не найден");
        }
        shelterRepository.deleteById(id);
    }
}
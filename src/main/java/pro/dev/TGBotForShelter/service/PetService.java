package pro.dev.TGBotForShelter.service;


import pro.dev.TGBotForShelter.model.Pet;
import pro.dev.TGBotForShelter.model.ShelterType;
import pro.dev.TGBotForShelter.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с питомцами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final PetRepository petRepository;



    /**
     * Создает нового питомца
     *
     * @param pet данные питомца
     * @return идентификатор созданного питомца
     */
    @Transactional
    public Long createPet(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        return savedPet.getId();
    }

    /**
     * Получает питомца по идентификатору
     *
     * @param id идентификатор питомца
     * @return данные питомца
     */
    public Pet getPet(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Питомец с id " + id + " не найден"));
    }

    /**
     * Получает всех питомцев
     *
     * @param type фильтр по типу (опционально)
     * @param hasDisabilities фильтр по наличию ограничений (опционально)
     * @return список питомцев
     */
    public List<Pet> getAllPets(ShelterType type, Boolean hasDisabilities) {
        if (type != null && hasDisabilities != null) {
            return petRepository.findAllByTypeAndHasDisabilities(type, hasDisabilities);
        }
        if (type != null) {
            return petRepository.findAllByType(type);
        }
        if (hasDisabilities != null) {
            return petRepository.findAllByHasDisabilities(hasDisabilities);
        }
        return petRepository.findAll();
    }

    /**
     * Получает питомцев по породе
     *
     * @param breed порода
     * @return список питомцев
     */
    public List<Pet> getPetsByBreed(String breed) {
        return petRepository.findAllByBreed(breed);
    }

    /**
     * Ищет питомцев по кличке
     *
     * @param name кличка (может быть частичное совпадение)
     * @return список питомцев
     */
    public List<Pet> searchPetsByName(String name) {
        return petRepository.findAllByNameContainingIgnoreCase(name);
    }

    /**
     * Получает молодых питомцев по типу
     *
     * @param type тип животного
     * @param maxAge максимальный возраст
     * @return список питомцев
     */
    public List<Pet> getYoungPets(ShelterType type, Integer maxAge) {
        return petRepository.findAllByTypeAndAgeLessThan(type, maxAge);
    }

    /**
     * Обновляет данные питомца
     *
     * @param id идентификатор питомца
     * @param pet новые данные питомца
     * @return обновленные данные питомца
     */
    @Transactional
    public Pet updatePet(Long id, Pet pet) {
        Pet existingPet = getPet(id);

        if (pet.getName() != null) {
            existingPet.setName(pet.getName());
        }
        if (pet.getBreed() != null) {
            existingPet.setBreed(pet.getBreed());
        }
        if (pet.getAge() != null) {
            existingPet.setAge(pet.getAge());
        }
        if (pet.getDescription() != null) {
            existingPet.setDescription(pet.getDescription());
        }

        return petRepository.save(existingPet);
    }

    /**
     * Удаляет питомца
     *
     * @param id идентификатор питомца
     */
    @Transactional
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new IllegalArgumentException("Питомец с id " + id + " не найден");
        }
        petRepository.deleteById(id);
    }
}

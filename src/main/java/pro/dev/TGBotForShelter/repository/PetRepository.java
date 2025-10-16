package pro.dev.TGBotForShelter.repository;


import pro.dev.TGBotForShelter.model.Pet;
import pro.dev.TGBotForShelter.model.ShelterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с питомцами
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    /**
     * Находит всех питомцев по типу
     *
     * @param type тип животного
     * @return список питомцев
     */
    List<Pet> findAllByType(ShelterType type);

    /**
     * Находит всех питомцев по породе
     *
     * @param breed порода
     * @return список питомцев
     */
    List<Pet> findAllByBreed(String breed);

    /**
     * Находит питомцев по кличке (содержит подстроку)
     *
     * @param name кличка
     * @return список питомцев
     */
    List<Pet> findAllByNameContainingIgnoreCase(String name);

    /**
     * Находит всех питомцев с ограниченными возможностями
     *
     * @param hasDisabilities флаг наличия ограничений
     * @return список питомцев
     */
    List<Pet> findAllByHasDisabilities(boolean hasDisabilities);

    /**
     * Находит питомцев по типу и возрасту меньше указанного
     *
     * @param type тип животного
     * @param age возраст
     * @return список питомцев
     */
    List<Pet> findAllByTypeAndAgeLessThan(ShelterType type, Integer age);

    /**
     * Находит питомцев по типу и наличию ограничений
     *
     * @param type тип животного
     * @param hasDisabilities флаг наличия ограничений
     * @return список питомцев
     */
    List<Pet> findAllByTypeAndHasDisabilities(ShelterType type, boolean hasDisabilities);
}
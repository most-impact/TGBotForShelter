package pro.dev.TGBotForShelter.repository;

import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.ShelterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с приютами
 */
@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    /**
     * Находит приют по типу
     *
     * @param type тип приюта
     * @return приют, если найден
     */
    Optional<Shelter> findByType(ShelterType type);

    /**
     * Находит все приюты по типу
     *
     * @param type тип приюта
     * @return список приютов
     */
    List<Shelter> findAllByType(ShelterType type);

    /**
     * Проверяет существование приюта по типу
     *
     * @param type тип приюта
     * @return true, если приют существует
     */
    boolean existsByType(ShelterType type);
}
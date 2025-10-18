package pro.dev.TGBotForShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с усыновителями
 */
@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    /**
     * Проверяет существование усыновителя для пользователя
     *
     * @param user пользователь
     * @return true, если усыновитель существует
     */
    @Query("SELECT COUNT(a) > 0 FROM Adopter a WHERE a.user = :user")
    boolean existsByUser(@Param("user") User user);

    /**
     * Находит усыновителя по ID пользователя
     *
     * @param userId ID пользователя
     * @return усыновитель или пустой Optional
     */
    @Query("SELECT a FROM Adopter a WHERE a.user.id = :userId")
    Optional<Adopter> findByUserId(@Param("userId") Long userId);

    /**
     * Находит всех усыновителей по ID приюта
     *
     * @param shelterId ID приюта
     * @return список усыновителей
     */
    List<Adopter> findAllByShelterId(Long shelterId);

    /**
     * Находит всех усыновителей по ID питомца
     *
     * @param petId ID питомца
     * @return список усыновителей
     */
    List<Adopter> findAllByPetId(Long petId);

    /**
     * Находит усыновителей, усыновивших после указанной даты
     *
     * @param date дата
     * @return список усыновителей
     */
    List<Adopter> findAllByAdoptionDateAfter(LocalDateTime date);

    /**
     * Находит усыновителей по приюту
     *
     * @param shelter приют
     * @return список усыновителей
     */
    List<Adopter> findByShelter(Shelter shelter);

    /**
     * Находит всех активных усыновителей (с усыновлением)
     *
     * @return список усыновителей
     */
    List<Adopter> findByAdoptionDateIsNotNull();

    /**
     * Находит усыновителей за период
     *
     * @param startDate начальная дата
     * @param endDate конечная дата
     * @return список усыновителей
     */
    List<Adopter> findAllByAdoptionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Проверяет существование усыновителя по ID
     *
     * @param id ID усыновителя
     * @return true, если усыновитель существует
     */
    boolean existsById(Long id);
}

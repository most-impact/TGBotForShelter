package pro.dev.TGBotForShelter.repository;


import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с усыновителями
 */
@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    /**
     * Находит усыновителя по пользователю
     *
     * @param user пользователь
     * @return усыновитель, если найден
     */
    Optional<Adopter> findByUser(User user);

    /**
     * Находит усыновителя по ID пользователя
     *
     * @param userId идентификатор пользователя
     * @return усыновитель, если найден
     */
    Optional<Adopter> findByUserId(Long userId);

    /**
     * Находит всех усыновителей по приюту
     *
     * @param shelter приют
     * @return список усыновителей
     */
    List<Adopter> findAllByShelter(Shelter shelter);

    /**
     * Находит всех усыновителей по ID приюта
     *
     * @param shelterId идентификатор приюта
     * @return список усыновителей
     */
    List<Adopter> findAllByShelterId(Long shelterId);

    /**
     * Находит усыновителей по дате усыновления после указанной даты
     *
     * @param date дата
     * @return список усыновителей
     */
    List<Adopter> findAllByAdoptionDateAfter(LocalDateTime date);

    /**
     * Находит усыновителей по дате усыновления в промежутке
     *
     * @param startDate начальная дата
     * @param endDate конечная дата
     * @return список усыновителей
     */
    List<Adopter> findAllByAdoptionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Проверяет существование усыновителя по пользователю
     *
     * @param user пользователь
     * @return true, если усыновитель существует
     */
    boolean existsByUser(User user);
}
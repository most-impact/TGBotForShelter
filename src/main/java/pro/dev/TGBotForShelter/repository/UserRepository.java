package pro.dev.TGBotForShelter.repository;


import pro.dev.TGBotForShelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по Telegram ID
     *
     * @param telegramId идентификатор пользователя в Telegram
     * @return пользователь, если найден
     */
    Optional<User> findByTelegramId(Long telegramId);

    /**
     * Находит пользователя по username
     *
     * @param username имя пользователя в Telegram
     * @return пользователь, если найден
     */
    Optional<User> findByUsername(String username);

    /**
     * Находит пользователя по номеру телефона
     *
     * @param phoneNumber номер телефона
     * @return пользователь, если найден
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Находит всех волонтеров
     *
     * @param isVolunteer флаг волонтера
     * @return список волонтеров
     */
    List<User> findAllByIsVolunteer(boolean isVolunteer);

    /**
     * Проверяет существование пользователя по Telegram ID
     *
     * @param telegramId идентификатор пользователя в Telegram
     * @return true, если пользователь существует
     */
    boolean existsByTelegramId(Long telegramId);
}
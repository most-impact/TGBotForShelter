package pro.dev.TGBotForShelter.service;


import pro.dev.TGBotForShelter.model.User;
import pro.dev.TGBotForShelter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;



    /**
     * Создает нового пользователя
     *
     * @param user данные пользователя
     * @return идентификатор созданного пользователя
     */
    @Transactional
    public Long createUser(User user) {
        if (userRepository.existsByTelegramId(user.getTelegramId())) {
            throw new IllegalArgumentException("Пользователь с Telegram ID " + user.getTelegramId() + " уже существует");
        }

        user.setRegisteredAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    /**
     * Получает пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     * @return данные пользователя
     */
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с id " + id + " не найден"));
    }

    /**
     * Получает пользователя по Telegram ID
     *
     * @param telegramId идентификатор в Telegram
     * @return данные пользователя
     */
    public User getUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с Telegram ID " + telegramId + " не найден"));
    }

    /**
     * Получает всех пользователей
     *
     * @param isVolunteer фильтр по статусу волонтера (опционально)
     * @return список пользователей
     */
    public List<User> getAllUsers(Boolean isVolunteer) {
        if (isVolunteer != null) {
            return userRepository.findAllByIsVolunteer(isVolunteer);
        }
        return userRepository.findAll();
    }

    /**
     * Получает всех волонтеров
     *
     * @return список волонтеров
     */
    public List<User> getVolunteers() {
        return userRepository.findAllByIsVolunteer(true);
    }

    /**
     * Обновляет данные пользователя
     *
     * @param id идентификатор пользователя
     * @param user новые данные пользователя
     * @return обновленные данные пользователя
     */
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = getUser(id);

        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(user.getPhoneNumber());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        return userRepository.save(existingUser);
    }

    /**
     * Устанавливает статус волонтера для пользователя
     *
     * @param id идентификатор пользователя
     * @param isVolunteer статус волонтера
     * @return обновленные данные пользователя
     */
    @Transactional
    public User setVolunteerStatus(Long id, boolean isVolunteer) {
        User user = getUser(id);
        user.setVolunteer(isVolunteer);
        return userRepository.save(user);
    }

    /**
     * Удаляет пользователя
     *
     * @param id идентификатор пользователя
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}
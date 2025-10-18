package pro.dev.TGBotForShelter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.model.Pet;
import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.User;
import pro.dev.TGBotForShelter.repository.AdopterRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с усыновителями
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdopterService {

    private final AdopterRepository adopterRepository;
    private final UserService userService;
    private final ShelterService shelterService;
    private final PetService petService;

    /**
     * Создает нового усыновителя
     *
     * @param adopter данные усыновителя
     * @return идентификатор созданного усыновителя
     */
    @Transactional
    public Long createAdopter(Adopter adopter) {
        // Получаем пользователя из сервиса
        User user = userService.getUser(adopter.getUser().getId());

        // ✅ Исправленный вызов: используем правильный метод existsByUser
        if (adopterRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Пользователь с id " + user.getId() + " уже является усыновителем");
        }

        // Получаем приют
        Shelter shelter = shelterService.getShelter(adopter.getShelter().getId());

        // Получаем питомца
        Pet pet = petService.getPet(adopter.getPet().getId());

        // Создаем новый объект adopter с правильными связями
        Adopter newAdopter = new Adopter();
        newAdopter.setUser(user);
        newAdopter.setShelter(shelter);
        newAdopter.setPet(pet);
        newAdopter.setAdoptionDate(LocalDateTime.now());

        // Сохраняем
        Adopter savedAdopter = adopterRepository.save(newAdopter);
        return savedAdopter.getId();
    }

    /**
     * Получает усыновителя по идентификатору
     *
     * @param id идентификатор усыновителя
     * @return данные усыновителя
     */
    public Adopter getAdopter(Long id) {
        Optional<Adopter> adopter = adopterRepository.findById(id);
        if (adopter.isEmpty()) {
            throw new IllegalArgumentException("Усыновитель с id " + id + " не найден");
        }
        return adopter.get();
    }

    /**
     * Получает усыновителя по ID пользователя
     *
     * @param userId идентификатор пользователя
     * @return данные усыновителя
     */
    public Adopter getAdopterByUserId(Long userId) {
        // ✅ Исправленный вызов: используем правильный метод findByUserId
        Optional<Adopter> adopter = adopterRepository.findByUserId(userId);
        if (adopter.isEmpty()) {
            throw new IllegalArgumentException("Усыновитель для пользователя с id " + userId + " не найден");
        }
        return adopter.get();
    }

    /**
     * Получает всех усыновителей
     *
     * @param shelterId фильтр по приюту (опционально)
     * @return список усыновителей
     */
    public List<Adopter> getAllAdopters(Long shelterId) {
        if (shelterId != null) {
            // ✅ Исправленный вызов: используем правильный метод
            return adopterRepository.findAllByShelterId(shelterId);
        }
        return adopterRepository.findAll();
    }

    /**
     * Получает недавних усыновителей
     *
     * @param daysAgo количество дней назад
     * @return список усыновителей
     */
    public List<Adopter> getRecentAdopters(int daysAgo) {
        LocalDateTime dateThreshold = LocalDateTime.now().minusDays(daysAgo);
        // ✅ Исправленный вызов: используем правильный метод
        return adopterRepository.findAllByAdoptionDateAfter(dateThreshold);
    }

    /**
     * Обновляет данные усыновителя
     *
     * @param id идентификатор усыновителя
     * @param adopter новые данные усыновителя
     * @return обновленные данные усыновителя
     */
    @Transactional
    public Adopter updateAdopter(Long id, Adopter adopter) {
        Adopter existingAdopter = getAdopter(id);

        // Обновляем питомца, если передан
        if (adopter.getPet() != null && adopter.getPet().getId() != null) {
            Pet pet = petService.getPet(adopter.getPet().getId());
            existingAdopter.setPet(pet);
        }

        // TODO: добавить обновление других полей при необходимости

        return adopterRepository.save(existingAdopter);
    }

    /**
     * Удаляет усыновителя
     *
     * @param id идентификатор усыновителя
     */
    @Transactional
    public void deleteAdopter(Long id) {
        if (!adopterRepository.existsById(id)) {
            throw new IllegalArgumentException("Усыновитель с id " + id + " не найден");
        }
        adopterRepository.deleteById(id);
    }
}

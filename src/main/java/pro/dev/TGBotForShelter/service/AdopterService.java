package pro.dev.TGBotForShelter.service;


import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.model.Pet;
import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.User;
import pro.dev.TGBotForShelter.repository.AdopterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        User user = userService.getUser(adopter.getUser().getId());

        if (adopterRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Пользователь с id " + user.getId() + " уже является усыновителем");
        }

        Shelter shelter = shelterService.getShelter(adopter.getShelter().getId());
        Pet pet = petService.getPet(adopter.getPet().getId());

        adopter.setUser(user);
        adopter.setShelter(shelter);
        adopter.setPet(pet);
        adopter.setAdoptionDate(LocalDateTime.now());

        Adopter savedAdopter = adopterRepository.save(adopter);
        return savedAdopter.getId();
    }

    /**
     * Получает усыновителя по идентификатору
     *
     * @param id идентификатор усыновителя
     * @return данные усыновителя
     */
    public Adopter getAdopter(Long id) {
        return adopterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Усыновитель с id " + id + " не найден"));
    }

    /**
     * Получает усыновителя по ID пользователя
     *
     * @param userId идентификатор пользователя
     * @return данные усыновителя
     */
    public Adopter getAdopterByUserId(Long userId) {
        return adopterRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Усыновитель для пользователя с id " + userId + " не найден"));
    }

    /**
     * Получает всех усыновителей
     *
     * @param shelterId фильтр по приюту (опционально)
     * @return список усыновителей
     */
    public List<Adopter> getAllAdopters(Long shelterId) {
        if (shelterId != null) {
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

        if (adopter.getPet() != null && adopter.getPet().getId() != null) {
            Pet pet = petService.getPet(adopter.getPet().getId());
            existingAdopter.setPet(pet);
        }

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
package pro.dev.TGBotForShelter.service;


import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.model.TrialPeriod;
import pro.dev.TGBotForShelter.model.TrialPeriodStatus;
import pro.dev.TGBotForShelter.repository.TrialPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для работы с испытательными сроками
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrialPeriodService {

    private final TrialPeriodRepository trialPeriodRepository;
    private final AdopterService adopterService;

    private static final int DEFAULT_TRIAL_DAYS = 30;
    private static final int EXTENSION_14_DAYS = 14;
    private static final int EXTENSION_30_DAYS = 30;
    private static final int MISSED_REPORTS_THRESHOLD = 2;



    /**
     * Создает новый испытательный срок
     *
     * @param trialPeriod данные испытательного срока
     * @return идентификатор созданного испытательного срока
     */
    @Transactional
    public Long createTrialPeriod(TrialPeriod trialPeriod) {
        Adopter adopter = adopterService.getAdopter(trialPeriod.getAdopter().getId());

        if (trialPeriodRepository.findByAdopter(adopter).isPresent()) {
            throw new IllegalArgumentException("Испытательный срок для усыновителя с id " + adopter.getId() + " уже существует");
        }

        trialPeriod.setAdopter(adopter);
        trialPeriod.setStartDate(LocalDate.now());
        trialPeriod.setEndDate(LocalDate.now().plusDays(DEFAULT_TRIAL_DAYS));
        trialPeriod.setStatus(TrialPeriodStatus.ACTIVE);

        TrialPeriod savedTrialPeriod = trialPeriodRepository.save(trialPeriod);
        return savedTrialPeriod.getId();
    }

    /**
     * Получает испытательный срок по идентификатору
     *
     * @param id идентификатор испытательного срока
     * @return данные испытательного срока
     */
    public TrialPeriod getTrialPeriod(Long id) {
        return trialPeriodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Испытательный срок с id " + id + " не найден"));
    }

    /**
     * Получает испытательный срок по ID усыновителя
     *
     * @param adopterId идентификатор усыновителя
     * @return данные испытательного срока
     */
    public TrialPeriod getTrialPeriodByAdopterId(Long adopterId) {
        return trialPeriodRepository.findByAdopterId(adopterId)
                .orElseThrow(() -> new IllegalArgumentException("Испытательный срок для усыновителя с id " + adopterId + " не найден"));
    }

    /**
     * Получает все испытательные сроки
     *
     * @param status фильтр по статусу (опционально)
     * @param shelterId фильтр по приюту (опционально)
     * @return список испытательных сроков
     */
    public List<TrialPeriod> getAllTrialPeriods(TrialPeriodStatus status, Long shelterId) {
        if (status != null && shelterId != null) {
            return trialPeriodRepository.findAllByShelterId(shelterId).stream()
                    .filter(tp -> tp.getStatus() == status)
                    .toList();
        }
        if (status != null) {
            return trialPeriodRepository.findAllByStatus(status);
        }
        if (shelterId != null) {
            return trialPeriodRepository.findAllByShelterId(shelterId);
        }
        return trialPeriodRepository.findAll();
    }

    /**
     * Получает активные испытательные сроки
     *
     * @return список активных испытательных сроков
     */
    public List<TrialPeriod> getActiveTrialPeriods() {
        return trialPeriodRepository.findAllActiveTrialPeriods(LocalDate.now());
    }

    /**
     * Получает испытательные сроки, требующие внимания волонтера
     *
     * @return список испытательных сроков
     */
    public List<TrialPeriod> getTrialPeriodsRequiringAttention() {
        return trialPeriodRepository.findTrialPeriodsRequiringAttention(
                MISSED_REPORTS_THRESHOLD,
                LocalDate.now()
        );
    }

    /**
     * Увеличивает счетчик пропущенных отчетов
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @Transactional
    public TrialPeriod incrementMissedReports(Long id) {
        TrialPeriod trialPeriod = getTrialPeriod(id);
        trialPeriod.setMissedReports(trialPeriod.getMissedReports() + 1);
        return trialPeriodRepository.save(trialPeriod);
    }

    /**
     * Сбрасывает счетчик пропущенных отчетов
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @Transactional
    public TrialPeriod resetMissedReports(Long id) {
        TrialPeriod trialPeriod = getTrialPeriod(id);
        trialPeriod.setMissedReports(0);
        return trialPeriodRepository.save(trialPeriod);
    }

    /**
     * Завершает испытательный срок успешно
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @Transactional
    public TrialPeriod passTrialPeriod(Long id) {
        TrialPeriod trialPeriod = getTrialPeriod(id);

        if (trialPeriod.getStatus() != TrialPeriodStatus.ACTIVE) {
            throw new IllegalStateException("Испытательный срок должен быть активным");
        }

        trialPeriod.setStatus(TrialPeriodStatus.PASSED);
        return trialPeriodRepository.save(trialPeriod);
    }

    /**
     * Продлевает испытательный срок на 14 дней
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @Transactional
    public TrialPeriod extendTrialPeriod14Days(Long id) {
        TrialPeriod trialPeriod = getTrialPeriod(id);

        if (trialPeriod.getStatus() != TrialPeriodStatus.ACTIVE) {
            throw new IllegalStateException("Испытательный срок должен быть активным");
        }

        trialPeriod.setEndDate(trialPeriod.getEndDate().plusDays(EXTENSION_14_DAYS));
        trialPeriod.setStatus(TrialPeriodStatus.EXTENDED_14);
        return trialPeriodRepository.save(trialPeriod);
    }

    /**
     * Продлевает испытательный срок на 30 дней
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @Transactional
    public TrialPeriod extendTrialPeriod30Days(Long id) {
        TrialPeriod trialPeriod = getTrialPeriod(id);

        if (trialPeriod.getStatus() != TrialPeriodStatus.ACTIVE) {
            throw new IllegalStateException("Испытательный срок должен быть активным");
        }

        trialPeriod.setEndDate(trialPeriod.getEndDate().plusDays(EXTENSION_30_DAYS));
        trialPeriod.setStatus(TrialPeriodStatus.EXTENDED_30);
        return trialPeriodRepository.save(trialPeriod);
    }

    /**
     * Отмечает испытательный срок как не пройденный
     *
     * @param id идентификатор испытательного срока
     * @return обновленные данные испытательного срока
     */
    @Transactional
    public TrialPeriod failTrialPeriod(Long id) {
        TrialPeriod trialPeriod = getTrialPeriod(id);

        if (trialPeriod.getStatus() != TrialPeriodStatus.ACTIVE) {
            throw new IllegalStateException("Испытательный срок должен быть активным");
        }

        trialPeriod.setStatus(TrialPeriodStatus.FAILED);
        return trialPeriodRepository.save(trialPeriod);
    }

    /**
     * Обновляет данные испытательного срока
     *
     * @param id идентификатор испытательного срока
     * @param trialPeriod новые данные испытательного срока
     * @return обновленные данные испытательного срока
     */
    @Transactional
    public TrialPeriod updateTrialPeriod(Long id, TrialPeriod trialPeriod) {
        TrialPeriod existingTrialPeriod = getTrialPeriod(id);

        if (trialPeriod.getEndDate() != null) {
            existingTrialPeriod.setEndDate(trialPeriod.getEndDate());
        }
        if (trialPeriod.getStatus() != null) {
            existingTrialPeriod.setStatus(trialPeriod.getStatus());
        }

        return trialPeriodRepository.save(existingTrialPeriod);
    }

    /**
     * Удаляет испытательный срок
     *
     * @param id идентификатор испытательного срока
     */
    @Transactional
    public void deleteTrialPeriod(Long id) {
        if (!trialPeriodRepository.existsById(id)) {
            throw new IllegalArgumentException("Испытательный срок с id " + id + " не найден");
        }
        trialPeriodRepository.deleteById(id);
    }
}
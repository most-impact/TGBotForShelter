package pro.dev.TGBotForShelter.controller;

import pro.dev.TGBotForShelter.model.User;
import pro.dev.TGBotForShelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    /**
     * Создает нового пользователя
     *
     * @param user данные пользователя
     * @return идентификатор созданного пользователя
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя", description = "Регистрирует нового пользователя в системе")
    public Long createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Получает пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     * @return данные пользователя
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по идентификатору")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * Получает пользователя по Telegram ID
     *
     * @param telegramId идентификатор в Telegram
     * @return данные пользователя
     */
    @GetMapping("/telegram/{telegramId}")
    @Operation(summary = "Получить пользователя по Telegram ID", description = "Возвращает данные пользователя по Telegram ID")
    public User getUserByTelegramId(@PathVariable Long telegramId) {
        return userService.getUserByTelegramId(telegramId);
    }

    /**
     * Получает всех пользователей
     *
     * @param isVolunteer фильтр по статусу волонтера (опционально)
     * @return список пользователей
     */
    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей с возможностью фильтрации")
    public List<User> getAllUsers(@RequestParam(required = false) Boolean isVolunteer) {
        return userService.getAllUsers(isVolunteer);
    }

    /**
     * Получает всех волонтеров
     *
     * @return список волонтеров
     */
    @GetMapping("/volunteers")
    @Operation(summary = "Получить всех волонтеров", description = "Возвращает список всех волонтеров")
    public List<User> getVolunteers() {
        return userService.getVolunteers();
    }

    /**
     * Обновляет данные пользователя
     *
     * @param id идентификатор пользователя
     * @param user новые данные пользователя
     * @return обновленные данные пользователя
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные существующего пользователя")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    /**
     * Устанавливает статус волонтера
     *
     * @param id идентификатор пользователя
     * @param isVolunteer статус волонтера
     * @return обновленные данные пользователя
     */
    @PatchMapping("/{id}/volunteer")
    @Operation(summary = "Установить статус волонтера", description = "Устанавливает или снимает статус волонтера")
    public User setVolunteerStatus(@PathVariable Long id, @RequestParam boolean isVolunteer) {
        return userService.setVolunteerStatus(id, isVolunteer);
    }

    /**
     * Удаляет пользователя
     *
     * @param id идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

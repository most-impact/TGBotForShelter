package pro.dev.TGBotForShelter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import pro.dev.TGBotForShelter.config.BotProperties;
import pro.dev.TGBotForShelter.model.Commands;
import pro.dev.TGBotForShelter.model.Stage;
import pro.dev.TGBotForShelter.model.UserSession;
import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.ShelterType;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Основной бот с поэтапной логикой:
 * - новый пользователь: приветствие + главное меню
 * - "возвращенец": при /start сначала выбрать приют
 * - этапы: 1) инфо о приюте, 2) как взять животное, 3) прислать отчёт, + позвать волонтёра
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotProperties props;
    private final UserSessionService userSessionService;   // in-memory сессии по telegramId
    private final ShelterService shelterService;           // работа с приютами (БД)

    public TelegramBot(BotProperties props,
                       UserSessionService userSessionService,
                       ShelterService shelterService) {
        this.props = props;
        this.userSessionService = userSessionService;
        this.shelterService = shelterService;
    }

    @Override
    public String getBotUsername() {
        return props.username();
    }

    @Override
    public String getBotToken() {
        return props.token();
    }

    @PostConstruct
    void initMenu() {
        registerBotMenu();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        final String text      = update.getMessage().getText();
        final long   chatId    = update.getMessage().getChatId();
        final long   telegramId= update.getMessage().getFrom().getId(); // важно: это НЕ chatId
        final String firstName = update.getMessage().getChat().getFirstName();

        // получаем/создаём сессию пользователя по его telegramId
        UserSession session = userSessionService.getOrCreate(telegramId);
        session.touch();

        // стандартные команды
        if (Objects.equals(text, Commands.START)) {
            // если у пользователя уже был выбран приют ранее — попросим выбрать заново
            if (session.getCurrentShelter() != null) {
                session.setStage(Stage.CHOOSE_SHELTER);
                send(chatId, "С какого приюта начнём? Выберите кнопкой ниже:",
                        chooseShelterKeyboard(shelterService.getAllShelters(null)));
            } else {
                session.setStage(Stage.MAIN_MENU);
                send(chatId, welcomeMessage(firstName), mainMenuKeyboard());
            }
            return;
        }
        if (Objects.equals(text, Commands.HELP)) {
            send(chatId, """
                    Доступные команды:
/start — начать заново
/help — помощь

На клавиатуре — основные действия.
                    """, mainMenuKeyboard());
            return;
        }

        // маршрутизация по текущему этапу
        switch (session.getStage()) {
            case WELCOME, MAIN_MENU -> handleMainMenuInput(session, chatId, text, firstName);
            case CHOOSE_SHELTER     -> handleShelterChoice(session, chatId, text);
            case STAGE1_INFO        -> {
                send(chatId, "Этап 1. Информация о приюте…", mainMenuKeyboard());
                session.setStage(Stage.MAIN_MENU);
            }
            case STAGE2_ADOPT       -> {
                send(chatId, "Этап 2. Как взять животное…", mainMenuKeyboard());
                session.setStage(Stage.MAIN_MENU);
            }
            case STAGE3_REPORT      -> {
                send(chatId, "Этап 3. Пришлите отчёт о питомце…", mainMenuKeyboard());
                session.setStage(Stage.MAIN_MENU);
            }
            case CALL_VOLUNTEER     -> {
                send(chatId, "Передал ваш запрос волонтёрам. Свяжутся как можно скорее.", mainMenuKeyboard());
                session.setStage(Stage.MAIN_MENU);
            }
        }
    }

    /* ---------- Хендлеры ---------- */

    private void handleMainMenuInput(UserSession s, long chatId, String text, String firstName) {
        switch (text) {
            case "Узнать информацию о приюте" -> {
                s.setStage(Stage.STAGE1_INFO);
                send(chatId, "Этап 1. Информация о приюте…", mainMenuKeyboard());
            }
            case "Как взять животное" -> {
                s.setStage(Stage.STAGE2_ADOPT);
                send(chatId, "Этап 2. Как взять животное…", mainMenuKeyboard());
            }
            case "Прислать отчёт о питомце" -> {
                s.setStage(Stage.STAGE3_REPORT);
                send(chatId, "Этап 3. Пришлите отчёт о питомце…", mainMenuKeyboard());
            }
            case "Позвать волонтёра" -> {
                s.setStage(Stage.CALL_VOLUNTEER);
                send(chatId, "Передал ваш запрос волонтёрам. Свяжутся как можно скорее.", mainMenuKeyboard());
                s.setStage(Stage.MAIN_MENU);
            }
            // кнопки из первоначальной клавиатуры (оставляем совместимость)
            case "Start" -> {
                s.setStage(Stage.MAIN_MENU);
                send(chatId, welcomeMessage(firstName), mainMenuKeyboard());
            }
            case "Help" -> {
                send(chatId, """
                        Доступные команды:
/start — начать заново
/help — помощь

На клавиатуре — основные действия.
                        """, mainMenuKeyboard());
            }
            default -> send(chatId, "Не понял команду. Выберите пункт на клавиатуре.", mainMenuKeyboard());
        }
    }

    private void handleShelterChoice(UserSession s, long chatId, String text) {
        // без изменения твоего репозитория: ищем по имени из списка
        List<Shelter> all = shelterService.getAllShelters(null);
        Shelter chosen = all.stream()
                .filter(sh -> sh.getName() != null && sh.getName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);

        if (chosen == null) {
            send(chatId, "Пожалуйста, выберите приют кнопкой из списка.",
                    chooseShelterKeyboard(all));
            return;
        }

        s.setCurrentShelter(chosen);
        s.setStage(Stage.MAIN_MENU);
        send(chatId, "Вы выбрали: " + chosen.getName() + ". Что дальше?", mainMenuKeyboard());
    }

    /* ---------- Системные вещи ---------- */

    private void registerBotMenu() {
        List<BotCommand> commands = List.of(
                new BotCommand(Commands.START, "Начать"),
                new BotCommand(Commands.HELP,  "Помощь")
        );
        try {
            execute(new SetMyCommands(commands, null, null));
        } catch (TelegramApiException e) {
            System.err.println("Не удалось установить меню команд: " + e.getMessage());
        }
    }

    /* ---------- Клавиатуры ---------- */

    private ReplyKeyboardMarkup mainMenuKeyboard() {
        // Главное меню с этапами
        KeyboardRow r1 = new KeyboardRow();
        r1.add(new KeyboardButton("Узнать информацию о приюте"));
        r1.add(new KeyboardButton("Как взять животное"));

        KeyboardRow r2 = new KeyboardRow();
        r2.add(new KeyboardButton("Прислать отчёт о питомце"));
        r2.add(new KeyboardButton("Позвать волонтёра"));

        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(r1);
        rows.add(r2);

        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        kb.setKeyboard(rows);
        kb.setResizeKeyboard(true);
        kb.setOneTimeKeyboard(false);
        return kb;
    }

    private ReplyKeyboardMarkup chooseShelterKeyboard(List<Shelter> shelters) {
        // Клавиатура со списком приютов (по именам)
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow cur = new KeyboardRow();

        for (Shelter s : shelters) {
            if (cur.size() >= 2) { // по 2 кнопки в ряд
                rows.add(cur);
                cur = new KeyboardRow();
            }
            String label = s.getName();
            if (label == null || label.isBlank()) {
                label = (s.getType() == ShelterType.DOG) ? "Приют (собаки)" : "Приют (коты)";
            }
            cur.add(new KeyboardButton(label));
        }
        if (!cur.isEmpty()) rows.add(cur);

        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        kb.setKeyboard(rows);
        kb.setResizeKeyboard(true);
        kb.setOneTimeKeyboard(false);
        return kb;
    }

    /* ---------- Отправка сообщений ---------- */

    private void send(Long chatId, String text, ReplyKeyboardMarkup kb) {
        SendMessage sm = new SendMessage(chatId.toString(), text);
        sm.setReplyMarkup(kb);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    private String welcomeMessage(String firstName) {
        String name = (firstName != null && !firstName.isBlank()) ? firstName : "друг";
        return "Привет, " + name + "! Я помогаю взаимодействовать с приютами для собачек.\n" +
                "Выберите, что хотите сделать:";
    }
}

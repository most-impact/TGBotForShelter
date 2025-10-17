package pro.dev.TGBotForShelter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pro.dev.TGBotForShelter.config.BotProperties;
import pro.dev.TGBotForShelter.model.Commands;
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

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotProperties props;

    public TelegramBot(BotProperties props) {
        this.props = props;
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

        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        switch (text) {
            case Commands.START -> handleStart(chatId, update.getMessage().getChat().getFirstName());
            case Commands.HELP  -> handleHelp(chatId);
            case "Start" -> handleStart(chatId, update.getMessage().getChat().getFirstName());
            case "Help"  -> handleHelp(chatId);
            default      -> send(chatId, "Не понял команду. Нажмите Start или Help 🙂", withMainKeyboard());
        }
    }

    private void handleStart(Long chatId, String firstName) {
        String msg = "Привет, " + (firstName != null ? firstName : "друг") + "!\n" +
                "Я минимальный Java-бот. Доступно: /start и /help.";
        send(chatId, msg, withMainKeyboard());
    }

    private void handleHelp(Long chatId) {
        String msg = """
                Доступные команды:
/start — приветствие
/help — описание команд

Также есть клавиатура с кнопками Start и Help.
                """;
        send(chatId, msg, withMainKeyboard());
    }

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

    private ReplyKeyboardMarkup withMainKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Start"));
        row.add(new KeyboardButton("Help"));
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);

        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        kb.setKeyboard(rows);
        kb.setResizeKeyboard(true);
        kb.setOneTimeKeyboard(false);
        return kb;
    }

    private void send(Long chatId, String text, ReplyKeyboardMarkup kb) {
        SendMessage sm = new SendMessage(chatId.toString(), text);
        sm.setReplyMarkup(kb);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}

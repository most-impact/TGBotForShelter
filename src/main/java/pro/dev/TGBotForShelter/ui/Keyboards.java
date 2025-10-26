package pro.dev.TGBotForShelter.ui;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import pro.dev.TGBotForShelter.model.Shelter;

import java.util.ArrayList;
import java.util.List;

public final class Keyboards {
    private Keyboards() {}

    public static ReplyKeyboardMarkup chooseShelter(List<Shelter> list) {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow cur = new KeyboardRow();
        for (Shelter s : list) {
            if (cur.size() >= 2) { rows.add(cur); cur = new KeyboardRow(); }
            cur.add(new KeyboardButton(s.getName())); // показываем имя приюта
        }
        if (!cur.isEmpty()) rows.add(cur);

        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        kb.setKeyboard(rows);
        kb.setResizeKeyboard(true);
        kb.setOneTimeKeyboard(false);
        return kb;
    }
}

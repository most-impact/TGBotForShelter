package pro.dev.TGBotForShelter.model;

import java.time.Instant;

public class UserSession {
    private final long telegramId;     // ключ сессии — tg id
    private Stage stage = Stage.WELCOME;
    private Shelter currentShelter;    // выбранный приют (может быть null)
    private Instant lastTouch = Instant.now();

    public UserSession(long telegramId) {
        this.telegramId = telegramId;
    }

    public long getTelegramId() { return telegramId; }
    public Stage getStage() { return stage; }
    public void setStage(Stage stage) { this.stage = stage; }
    public Shelter getCurrentShelter() { return currentShelter; }
    public void setCurrentShelter(Shelter currentShelter) { this.currentShelter = currentShelter; }
    public Instant getLastTouch() { return lastTouch; }
    public void touch() { this.lastTouch = Instant.now(); }
}

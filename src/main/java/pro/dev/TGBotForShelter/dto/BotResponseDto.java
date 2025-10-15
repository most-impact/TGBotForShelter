package pro.dev.TGBotForShelter.dto;

/**
 * DTO для ответа бота на обработанный запрос.
 * Содержит информацию о статусе обработки и текст ответа.
 */
public class BotResponseDto {

    /**
     * Флаг успешной обработки запроса.
     */
    private boolean handled;

    /**
     * Текущий этап взаимодействия (STAGE_0, STAGE_1, STAGE_2, STAGE_3).
     */
    private String stage;

    /**
     * Текст ответа бота пользователю.
     */
    private String responseText;

    /**
     * Причина ошибки при неуспешной обработке.
     */
    private String errorReason;

    /**
     * Конструктор для успешного ответа.
     *
     * @param handled статус обработки
     * @param stage текущий этап взаимодействия
     * @param responseText текст ответа бота
     */
    public BotResponseDto(boolean handled, String stage, String responseText) {
        this.handled = handled;
        this.stage = stage;
        this.responseText = responseText;
    }

    /**
     * Конструктор для ответа с ошибкой.
     *
     * @param handled статус обработки (обычно false)
     * @param errorReason причина ошибки
     */
    public BotResponseDto(boolean handled, String errorReason) {
        this.handled = handled;
        this.errorReason = errorReason;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}

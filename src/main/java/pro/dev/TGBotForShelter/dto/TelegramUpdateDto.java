package pro.dev.TGBotForShelter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для получения обновлений от Telegram Bot API.
 * Содержит информацию о сообщениях и callback-запросах.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUpdateDto {

    /**
     * Уникальный идентификатор обновления.
     */
    @JsonProperty("update_id")
    private Long updateId;

    /**
     * Информация о полученном сообщении.
     */
    private Message message;

    /**
     * Информация о callback-запросе.
     */
    @JsonProperty("callback_query")
    private CallbackQuery callbackQuery;

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public CallbackQuery getCallbackQuery() {
        return callbackQuery;
    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    /**
     * DTO для сообщения Telegram.
     */
    public static class Message {

        /**
         * Идентификатор сообщения.
         */
        @JsonProperty("message_id")
        private Long messageId;

        /**
         * Информация об отправителе.
         */
        private From from;

        /**
         * Информация о чате.
         */
        private Chat chat;

        /**
         * Время отправки сообщения (Unix timestamp).
         */
        private Long date;

        /**
         * Текст сообщения.
         */
        private String text;

        /**
         * Массив фотографий разного размера.
         */
        @JsonProperty("photo")
        private Photo[] photo;

        public Long getMessageId() {
            return messageId;
        }

        public void setMessageId(Long messageId) {
            this.messageId = messageId;
        }

        public From getFrom() {
            return from;
        }

        public void setFrom(From from) {
            this.from = from;
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Photo[] getPhoto() {
            return photo;
        }

        public void setPhoto(Photo[] photo) {
            this.photo = photo;
        }

        /**
         * DTO для фотографии.
         */
        public static class Photo {

            /**
             * Идентификатор файла для получения через Bot API.
             */
            @JsonProperty("file_id")
            private String fileId;

            /**
             * Размер файла в байтах.
             */
            @JsonProperty("file_size")
            private Long fileSize;

            /**
             * Ширина фотографии.
             */
            private int width;

            /**
             * Высота фотографии.
             */
            private int height;

            public String getFileId() {
                return fileId;
            }

            public void setFileId(String fileId) {
                this.fileId = fileId;
            }

            public Long getFileSize() {
                return fileSize;
            }

            public void setFileSize(Long fileSize) {
                this.fileSize = fileSize;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }

    /**
     * DTO для информации об отправителе.
     */
    public static class From {

        /**
         * Уникальный идентификатор пользователя в Telegram.
         */
        private Long id;

        /**
         * Имя пользователя.
         */
        @JsonProperty("first_name")
        private String firstName;

        /**
         * Username пользователя в Telegram.
         */
        private String username;

        /**
         * Флаг является ли пользователь ботом.
         */
        @JsonProperty("is_bot")
        private boolean isBot;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isBot() {
            return isBot;
        }

        public void setBot(boolean bot) {
            isBot = bot;
        }
    }

    /**
     * DTO для информации о чате.
     */
    public static class Chat {

        /**
         * Уникальный идентификатор чата.
         */
        private Long id;

        /**
         * Тип чата (private, group, supergroup, channel).
         */
        private String type;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * DTO для callback-запроса от inline-кнопок.
     */
    public static class CallbackQuery {

        /**
         * Уникальный идентификатор запроса.
         */
        private String id;

        /**
         * Информация об отправителе.
         */
        private From from;

        /**
         * Сообщение с inline-кнопкой.
         */
        private Message message;

        /**
         * Данные callback-кнопки.
         */
        private String data;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public From getFrom() {
            return from;
        }

        public void setFrom(From from) {
            this.from = from;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}

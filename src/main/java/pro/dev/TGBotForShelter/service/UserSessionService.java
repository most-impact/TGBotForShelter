package pro.dev.TGBotForShelter.service;

import org.springframework.stereotype.Service;
import pro.dev.TGBotForShelter.model.UserSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService {
    // ключ: telegramId
    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    public UserSession getOrCreate(long telegramId) {
        return sessions.computeIfAbsent(telegramId, UserSession::new);
    }
}

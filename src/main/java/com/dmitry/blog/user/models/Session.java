package com.dmitry.blog.user.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {

    private UUID sessionId;
    private UUID userID;
    private LocalDateTime expireAt;

    public Session(UUID sessionId, UUID userID, LocalDateTime expireAt) {
        this.sessionId = sessionId;
        this.userID = userID;
        this.expireAt = expireAt;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getUserID() {
        return userID;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }
}

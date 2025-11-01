package com.dmitry.blog.user.repositories;

import com.dmitry.blog.user.models.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    Session save(Session session);

    Optional<Session> findById(UUID sessionId);

    void deleteExpiredSessions();
}

package com.dmitry.blog.user.repositories;

import com.dmitry.blog.user.models.Session;
import com.dmitry.blog.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionRepositoryImpl implements SessionRepository{

    private static final String SESSION_TABLE_CREATE_QUERY = """
            CREATE TABLE IF NOT EXISTS sessions (
                session_id UUID PRIMARY KEY,
                user_id UUID NOT NULL,
                expire_at TIMESTAMP NOT NULL
            );
            """;

    private static final String SAVE_SESSION_QUERY = """
            INSERT INTO sessions (session_id, user_id, expire_at) 
            VALUES (?, ?, ?);
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT session_id, user_id, expire_at 
            FROM sessions 
            WHERE session_id = ?;
            """;

    private static final String DELETE_EXPIRED_QUERY = """
            DELETE FROM sessions 
            WHERE expire_at < NOW();
            """;

    private final Connection connection;

    public SessionRepositoryImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
            initializeDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize SessionRepository", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(SESSION_TABLE_CREATE_QUERY);
        }
    }

    @Override
    public Session save(Session session) {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SESSION_QUERY)) {
            statement.setObject(1, session.getSessionId());
            statement.setObject(2, session.getUserID());
            statement.setTimestamp(3, Timestamp.valueOf(session.getExpireAt()));

            statement.executeUpdate();
            return session;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save session", e);
        }
    }

    @Override
    public Optional<Session> findById(UUID sessionId) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setObject(1, sessionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Session session = mapResultSetToSession(resultSet);
                    return Optional.of(session);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find session by id", e);
        }
    }

    @Override
    public void deleteExpiredSessions() {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_EXPIRED_QUERY)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete expired sessions", e);
        }
    }

    private Session mapResultSetToSession(ResultSet resultSet) throws SQLException {
        UUID sessionId = (UUID) resultSet.getObject("session_id");
        UUID userId = (UUID) resultSet.getObject("user_id");
        LocalDateTime expireAt = resultSet.getTimestamp("expire_at").toLocalDateTime();

        return new Session(sessionId, userId, expireAt);
    }
}

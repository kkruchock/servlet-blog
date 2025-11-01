package com.dmitry.blog.user.repositories;

import com.dmitry.blog.user.models.User;
import com.dmitry.blog.utils.DatabaseConnection;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    private static final String USER_TABLE_CREATE_QUERY = """
            CREATE TABLE IF NOT EXISTS users (
                id UUID PRIMARY KEY,
                email VARCHAR(255) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL
            );
            """;

    private static final String SAVE_USER_QUERY = """
            INSERT INTO users (id, email, password) 
            VALUES (?, ?, ?);
            """;

    private static final String FIND_BY_EMAIL_QUERY = """
            SELECT * 
            FROM users 
            WHERE email = ?;
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM users 
            WHERE id = ?;
            """;

    private final Connection connection;

    public UserRepositoryImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
            initializeDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize UserRepository", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(USER_TABLE_CREATE_QUERY);
        }
    }

    @Override
    public User save(User user) {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_USER_QUERY)) {
            statement.setObject(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());

            statement.executeUpdate();
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL_QUERY)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = resultSetToUser(resultSet);
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email: " + email, e);
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = resultSetToUser(resultSet);
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by id: " + id, e);
        }
    }

    private User resultSetToUser(ResultSet resultSet) throws SQLException {
        UUID id = (UUID) resultSet.getObject("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        return new User(id, email, password);
    }
}
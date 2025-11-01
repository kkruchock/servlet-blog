package com.dmitry.blog.user.services;

import com.dmitry.blog.user.exceptions.AuthenticationException;
import com.dmitry.blog.user.models.Session;
import com.dmitry.blog.user.models.User;
import com.dmitry.blog.user.repositories.SessionRepository;
import com.dmitry.blog.user.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final Duration sessionDuration;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.sessionDuration = Duration.ofMinutes(10);
    }

    public String register(String email, String password, String passwordRepeat) {
        if (!password.equals(passwordRepeat)) {
            throw new AuthenticationException("Passwords don't match");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new AuthenticationException("User with this email already exists");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(email, hashedPassword);
        User savedUser = userRepository.save(user);

        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(
                UUID.fromString(sessionId),
                savedUser.getId(),
                LocalDateTime.now().plus(sessionDuration)
        );
        sessionRepository.save(session);

        return sessionId;
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new AuthenticationException("Email not found");
        }

        User user = userOpt.get();
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(
                UUID.fromString(sessionId),
                user.getId(),
                LocalDateTime.now().plus(sessionDuration)
        );
        sessionRepository.save(session);

        return sessionId;
    }

    public Optional<User> getCurrentUser(String sessionId) {

        if (sessionId == null || sessionId.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            UUID sessionUUID = UUID.fromString(sessionId);
            Optional<Session> sessionOpt = sessionRepository.findById(sessionUUID);

            if (sessionOpt.isEmpty()) {
                return Optional.empty();
            }

            Session session = sessionOpt.get();
            if (session.getExpireAt().isBefore(LocalDateTime.now())) {
                sessionRepository.deleteExpiredSessions();
                return Optional.empty();
            }

            return userRepository.findById(session.getUserID());

        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public boolean isAuthenticated(String sessionId) {
        return getCurrentUser(sessionId).isPresent();
    }
}
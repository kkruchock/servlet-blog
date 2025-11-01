package com.dmitry.blog.user.repositories;

import com.dmitry.blog.user.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
}

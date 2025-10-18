package com.dmitry.blog.post.repositories;

import com.dmitry.blog.post.models.Post;
import com.dmitry.blog.post.models.SortDirection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository {

    void create(Post post);

    void update(Post post);

    void delete(UUID id);

    Optional<Post> findById(UUID id);

    int count();

    List<Post> findAllPaginated(int offset, int limit, SortDirection sortDirection);
}
package com.dmitry.blog.post.services;

import com.dmitry.blog.post.models.Post;
import com.dmitry.blog.post.models.SortDirection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {

    void createPost(String title, String text);

    void updatePost(UUID id, String title, String text);

    void deletePost(UUID id);

    Optional<Post> getPostById(UUID id);

    List<Post> getPostsPage(int page, int pageSize, SortDirection sortDirection);

    int getTotalPages(int pageSize);
}
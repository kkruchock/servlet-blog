package com.dmitry.blog.post.services;

import com.dmitry.blog.post.models.Post;
import com.dmitry.blog.post.models.SortDirection;
import com.dmitry.blog.post.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void createPost(String title, String text) {
        Post post = new Post(title, text);
        postRepository.create(post);
        log.info("Created new post with title: {}", title);
    }

    @Override
    public void updatePost(UUID id, String title, String text) {
        Optional<Post> oldPost = postRepository.findById(id);
        if (oldPost.isPresent()) {
            Post post = oldPost.get();
            post.setTitle(title);
            post.setText(text);
            postRepository.update(post);
            log.info("Updated post with ID: {}", id);
        } else {
            log.warn("Attempted to update non-existent post with ID: {}", id);
        }
    }

    @Override
    public void deletePost(UUID id) {
        postRepository.delete(id);
        log.info("Deleted post with ID: {}", id);
    }

    @Override
    public Optional<Post> getPostById(UUID id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> getPostsPage(int page, int pageSize, SortDirection sortDirection) {
        int offset = (page - 1) * pageSize;
        return postRepository.findAllPaginated(offset, pageSize, sortDirection);
    }

    @Override
    public int getTotalPages(int pageSize) {
        int totalPosts = postRepository.count();
        int pages = totalPosts / pageSize;
        if (totalPosts % pageSize != 0) {
            pages++;
        }
        return pages;
    }
}
package com.dmitry.blog.post.servlets;

import com.dmitry.blog.post.models.Post;
import com.dmitry.blog.post.repositories.PostRepositoryImpl;
import com.dmitry.blog.post.services.PostService;
import com.dmitry.blog.post.services.PostServiceImpl;
import com.dmitry.blog.user.models.User;
import com.dmitry.blog.user.repositories.SessionRepositoryImpl;
import com.dmitry.blog.user.repositories.UserRepositoryImpl;
import com.dmitry.blog.user.services.AuthService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.dmitry.blog.utils.CookieUtils.getSessionIdFromCookies;

@WebServlet("/delete")
public class PostDeleteServlet extends HttpServlet {

    private PostService postService;
    private AuthService authService;

    @Override
    public void init() {
        this.postService = new PostServiceImpl(new PostRepositoryImpl());
        this.authService = new AuthService(
                new UserRepositoryImpl(),
                new SessionRepositoryImpl()
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String sessionId = getSessionIdFromCookies(request);
        Optional<User> currentUser = authService.getCurrentUser(sessionId);

        if (currentUser.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String postId = request.getParameter("id");

        if (postId == null || postId.isEmpty()) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        try {
            UUID postUUID = UUID.fromString(postId);
            Optional<Post> post = postService.getPostById(postUUID);

            if (post.isEmpty()) {
                response.sendRedirect(request.getContextPath());
                return;
            }

            if (!post.get().getAuthorId().equals(currentUser.get().getId())) {
                response.sendRedirect(request.getContextPath());
                return;
            }

            postService.deletePost(postUUID);
            response.sendRedirect(request.getContextPath());

        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath());
        }
    }
}
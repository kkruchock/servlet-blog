package com.dmitry.blog.post.servlets;

import com.dmitry.blog.post.repositories.PostRepositoryImpl;
import com.dmitry.blog.post.services.PostService;
import com.dmitry.blog.post.services.PostServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/delete")
public class PostDeleteServlet extends HttpServlet {

    private PostService postService;

    @Override
    public void init() {
        this.postService = new PostServiceImpl(new PostRepositoryImpl());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String id = request.getParameter("id");

        if (id != null) {
            postService.deletePost(UUID.fromString(id));
        }

        response.sendRedirect(request.getContextPath());
    }
}

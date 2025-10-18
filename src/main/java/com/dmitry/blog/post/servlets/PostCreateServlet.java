package com.dmitry.blog.post.servlets;

import com.dmitry.blog.post.repositories.PostRepositoryImpl;
import com.dmitry.blog.post.services.PostService;
import com.dmitry.blog.post.services.PostServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/create")
public class PostCreateServlet extends HttpServlet {

    private PostService postService;

    @Override
    public void init() {
        this.postService = new PostServiceImpl(new PostRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/post-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String text = request.getParameter("text");

        if (title == null || title.isBlank()) {
            request.setAttribute("error", "Введите заголовок!");
            request.setAttribute("savedTitle", title);
            request.setAttribute("savedText", text);
            request.getRequestDispatcher("/WEB-INF/views/post-form.jsp").forward(request, response);
            return;
        }

        if (text == null || text.trim().isEmpty()) {
            request.setAttribute("error", "Введите текст!");
            request.setAttribute("savedTitle", title);
            request.setAttribute("savedText", text);
            request.getRequestDispatcher("/WEB-INF/views/post-form.jsp").forward(request, response);
            return;
        }

        postService.createPost(title.trim(), text.trim());
        response.sendRedirect(request.getContextPath());
    }
}
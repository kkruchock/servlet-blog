package com.dmitry.blog.post.servlets;

import com.dmitry.blog.post.repositories.PostRepositoryImpl;
import com.dmitry.blog.post.services.PostService;
import com.dmitry.blog.post.services.PostServiceImpl;
import com.dmitry.blog.user.models.User;
import com.dmitry.blog.user.repositories.SessionRepositoryImpl;
import com.dmitry.blog.user.repositories.UserRepositoryImpl;
import com.dmitry.blog.user.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static com.dmitry.blog.utils.CookieUtils.getSessionIdFromCookies;

@WebServlet("/create")
public class PostCreateServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sessionId = getSessionIdFromCookies(request);
        if (!authService.isAuthenticated(sessionId)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/post-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sessionId = getSessionIdFromCookies(request);
        Optional<User> currentUser = authService.getCurrentUser(sessionId);

        if (currentUser.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String title = request.getParameter("title");
        String text = request.getParameter("text");
        User author = currentUser.get();


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

        postService.createPost(title.trim(), text.trim(), author.getId());
        response.sendRedirect(request.getContextPath());
    }
}
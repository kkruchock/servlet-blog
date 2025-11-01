package com.dmitry.blog.post.servlets;

import com.dmitry.blog.post.models.Post;
import com.dmitry.blog.post.models.SortDirection;
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
import java.util.List;
import java.util.Optional;

import static com.dmitry.blog.utils.CookieUtils.getSessionIdFromCookies;

@WebServlet("/")
public class PostShowServlet extends HttpServlet {

    private PostService postService;
    private AuthService authService;
    private static final int PAGE_SIZE = 5;

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
        Optional<User> currentUser = authService.getCurrentUser(sessionId);
        currentUser.ifPresent(user -> request.setAttribute("currentUser", user));

        String pageParam = request.getParameter("page");
        int page = (pageParam == null || pageParam.isEmpty()) ? 1 : Integer.parseInt(pageParam);

        String sortParam = request.getParameter("sort");
        SortDirection sort = "asc".equalsIgnoreCase(sortParam) ? SortDirection.ASC : SortDirection.DESC;

        List<Post> posts = postService.getPostsPage(page, PAGE_SIZE, sort);
        int totalPages = postService.getTotalPages(PAGE_SIZE);

        request.setAttribute("posts", posts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sort", sort);

        request.getRequestDispatcher("/WEB-INF/views/post-list.jsp").forward(request, response);
    }
}
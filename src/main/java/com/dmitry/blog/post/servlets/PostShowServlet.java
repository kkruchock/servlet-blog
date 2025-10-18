package com.dmitry.blog.post.servlets;

import com.dmitry.blog.post.models.Post;
import com.dmitry.blog.post.models.SortDirection;
import com.dmitry.blog.post.repositories.PostRepositoryImpl;
import com.dmitry.blog.post.services.PostService;
import com.dmitry.blog.post.services.PostServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class PostShowServlet extends HttpServlet {

    private PostService postService;
    private static final int PAGE_SIZE = 5;

    @Override
    public void init() {
        this.postService = new PostServiceImpl(new PostRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //получаем + проверяем страницу
        String pageParam = request.getParameter("page");
        int page = (pageParam == null || pageParam.isEmpty()) ? 1 : Integer.parseInt(pageParam);
        //получаем сортировку
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
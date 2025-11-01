<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 17.10.2025
  Time: 23:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dmitry.blog.post.models.Post" %>
<%@ page import="com.dmitry.blog.user.models.User" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="blog" tagdir="/WEB-INF/tags" %>
<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    int currentPage = (Integer) request.getAttribute("currentPage");
    int totalPages = (Integer) request.getAttribute("totalPages");
    String sort = request.getAttribute("sort").toString();
    User currentUser = (User) request.getAttribute("currentUser");
%>
<html>
<head>
    <title>Блог</title>
</head>
<body>
<h1>Блог</h1>

<%-- Блок авторизации --%>
<div style="float: right;">
    <% if (currentUser != null) { %>
    Привет, <%= currentUser.getEmail() %>!
    <form action="/blog/logout" method="post" style="display: inline;">
        <button type="submit">Выйти</button>
    </form>
    <% } else { %>
    <a href="/blog/login">Войти</a> |
    <a href="/blog/register">Регистрация</a>
    <% } %>
</div>

<%-- Кнопка создания поста только для авторизованных --%>
<% if (currentUser != null) { %>
<a href="/blog/create">Новый пост</a>
<% } %>

<blog:sort-buttons currentPage="<%= currentPage %>" sort="<%= sort %>"/>

<% for (Post post : posts) { %>
<div style="border: 1px solid #ccc; padding: 10px; margin: 10px 0;">
    <h3><%= post.getTitle() %></h3>
    <p><%= post.getText() %></p>
    <small>
        <blog:date-format date="<%= post.getCreatedAt() %>"/>
    </small>
    <br>

    <%-- Проверка авторства прямо в JSP --%>
    <% if (currentUser != null && post.getAuthorId().equals(currentUser.getId())) { %>
    <blog:post-actions
            postId="<%= post.getId().toString() %>"
            createdAt="<%= post.getCreatedAt() %>"
    />
    <% } %>
</div>
<% } %>

<blog:pagination currentPage="<%= currentPage %>" totalPages="<%= totalPages %>"/>
</body>
</html>
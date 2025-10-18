<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 17.10.2025
  Time: 23:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dmitry.blog.post.models.Post" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="blog" tagdir="/WEB-INF/tags" %>
<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    int currentPage = (Integer) request.getAttribute("currentPage");
    int totalPages = (Integer) request.getAttribute("totalPages");
    String sort = request.getAttribute("sort").toString();
%>

<h1>Блог</h1>
<a href="create">Новый пост</a>

<blog:sort-buttons currentPage="<%= currentPage %>" sort="<%= sort %>"/>

<% for (Post post : posts) { %>
    <h3><%= post.getTitle() %></h3>
    <p><%= post.getText() %></p>
    <blog:date-format date="<%= post.getCreatedAt() %>"/>
    <blog:post-actions postId="<%= post.getId().toString() %>" createdAt="<%= post.getCreatedAt() %>"/>
    <hr>
<% } %>

<blog:pagination currentPage="<%= currentPage %>" totalPages="<%= totalPages %>"/>
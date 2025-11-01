<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 17.10.2025
  Time: 23:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dmitry.blog.post.models.Post" %>
<%
  Post post = (Post) request.getAttribute("post");
  boolean isEdit = (post != null);
  String error = (String) request.getAttribute("error");
  String savedTitle = (String) request.getAttribute("savedTitle");
  String savedText = (String) request.getAttribute("savedText");
%>
<html>
<head>
  <title><%= isEdit ? "Редактировать пост" : "Создать пост" %></title>
</head>
<body>
<h1><%= isEdit ? "Редактировать пост" : "Создать пост" %></h1>

<% if (error != null) { %>
<p style="color: red;"><%= error %></p>
<% } %>

<form method="post" action="<%= isEdit ? "/blog/edit" : "/blog/create" %>">
  <% if (isEdit) { %>
  <input type="hidden" name="id" value="<%= post.getId() %>">
  <% } %>

  Заголовок:<br>
  <input type="text" name="title" value="<%= isEdit ? post.getTitle() : (savedTitle != null ? savedTitle : "") %>" style="width: 300px;"><br><br>

  Текст:<br>
  <textarea name="text" rows="10" style="width: 500px;"><%= isEdit ? post.getText() : (savedText != null ? savedText : "") %></textarea><br><br>

  <button type="submit"><%= isEdit ? "Обновить" : "Создать" %></button>
  <a href="./">Отмена</a>
</form>
</body>
</html>
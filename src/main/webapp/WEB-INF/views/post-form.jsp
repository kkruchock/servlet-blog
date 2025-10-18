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

<h1><%= isEdit ? "Редактировать" : "Создать" %></h1>

<% if (error != null) { %>
    <p style="color: red;"><%= error %></p>
<% } %>

<form method="post">
  <% if (isEdit) { %>
      <input type="hidden" name="id" value="<%= post.getId() %>">
  <% } %>

  Заголовок:<br>
  <input type="text" name="title" value="<%= isEdit ? post.getTitle() : (savedTitle != null ? savedTitle : "") %>"><br>

  Текст:<br>
  <textarea name="text"><%= isEdit ? post.getText() : (savedText != null ? savedText : "") %></textarea><br>

  <button><%= isEdit ? "Обновить" : "Создать" %></button>
  <a href="./">Отмена</a>
</form>
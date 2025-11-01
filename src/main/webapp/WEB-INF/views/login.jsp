<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 01.11.2025
  Time: 1:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Вход</title>
</head>
<body>
<h2>Вход в систему</h2>

<% if (error != null) { %>
<p style="color: red;"><%= error %></p>
<% } %>

<form action="login" method="post">
    Email:<br>
    <input type="email" name="email" required style="width: 250px;"><br><br>

    Пароль:<br>
    <input type="password" name="password" required style="width: 250px;"><br><br>

    <button type="submit">Войти</button>
</form>

<p>Нет аккаунта? <a href="/blog/register">Зарегистрироваться</a></p>
<p><a href="/blog/">На главную</a></p>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 01.11.2025
  Time: 1:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String error = (String) request.getAttribute("error");
%>
<html>
<head>
  <title>Регистрация</title>
</head>
<body>
<h2>Регистрация</h2>

<% if (error != null) { %>
<p style="color: red;"><%= error %></p>
<% } %>

<form action="/blog/register" method="post">
  Email:<br>
  <input type="email" name="email" required style="width: 250px;"><br><br>

  Пароль:<br>
  <input type="password" name="password" required style="width: 250px;"><br><br>

  Повторите пароль:<br>
  <input type="password" name="passwordRepeat" required style="width: 250px;"><br><br>

  <button type="submit">Зарегистрироваться</button>
</form>
<p>Уже есть аккаунт? <a href="/blog/login">Войти</a></p>
</body>
</html>
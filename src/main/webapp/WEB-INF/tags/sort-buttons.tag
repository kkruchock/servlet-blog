<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 18.10.2025
  Time: 1:00
  To change this template use File | Settings | File Templates.
--%>
<%-- src/main/webapp/WEB-INF/tags/post-actions.tag --%>
<%@ tag description="Sort Buttons" pageEncoding="UTF-8"%>
<%@ attribute name="currentPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="sort" required="true" type="java.lang.String" %>

<div>
    <strong>Сортировка:</strong>
    <% if ("desc".equalsIgnoreCase(sort)) { %>
        <strong style="color: blue;">Новые сначала</strong> |
        <a href="?page=<%= currentPage %>&sort=asc">Старые сначала</a>
    <% } else { %>
        <a href="?page=<%= currentPage %>&sort=desc">Новые сначала</a> |
        <strong style="color: blue;">Старые сначала</strong>
    <% } %>
</div>
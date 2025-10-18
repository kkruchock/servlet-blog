<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 18.10.2025
  Time: 1:36
  To change this template use File | Settings | File Templates.
--%>
<%@ tag description="Pagination" pageEncoding="UTF-8" %>
<%@ attribute name="currentPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="totalPages" required="true" type="java.lang.Integer" %>

<% if (totalPages > 1) { %>
  <div style="margin: 20px 0; text-align: center;">
    Страницы:
    <% for (int i = 1; i <= totalPages; i++) { %>
      <% if (i == currentPage) { %>
        <strong style="background: #007bff; color: white; padding: 3px 8px; border-radius: 3px; margin: 0 2px;">
          <%= i %>
          </strong>
      <% } else { %>
          <a href="?page=<%= i %>"
          style="text-decoration: none; border: 1px solid #ddd; padding: 3px 8px; border-radius: 3px; margin: 0 2px;">
          <%= i %>
          </a>
      <% } %>
  <% } %>
</div>
<% } %>
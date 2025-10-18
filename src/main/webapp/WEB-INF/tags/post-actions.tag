<%@ tag description="Post Actions" pageEncoding="UTF-8" %>
<%@ attribute name="postId" required="true" type="java.lang.String" %>
<%@ attribute name="createdAt" required="true" type="java.time.LocalDateTime" %>

<div style="margin-top: 10px;">

    <a href="edit?id=<%= postId %>"
       style="text-decoration: none;
        background: #28a745;
        color: white;
        padding: 3px 8px;
        border-radius: 3px;
        margin-right: 5px;">
        Редактировать
    </a>

    <form method="post" action="delete" style="display: inline;">
        <input type="hidden" name="id" value="<%= postId %>">
        <button type="submit"
                style="background: #dc3545;
                color: white;
                border: none;
                padding: 3px 8px;
                border-radius: 3px;
                cursor: pointer;">
            Удалить
        </button>
    </form>
</div>
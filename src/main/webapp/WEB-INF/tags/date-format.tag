<%--
  Created by IntelliJ IDEA.
  User: Дмитрий
  Date: 18.10.2025
  Time: 1:26
  To change this template use File | Settings | File Templates.
--%>
<%@ tag description="Date Formatter" pageEncoding="UTF-8" %>
<%@ attribute name="date" required="true" type="java.time.LocalDateTime" %>

<%
    String formatted = date.toString().replace("T", " ").substring(0, 16);
%>

<%= formatted %>
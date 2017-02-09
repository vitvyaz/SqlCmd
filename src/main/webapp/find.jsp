<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <table border="1">
            <a href="insert?table=<%= request.getParameter("table") %>">insert</a>
            &nbsp
            <a href="update?table=<%= request.getParameter("table") %>">update</a>
            &nbsp
            <a href="menu">menu</a><br>
            <hr>

            <b>Table: <%= request.getParameter("table") %></b>
            <c:forEach items="${data}" var="row">
                <tr>
                <c:forEach items="${row}" var="element">
                    <td>${element}</td>
                </c:forEach>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
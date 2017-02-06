<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <b>Insert row in table: <%= request.getParameter("table") %></b>
        <form action="insert" method="post">
            <table>
                <c:forEach items="${columns}" var="column">
                    <tr>
                        <td>${column}</td>
                        <td><input type="text" name="${column}value" /></td>
                    </tr>
                </c:forEach>
                <td></td>
                <td><input type="submit" name="insert"/></td>
            </table>
         </form>
    </body>
</html>
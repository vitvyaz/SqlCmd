<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <a href="menu">menu</a><br>
        <hr>
        <b>Update table: <%= request.getParameter("table") %></b>
        <form action="update" method="post">
            <table>
                <c:forEach items="${columns}" var="column">
                    <tr>
                        <td>${column}</td>
                        <td><input type="text" name="${column}value" /></td>
                    </tr>
                </c:forEach>
            </table>

            <b>Conditions for update:</b><br>
            <select name="column1">
                <c:forEach items="${columns}" var="column">
                    <option>column</option>
                </c:forEach>
            </select>
            <select name="sing1">
                <option>=</option>
                <option><</option>
                <option>></option>
                <option><></option>
            </select>
            <input type="text" name="value1"/><br>
            <hr>
            <td><input type="submit" value="update" name="update"/></td>
         </form>
    </body>
</html>
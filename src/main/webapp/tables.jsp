<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <a href="menu">menu</a>
        <hr>
        <b>Tables:</b><br>
        <table border="1">
            <c:forEach items="${tables}" var="item">
                <tr>
                    <td><a href="find?table=${item}">${item}</a></td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
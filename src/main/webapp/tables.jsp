<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <table border="1">
            <c:forEach items="${tables}" var="row">
                <tr>
                    <td>${row}</td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
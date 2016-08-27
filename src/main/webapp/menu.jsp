<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <c:forEach items="${items}" var="item">
            <a href="${item}">${item}</a><br>
        <td>${element.getStatus()}</td>
        </c:forEach>
    </body>
</html>
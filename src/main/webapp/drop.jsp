<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <a href="menu">menu</a><br>
        <hr>
        <b>Drop table: <%= request.getParameter("table") %></b><br>
        Are you sure?
        <form action="drop" method="post">
            <input type="radio" name="confirm" value="yes"/>Yes
            <input type="radio" name="confirm" value="no"/>No
            <br>
            <td><input type="submit" name="submit"/></td>
        </form>
    </body>
</html>
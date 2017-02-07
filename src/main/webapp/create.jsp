<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="create" method="post">
            <a href="menu">menu</a>
            <hr>
            <b>Create table</b><br>
            Description: <br>
            <p>tableName ( columnName1 dataType1 [PRIMARY KEY] [NOT NULL], ... columnNameN dataTypeN [NOT NULL] )</p>
            <textarea name="query" cols="60" rows="3"></textarea>
            <input type="submit" name="create"/>
        </form>
    </body>
</html>
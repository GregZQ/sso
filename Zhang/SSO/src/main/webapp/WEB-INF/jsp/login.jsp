<%--
  Created by IntelliJ IDEA.
  User: Jone
  Date: 2018-01-11
  Time: 22:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<html>
<head>
    <title>登陆</title>
</head>
<body>
    <form action="/user/login" method="post">
        请输入用户名:<input  type="text" name="username"/><br>
        请输入密码:<input type="password" name="password"/><br>
        <input name="url" type="hidden" value="${url}"/>
        <input type="submit" value="提交"/>

    </form>
</body>
</html>

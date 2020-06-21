<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: abderrahman.boudrai
  Date: 23/05/2020
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="d" %>

<html>
<head>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
          crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="resources/css/styles-menu.css"/>"/>
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <script src="script.js"></script>
    <title>MagickTricks BO</title>
</head>
<body>
<div id='logo'>
    <img src="<c:url value="resources/images/logo.png"/>"/>
</div>

<div id='cssmenu'>
    <ul>
        <li class='active'><a href='/login'><span>Users</span></a></li>
        <li><a href='/videos'><span>Videos</span></a></li>
    </ul>
</div>
<div id="common-table">
    <table class="tftable" border="1" id="tableId">
        <tr>
            <th>ID</th>
            <th>EMAIL</th>
            <th>NICKNAME</th>
            <th>PASSWORD</th>
            <th>PHOTO</th>
            <th>BAN</th>
            <th>DELETE</th>
        </tr>
        <d:forEach items="${users}" var="user" varStatus="status">
            <tr>
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.nickName}</td>
                <td>${user.password}</td>
                <td><img src="${user.photo}" width="100" height="100"/></td>
                <form:form method="POST" modelAttribute="updateUserDto">
                    <form:input path="userId" type="hidden" value="${user.id}"/>
                    <td>
                        <form:button class="btn btn-secondary" type="submit" name="banBnt" value="Ban" onclick="javascript: form.action='/banUser';">${user.banned ? "Unban" : "Ban"}</form:button>
                    </td>
                    <td>
                        <form:button class="btn btn-secondary" name="deleteBnt" type="submit" value="Delete" onclick="javascript: form.action='/deleteUser';">Delete</form:button>
                    </td>
                </form:form>
            </tr>
        </d:forEach>

    </table>
</div>
</body>

</html>

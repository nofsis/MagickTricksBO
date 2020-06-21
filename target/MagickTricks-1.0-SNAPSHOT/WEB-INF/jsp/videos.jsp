<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: abderrahman.boudrai
  Date: 23/05/2020
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="d" %>

<body>
<head>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="resources/css/styles-menu.css"/>"/>
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <script src="script.js"></script>
    <title>MagickTricks BO</title>
</head>
<div id='logo'>
    <img src="<c:url value="resources/images/logo.png"/>"/>
</div>

<div id='cssmenu'>
    <ul>
        <li ><a href='/login'><span>Users</span></a></li>
        <li class='active'><a href='/videos'><span>Videos</span></a></li>
    </ul>
</div>


<div id="common-table">
    <table class="tftable" border="1" id="tableId">
        <tr>
            <th>ID</th>
            <th>TITULO</th>
            <th>DURACION</th>
            <th>Nº REPRODUCCIONES</th>
            <th>VALORACION</th>
            <th>VIDEO</th>
            <th>DELETE</th>
        </tr>
        <d:forEach items="${videos}" var="video" varStatus="status">
            <tr>
                <td>${video.uploadId}</td>
                <td>${video.name}</td>
                <td>${video.duracion}</td>
                <td>${video.numeroReproducciones}</td>
                <td>${video.valoracion}</td>
                <td><img src="${video.imageUrl}" width="100" height="100"/></td>
                <form:form method="POST" modelAttribute="updateVideoDto">
                    <form:input path="videoId" type="hidden" value="${video.uploadId}"/>
                    <td>
                        <form:button class="btn btn-secondary" name="deleteBnt" type="submit" value="Delete" onclick="javascript: form.action='/deleteVideo';">Delete</form:button>
                    </td>
                </form:form>

            </tr>
        </d:forEach>

    </table>


</div>
</body>
</html>

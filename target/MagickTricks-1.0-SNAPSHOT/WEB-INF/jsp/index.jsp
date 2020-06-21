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

<html>
<head>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="resources/css/styles.css"/>"/>
    <title>MagickTricks BO</title>
</head>
<body>



<div class="wrapper">

    <form:form class="form-signin" method="POST" action="/login" modelAttribute="userLoginDto" >
        <h2 class="form-signin-heading">Please login</h2>
        <form:input type="text" class="form-control" path="user" placeholder="Email Address" required="" autofocus="" />
        <form:input type="password" class="form-control" path="password" placeholder="Password" required=""/>
        <!--<label class="checkbox">
            <input type="checkbox" value="remember-me" id="rememberMe" name="rememberMe"> Remember me
        </label>-->
        <form:button class="btn btn-secondary" type="submit">Login</form:button>
    </form:form>
</div>


</body>

</html>

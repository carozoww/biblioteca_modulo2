<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<%@include file="WEB-INF/components/header.jsp"%>

<%  String correo = (String) request.getParameter("correo");  %>

<%  String pass = (String) request.getParameter("pass"); %>

<h1>Iniciar Sesión / Registrarse</h1>

<%-- Mensaje de error si existe --%>
<% if(request.getParameter("error") != null) { %>
<div style="color: red;">Error en el login</div>
<% } %>

<form action="users" method="post">
    <label for="correo">Correo electronico:</label>
    <input type="text" id="correo" name="correo" required>

    <br>

    <label for="pass">Contraseña:</label>
    <input type="password" id="pass" name="pass" required>

    <br>

    <button type="submit">Registrarse / Login</button>
</form>

<p><a href="users">Ver lista de usuarios (sin login)</a></p>
<a href="register">Registro</a>

</body>
</html>
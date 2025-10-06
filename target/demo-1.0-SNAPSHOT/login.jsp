<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<%@include file="WEB-INF/components/header.jsp"%>

<h1>Iniciar Sesión / Registrarse</h1>

<%-- Mensaje de error si existe --%>
<% if(request.getParameter("error") != null) { %>
<div style="color: red;">Error en el login</div>
<% } %>

<form action="users" method="post">
    <label for="nombre">Nombre:</label>
    <input type="text" id="nombre" name="nombre" required>

    <br>

    <label for="apellido">Apellido:</label>
    <input type="text" id="apellido" name="apellido" required>

    <br>

    <label for="pass">Contraseña:</label>
    <input type="password" id="pass" name="pass" required>

    <br>

    <button type="submit">Registrarse / Login</button>
</form>

<p><a href="users">Ver lista de usuarios (sin login)</a></p>

</body>
</html>
<%@ page import="java.util.List" %>
<%@ page import="models.Lector" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <style><%@include file="./WEB-INF/estilo/stiloform.css"%></style>
</head>
<body>


<% Boolean existeCedula = false;
    Lector usuarioLogueado = (Lector) session.getAttribute("authUser");
    if (usuarioLogueado != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
%>

<div id="encabezado">
    <h1>Biblioteca</h1>
</div>
<div  id="camposytitulo">
    <h2>Inicio de Sesión</h2>
    <div class="formulario">
        <form action="login-lector" method="post">
            <div id="campoynombre">
                <label for="cedula">Cédula:</label>
                <input type="number" id="campo" name="cedula" required >
            </div>

            <div id="campoynombre">
                <label for="pass">Contraseña:</label>
                <input type="password" id="campo" name="pass" required>
            </div>

            <div id="campoynombre">
                <button type="submit">Login</button>
            </div>
            <% if(request.getAttribute("cedulaRegistrada") != null) {
                existeCedula = (Boolean) request.getAttribute("cedulaRegistrada");
            } %>

            <% if(existeCedula && existeCedula != null){ %>
            <p>Contraseña incorrecta</p>
            <% }%>
        </form>

        <a href="register">Registro</a>

    </div>
</div>


</body>
</html>
<%@ page import="java.util.List" %>
<%@ page import="models.Lector" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <style><%@include file="./WEB-INF/estilo/login.css"%></style>
</head>
<body>


<% Boolean existeCedula = false;
    Lector usuarioLogueado = (Lector) session.getAttribute("authUser");
    if (usuarioLogueado != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
%>

<div  id="container">
    <div id="container-logo">
        <img src="imgs/logo.jpg" alt="" width="150px" height="150px">
        <h1>Inicio de Sesión</h1>
    </div>
    <div class="seccion-campos">

        <form action="login-lector" method="post">
            <div class="ingreso">
                <label for="cedula">Cédula:</label>
                <input type="number" id="campo" name="cedula" required >
            </div>

            <div class="ingreso">
                <label for="">Contraseña:</label>
                <div class="ingreso-pass">
                    <input type="password" id="password" name="pass" class="password-input">
                    <img src="imgs/eye-fill-6.png" alt="" class="ojologo">
                </div>
            </div>

            <div id="botones">
                <button type="button" onclick="window.location.href='dashboardUsuario'">Regresar</button>
                <button type="submit">Iniciar Sesión</button>
            </div>

            <div id="aviso">
                <p>¿No tienes una cuenta?</p>
                <p><a href="register">Crear una cuenta</a></p>
            </div>

            <% if(request.getAttribute("cedulaRegistrada") != null) {
                existeCedula = (Boolean) request.getAttribute("cedulaRegistrada");
            } %>

            <% if(existeCedula && existeCedula != null){ %>
            <p class="mensajeError">Contraseña incorrecta</p>
            <% }%>
        </form>


    </div>
</div>
<script>
    let ojoiconos = document.querySelectorAll('.ojologo');
    let contras = document.querySelectorAll('.password-input');

    ojoiconos.forEach((ojoicono, index) => {
        ojoicono.onclick = function () {
            if (contras[index].type == 'password') {
                contras[index].type = 'text';
                ojoicono.src = 'imgs/eye-close-fill-4.png';
            } else {
                contras[index].type = 'password';
                ojoicono.src = 'imgs/eye-fill-6.png';
            }
        }
    });
</script>


</body>
</html>
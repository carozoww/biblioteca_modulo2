<%@ page import="models.Administrador" %><%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 25/10/2025
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean existeCorreo = false;
 Administrador admin = (Administrador) session.getAttribute("authAdmin");
    if (admin != null) {
        response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
        return;
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión</title>
    <style><%@include file="./WEB-INF/estilo/login.css"%></style>
</head>
<body>
<div id="container">
    <div id="container-logo">
        <img src="imgs/logo.jpg" alt="" width="150px" height="150px">
        <h1>Inicio de Sesión</h1>
    </div>
    <div id="seccion-campos">
        <form action="login-admin" method="POST">
            <div class="ingreso">
                <label for="correo">Correo Electronico:</label>
                <input type="email" name="correo">
            </div>
            <div class="ingreso">
                <label for="">Contraseña:</label>
                <div class="ingreso-pass">
                    <input type="password" id="password" name="password" class="password-input">
                    <img src="imgs/eye-fill-6.png" alt="" class="ojologo">
                </div>
            </div>

            <div id="botones">
                <button>Cancelar</button>
                <button type="submit">Iniciar Sesión</button>
            </div>

            <%
                if(request.getAttribute("existeCorreo") != null){
                    existeCorreo = (Boolean)request.getAttribute("existeCorreo");

                if(existeCorreo){ %>
            <p id="mensajeError">Contraseña Incorrecta</p>
            <%}else if(!existeCorreo){%>
            <p id="mensajeError">Correo y Contraseña incorrecta</p>
            <% }
            }%>
            <div id="aviso">
                <p>¿No tienes una cuenta?</p>
                <p><a href="registerAdmin">Crear una cuenta</a></p>
            </div>

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

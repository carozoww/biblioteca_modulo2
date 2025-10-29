<%@ page import="models.Administrador" %><%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 25/10/2025
  Time: 16:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% Boolean existeAdmin = false;


    if(request.getAttribute("existeAdmin") != null){
        existeAdmin = (Boolean) request.getAttribute("existeAdmin");
}  %>

<%  Administrador admin = (Administrador) session.getAttribute("authAdmin");
    if (admin != null) {
        response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
        return;
    }%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style><%@include file="./WEB-INF/estilo/login.css"%></style>
</head>
<body>
<div id="container">
    <div id="container-logo">
        <img src="imgs/logo.jpg" alt="" width="150px" height="150px">
        <h1>Registro</h1>
    </div>
    <div id="seccion-campos">
        <form action="registerAdmin" method="POST">
            <div class="ingreso">
                <label for="Nombre">Nombre:</label>
                <input type="text" name="nombre">
            </div>
            <div class="ingreso">
                <label for="correo">Correo Electronico:</label>
                <input type="email" name="correo">
            </div>
            <div class="ingreso">
                <label for="fecha">Fecha de nacimiento</label>
                <input type="date" name="fecha">
            </div>
            <div class="ingreso">
                <label for="password">Contraseña:</label>
                <div class="ingreso-pass">
                    <input type="password" id="password" class="password-input" name="password">
                    <img src="imgs/eye-fill-6.png" alt="" class="ojologo">
                </div>
            </div>
            <div class="ingreso">
                <label for="">Repetir contraseña:</label>
                <div class="ingreso-pass">
                    <input type="password" id="repetir-password" class="password-input" name="passwordConfirm">
                    <img src="imgs/eye-fill-6.png" alt="" class="ojologo">
                </div>
            </div>

            <div id="botones">
                <button>Cancelar</button>
                <button type="submit" id="botonSubmit">Registrar</button>
            </div>
            <% if(existeAdmin) {%>
            <p id="mensajeErrorCorreo"> Ya existe un Administrador con ese correo electronico!!!</p>
            <% }%>
            <p id="mensajeError"></p>
            <div id="aviso">
                <p>¿Ya tienes una cuenta?</p>
                <p><a href="login-admin">Iniciar Sesion</a></p>
            </div>

        </form>
    </div>
</div>
<script>
    let ojoiconos = document.querySelectorAll('.ojologo');
    let contras = document.querySelectorAll('.password-input');

    const botonSubmit = document.getElementById('botonSubmit');

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

    botonSubmit.addEventListener('click', (e) =>{
        const valorContra = document.getElementById('password').value;
        const valorContraRepetida = document.getElementById('repetir-password').value;
        if(valorContra != valorContraRepetida){
            e.preventDefault();
            const mensajeP = document.getElementById('mensajeError');
            mensajeP.innerText ="Las contraseñas no coinciden!!!";
        }
    })
</script>
</body>
</html>

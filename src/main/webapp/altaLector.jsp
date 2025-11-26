<%@ page import="models.Lector" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 4/11/2025
  Time: 18:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

    Boolean existeLector = false;
    List<Lector> lectores = new ArrayList<>();
    if(request.getAttribute("lectores") != null){
        lectores = (List<Lector>) request.getAttribute("lectores");
    }

%>
<html>
<head>
    <title>Registrar Lector</title>
    <style><%@include file="./WEB-INF/estilo/login.css"%></style>
    <link rel="icon" type="image/png" href="imgs/logo.jpg">
</head>
<body>
<div id="container">
    <div id="container-logo">
        <img src="imgs/logo.jpg" alt="" width="150px" height="150px">

        <h1>Registro de nuevo lector</h1>
    </div>
    <div class="seccion-campos">
        <form action="alta-lector-servlet" method="post" id="mandar">
                <div class="ingreso">
                    <label for="ced">Cédula</label>
                    <input type="number" name="ced" id="ced" required>
                </div>
                <div class="ingreso">
                    <label for="nombre">Nombre</label>
                    <input type="text" name="nombre" id="nombre" maxlength="49" required>
                </div>

                <div class="ingreso">
                    <label for="correo">Email</label>
                    <input type="email" name="correo" id="correo" maxlength="49" required>
                </div>

                <div class="ingreso">
                    <label for="tele">Teléfono</label>
                    <input type="text" name="tele" id="tele" maxlength="19" required>
                </div>

                <div class="ingreso">
                    <label for="dir">Dirección</label>
                    <textarea name="dir" id="campo" rows="5" cols="40" maxlength="49" required></textarea>
                </div>

                <div class="ingreso">
                    <label for="fecha">Fecha de Nacimiento</label>
                    <br>
                    <input type="date" name="fecha" required>
                </div>


                <div class="ingreso">
                    <label for="pass">Contraseña</label>
                    <input type="password" name="pass" required>
                </div>

                <div id="botones">
                    <button type="button" onclick="window.location.href='dashboardAdmin'">Cancelar</button>
                    <button type="submit">Registrar</button>
                </div>
                <div id="mensajes">

                </div>

            <% if(request.getAttribute("existeLector") != null) {
                existeLector = (Boolean) request.getAttribute("existeLector");
            } %>

            <% if(existeLector && existeLector != null){ %>
            <p class="mensajeError">Ya existe un lector con esa cedula!!!!</p>
            <% }%>

        </form>
    </div>

</div>
<script>

    const mensajes = document.getElementById('mensajes')

    const lectores = [
        <% for(Lector lector : lectores) { %>
        {
            nombre: "<%=lector.getNombre()%>",
            cedula: <%=lector.getCedula()%>,
            correo:"<%=lector.getCorreo()%>",
            telefono: "<%=lector.getTelefono()%>",
        },
        <% } %>
    ];



    console.log(lectores);

    const enviarForm = document.getElementById('mandar');
    enviarForm.addEventListener('submit',(e) =>{

        const cedula = document.getElementById('ced').value;
        const correo = document.getElementById('correo').value;
        const telefono = document.getElementById('tele').value;
        const nombre = document.getElementById('nombre').value;
        const lectorEncontrado = lectores.find(lector => lector.cedula == cedula);
        const existeCorreo = lectores.find(lector => lector.correo == correo);
        const existeTelefono = lectores.find(lector => lector.telefono == telefono);
        const existeNombre = lectores.find(lector => lector.nombre == nombre);

        mensajes.innerHTML = "";

        var hayError = false;

        if(lectorEncontrado){
            hayError = true;
            const contenido = document.createElement('p');
            contenido.innerText = "Ya existe un lector con esa cedula";
            contenido.className = "mensajeError";
            mensajes.appendChild(contenido);
        }
        if(existeCorreo){
            hayError = true;
            const contenido1 = document.createElement('p');
            contenido1.innerText = "Ya existe un lector con ese correo";
            contenido1.className = "mensajeError";
            mensajes.appendChild(contenido1);
        }
        if(existeTelefono){
            hayError = true;
            const contenido2 = document.createElement('p');
            contenido2.innerText = "Ya existe un lector con ese telefono";
            contenido2.className = "mensajeError";
            mensajes.appendChild(contenido2);
        }

        if(existeNombre){
            hayError = true;
            const contenido3 = document.createElement('p');
            contenido3.innerText = "Ya existe un lector con ese nombre";
            contenido3.className = "mensajeError";
            mensajes.appendChild(contenido3);
        }

        if(hayError){
            e.preventDefault();
        }

    });





</script>
</body>
</html>

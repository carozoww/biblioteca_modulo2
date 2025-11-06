<%@ page import="models.Lector" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<link>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

    Boolean existeLector = false;
    List<Lector> lectores = new ArrayList<>();
    if(request.getAttribute("lectores") != null){
        lectores = (List<Lector>) request.getAttribute("lectores");
    }
    Lector usuarioLogueado = (Lector) session.getAttribute("authUser");
    if (usuarioLogueado != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }

%>

<html>
<head>
    <title>Title</title>
    <style><%@include file="./WEB-INF/estilo/login.css"%></style>

</head>
<body>
<div id="container">
    <div id="container-logo">
        <img src="imgs/logo.jpg" alt="" width="150px" height="150px">

        <h1>Registro </h1>
    </div>
    <div class="formulario">
        <form action="register" method="post" id="mandar">
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
                <label for="password">Contraseña:</label>
                <div class="ingreso-pass">
                    <input type="password" id="password" class="password-input" name="pass">
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
                <button type="button" onclick="window.location.href='dashboardUsuario'">Cancelar</button>
                <button type="submit">Registrar</button>
            </div>
            <div id="mensajes">

            </div>

            <p id="mensajeErrorContra" class="mensajeError"></p>

            <div id="aviso">
                <p>¿Ya tienes una cuenta?</p>
                <p><a href="login-lector"> Inicia Sesión!!!</a></p>
            </div>




            <% if(request.getAttribute("existeLector") != null) {
                existeLector = (Boolean) request.getAttribute("existeLector");
            } %>

            <% if(existeLector && existeLector != null){ %>
            <p>Ya existe un lector con esa cedula!!!!</p>
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

        const valorContra = document.getElementById('password').value;
        const valorContraRepetida = document.getElementById('repetir-password').value;
        if(valorContra != valorContraRepetida){
            hayError = true;
            const mensajeP = document.createElement('p');
            mensajeP.innerText ="Las contraseñas no coinciden!!!";
            mensajes.appendChild(mensajeP);
        }

        if(hayError){
            e.preventDefault();
        }

    });

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






</script>


</body>
</html>


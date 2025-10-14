<%@ page import="models.Lector" %>
<link>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

    Boolean existeLector = false;

    Lector usuarioLogueado = (Lector) session.getAttribute("authUser");
    if (usuarioLogueado != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }

%>

<html>
<head>
    <title>Title</title>
    <style><%@include file="./WEB-INF/estilo/stiloform.css"%></style>

</head>
<body>
<div id="encabezado">
    <h1>Biblioteca</h1>
</div>
<div id="camposytitulo">
    <h2>Registro de nuevo lector</h2>
    <div class="formulario">
        <form action="register" method="post">

            <div id="campos">

                <div id="campoynombre">
                    <label for="ced">Cédula</label>
                    <input type="text" name="ced" id="campo">
                </div>
                <div id="campoynombre">
                    <label for="nombre">Nombre</label>
                    <input type="text" name="nombre" id="campo">
                </div>

                <div id="campoynombre">
                    <label for="correo">Email</label>
                    <input type="text" name="correo" id="campo">
                </div>

                <div id="campoynombre">
                    <label for="tele">Teléfono</label>
                    <input type="text" name="tele" id="campo">
                </div>

                <div id="campoynombre">
                    <label for="dir">Dirección</label>
                    <textarea name="dir" id="campo" rows="5" cols="40"></textarea>
                </div>

                <div id="campoynombre">
                    <label for="fecha">Fecha de Nacimiento</label>
                    <br>
                    <input type="date" name="fecha">
                </div>

                <div id="campoynombre">
                    <label for="pass">Contrasenia</label>
                    <input type="password" name="pass">
                </div>

                <div id="campoynombre">
                    <button type="submit">click</button>
                </div>


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



</body>
</html>


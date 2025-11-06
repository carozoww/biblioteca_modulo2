<%@ page import="models.Genero" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Genero genero = (Genero) request.getAttribute("genero");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Género</title>

    <!-- Estilos simples para los botones y tabla -->
    <style><%@include file="./WEB-INF/estilo/estiloscrud.css"%></style>

</head>
<body>
    <nav>
        <div id="logoynombre">
            <a class="generosAdmin" href="generosAdmin">Regresar</a>
            <img src="imgs/logo.jpg" width="100px" height="100px">
            <h1>Biblio-Tech-a</h1>
        </div>
        <div id="elementos_derecha">
            <!-- Botón de cierre de sesión -->
            <a href="logout">Cerrar sesion</a>
        </div>
    </nav>

    <main>

        <h2>Editar Género</h2>
        <form class="crud-form" action="editarGenero" method="post">
            <input type="hidden" name="id" value="<%= genero.getId_genero() %>">
            <label>Nombre:</label>
            <input type="text" name="nombre" value="<%= genero.getNombre() %>" required>
            <br><br>
            <button class="boton" type="submit">Guardar cambios</button>
            <button class="boton" type="button" onclick="window.location.href='generosAdmin'" >Cancelar</button>
        </form>
    </main>

</body>
</html>

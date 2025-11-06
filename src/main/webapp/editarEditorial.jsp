<%@ page import="models.Editorial" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Editorial editorial = (Editorial) request.getAttribute("editorial");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Editorial</title>
    <!-- Estilos simples para los botones y tabla -->
    <style><%@include file="./WEB-INF/estilo/estiloscrud.css"%></style>
</head>
<body>
    <nav>
        <div id="logoynombre">
            <a class="editorialesAdmin" href="editorialesAdmin">Regresar</a>
            <img src="imgs/logo.jpg" width="100px" height="100px">
            <h1>Biblio-Tech-a</h1>
        </div>
        <div id="elementos_derecha">
            <!-- Botón de cierre de sesión -->
            <a href="logout">Cerrar sesion</a>
        </div>
    </nav>

    <main>

        <h2>Editar Editorial</h2>
        <form class="crud-form" action="editarEditorial" method="post">
            <input type="hidden" name="id" value="<%= editorial.getIdEditorial() %>">
            <label>Nombre:</label>
            <input type="text" name="nombre" maxlength="30" value="<%= editorial.getNombre() %>" required>
            <br><br>
            <button class="boton" type="submit">Guardar cambios</button>
            <button class="boton" type="button" onclick="window.location.href='editorialesAdmin'" >Cancelar</button>
        </form>
    </main>

</body>
</html>

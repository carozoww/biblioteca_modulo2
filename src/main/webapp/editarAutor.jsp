
<%@ page import="models.Autor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Autor autor = (Autor) request.getAttribute("autor");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Género</title>
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>
    <!-- Estilos simples para los botones y tabla -->
    <style><%@include file="./WEB-INF/estilo/estiloscrud.css"%></style>
    <style><%@include file="./WEB-INF/estilo/formLibro.css"%></style>
</head>
<body>
<nav>
    <div id="logoynombre">
        <a class="autoresAdmin" href="autoresAdmin">Regresar</a>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <!-- Botón de cierre de sesión -->
        <a href="logout">Cerrar sesion</a>
    </div>
</nav>

<main>

    <h2>Editar Autor</h2>
    <form action="editarAutor" method="post">
        <input type="hidden" name="id" value="<%= autor.getId_autor()%>">
        <label>Nombre:</label>
        <input type="text" name="nombre" value="<%= autor.getNombre() %>" required>
        <label>Apellido:</label>
        <input type="text" name="apellido" value="<%= autor.getApellido() %>" required>
        <br><br>
        <button class="boton" type="submit">Guardar cambios</button>
        <button class="boton" type="button" onclick="window.location.href='autoresAdmin'" >Cancelar</button>
    </form>
</main>

</body>
</html>

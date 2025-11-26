<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/png" href="imgs/logo.jpg">
    <meta charset="UTF-8">
    <title>Agregar Género</title>
    <!-- Estilos simples para los botones y tabla -->
    <style><%@include file="./WEB-INF/estilo/estiloscrud.css"%></style>
<%--
    <style><%@include file="./WEB-INF/estilo/formLibro.css"%></style>
--%>
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
        <h2>Agregar Nuevo Género</h2>
        <form class="crud-form" action="agregarGenero" method="post">
            <label>Nombre:</label>
            <input type="text" name="nombre" required>
            <br><br>
            <button  class="boton" type="submit">Guardar</button>
            <button class="boton" type="button" onclick="window.location.href='generosAdmin'" >Cancelar</button>
        </form>

    </main>
</body>
</html>

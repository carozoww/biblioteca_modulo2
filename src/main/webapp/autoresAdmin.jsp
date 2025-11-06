<%@ page import="java.util.List" %>
<%@ page import="models.Autor" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Autor> autores = (List<Autor>) request.getAttribute("listaAutores");
    String mensaje = (String) request.getParameter("mensaje");
    String error = request.getParameter("error");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generos</title>
    <!-- Estilos simples para los botones y tabla -->
    <style><%@include file="./WEB-INF/estilo/estiloscrud.css"%></style>
</head>
<body>
<nav>
    <div id="logoynombre">
        <a class="dashboardAdmin" href="dashboardAdmin">Regresar</a>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <!-- Botón de cierre de sesión -->
        <a href="logout">Cerrar sesion</a>
    </div>
</nav>

<main>

    <!-- Título principal -->
    <h2 style="text-align:center;">Gestión de Autores</h2>

    <% if (mensaje != null) { %>
    <div class="mensaje <%= mensaje.contains("No se puede") || mensaje.contains("") || mensaje.contains("") ? "error" : "exito" %>">
        <%= mensaje %>
    </div>
    <% } %>

    <!-- Botón para agregar un nuevo autores -->
    <div style="text-align:center; margin-bottom:20px;">
        <form action="agregarAutor" method="get" style="display:inline;">
            <button type="submit" class="agregar">Agregar Autor</button>
        </form>
    </div>

    <!-- Tabla de autores -->
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Nombre del Autor</th>
            <th>Apellido del Autor</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%
            // Si hay géneros, los muestra en la tabla
            if (autores != null && !autores.isEmpty()) {
                int index = 1;
                for (Autor autor : autores) {
        %>
        <tr>
            <td><%= index++ %></td>
            <td><%= autor.getNombre() %></td>
            <td><%= autor.getApellido() %></td>
            <td>
                <!-- Botones de acción CRUD -->
                <form action="editarAutor" method="get" style="display:inline;">
                    <input type="hidden" name="id" value="<%= autor.getId_autor() %>">
                    <button type="submit" class="editar">Editar</button>
                </form>
                <form action="eliminarAutor" method="post" style="display:inline;" onsubmit="return confirm('¿Seguro que deseas eliminar este autor?');">
                    <input type="hidden" name=id value="<%= autor.getId_autor() %>">
                    <button type="submit" class="eliminar">Eliminar</button>
                </form>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <!-- Mensaje si no hay autores -->
        <tr>
            <td colspan="3">No hay autores registrados.</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

</main>

</body>
</html>

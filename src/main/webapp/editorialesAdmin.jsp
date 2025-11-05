<%@ page import="java.util.List" %>
<%@ page import="models.Editorial" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Editorial> editoriales = (List<Editorial>) request.getAttribute("listaEditoriales");
    String mensaje = (String) request.getParameter("mensaje");
    String error = request.getParameter("error");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editoriales</title>
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>
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
    <h2 style="text-align:center;">Gestión de Editoriales</h2>

    <% if (mensaje != null) { %>
    <div class="mensaje <%= mensaje.contains("No se puede") || mensaje.contains("Error") ? "error" : "exito" %>">
        <%= mensaje %>
    </div>
    <% } %>

    <!-- Botón para agregar un nuevo Editorial -->
    <div style="text-align:center; margin-bottom:20px;">
        <form action="agregarEditorial" method="get" style="display:inline;">
            <button type="submit" class="agregar">Agregar Editorial</button>
        </form>
    </div>

    <!-- Tabla de Editoriales -->
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Nombre del Editorial</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%
            // Si hay Editoriales, los muestra en la tabla
            if (editoriales != null && !editoriales.isEmpty()) {
                int index = 1;
                for (Editorial editorial : editoriales) {
        %>
        <tr>
            <td><%= index++ %></td>
            <td><%= editorial.getNombre() %></td>
            <td>
                <!-- Botones de acción CRUD -->
                <form action="editarEditorial" method="get" style="display:inline;">
                    <input type="hidden" name="id" value="<%= editorial.getIdEditorial() %>">
                    <button type="submit" class="editar">Editar</button>
                </form>
                <form action="eliminarEditorial" method="post" style="display:inline;" onsubmit="return confirm('¿Seguro que deseas eliminar este Editorial?');">
                    <input type="hidden" name=id value="<%= editorial.getIdEditorial() %>">
                    <button type="submit" class="eliminar">Eliminar</button>
                </form>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <!-- Mensaje si no hay Editoriales -->
        <tr>
            <td colspan="3">No hay Editoriales registrados.</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

</main>

</body>
</html>

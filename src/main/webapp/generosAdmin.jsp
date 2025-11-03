<%@ page import="models.Administrador" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Genero" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Genero> generos = (List<Genero>) request.getAttribute("listaGeneros");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generos</title>
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
        <h2 style="text-align:center;">Gestión de Géneros</h2>

        <% if (mensaje != null) { %>
        <div class="mensaje <%= mensaje.contains("correctamente") ? "exito" : "error" %>">
            <%= mensaje %>
        </div>
        <% } %>

        <!-- Botón para agregar un nuevo género -->
        <div style="text-align:center; margin-bottom:20px;">
            <form action="agregarGenero" method="get" style="display:inline;">
                <button type="submit" class="agregar">Agregar Género</button>
            </form>
        </div>

        <!-- Tabla de géneros -->
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th>Nombre del Género</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <%
                // Si hay géneros, los muestra en la tabla
                if (generos != null && !generos.isEmpty()) {
                    int index = 1;
                    for (Genero genero : generos) {
            %>
            <tr>
                <td><%= index++ %></td>
                <td><%= genero.getNombre() %></td>
                <td>
                    <!-- Botones de acción CRUD -->
                    <form action="editarGenero" method="get" style="display:inline;">
                        <input type="hidden" name="id" value="<%= genero.getId_genero() %>">
                        <button type="submit" class="editar">Editar</button>
                    </form>
                    <form action="eliminarGenero" method="post" style="display:inline;" onsubmit="return confirm('¿Seguro que deseas eliminar este género?');">
                        <input type="hidden" name=id value="<%= genero.getId_genero() %>">
                        <button type="submit" class="eliminar">Eliminar</button>
                    </form>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <!-- Mensaje si no hay géneros -->
            <tr>
                <td colspan="3">No hay géneros registrados.</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>

    </main>

</body>
</html>

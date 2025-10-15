<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Libro" %>

<%
    List<Libro> libros = (List<Libro>) request.getAttribute("libros");
    List<String> estados = (List<String>) request.getAttribute("estados");
    if (libros == null) libros = new java.util.ArrayList<>();
    if (estados == null) estados = new java.util.ArrayList<>();

    String msg = request.getParameter("msg");

    Boolean tienePrestamo = (Boolean) request.getAttribute("tienePrestamo");
    if (tienePrestamo == null) tienePrestamo = false;
%>




<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catálogo de Libros</title>
    <style><%@include file="./WEB-INF/estilo/estiloslibros.css"%></style></head>
<body>

<h2 style="text-align:center;">Catálogo de Libros</h2>

<% if ("ok".equals(msg)) { %>
<div class="mensaje" style="color:green;">Operación realizada con éxito</div>
<% } else if ("error".equals(msg)) { %>
<div class="mensaje" style="color:red;">Ocurrió un error</div>
<% } %>

<table>
    <tr>
        <th>Título</th>
        <th>ISBN</th>
        <th>Estado</th>
        <th>Acción</th>
    </tr>
    <% for (int i = 0; i < libros.size(); i++) {
        Libro libro = libros.get(i);
        String estado = estados.get(i);
    %>
    <tr>
        <td><%= libro.getTitulo() %></td>
        <td><%= libro.getIsbn() %></td>
        <td class="<%= "DISPONIBLE".equals(estado) ? "disponible" : "no-disponible" %>">
            <%= estado %>
        </td>
        <td>
            <%
                boolean disponible = "DISPONIBLE".equals(estado);
                boolean desactivar = tienePrestamo || !disponible;
            %>
            <form action="prestamos" method="post">
                <input type="hidden" name="accion" value="reservar">
                <input type="hidden" name="idLibro" value="<%= libro.getIdLibro() %>">
                <button type="submit" <%= desactivar ? "disabled style='color:gray;'" : "" %>>
                    <%= desactivar ? "No disponible" : "Reservar" %>
                </button>
            </form>
        </td>
    </tr>
    <% } %>
</table>

<div style="text-align:center; margin-top:20px;">
    <a href="prestamos?accion=listar">Ver mis préstamos</a>
</div>

</body>
</html>

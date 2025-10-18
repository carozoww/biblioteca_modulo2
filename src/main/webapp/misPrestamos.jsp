<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Prestamo" %>
<%@ page import="models.Libro" %>
<%@ page import="dao.LibroDAO" %>
<%@ page import="models.Lector" %>

<%
    // Verificar sesión del usuario
    Lector usuario = (Lector) session.getAttribute("authUser");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login-lector");
        return;
    }

    String msg = request.getParameter("msg");
    List<Prestamo> prestamos = (List<Prestamo>) request.getAttribute("prestamos");
    LibroDAO libroDAO = new LibroDAO();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Préstamos</title>
    <style><%@include file="./WEB-INF/estilo/estiloslibros.css"%></style>
</head>
<body>

<h1>Mis Préstamos</h1>

<% if ("ya_tiene_prestamo".equals(msg)) { %>
<p style="color:red;">Ya tienes un préstamo activo. No puedes reservar otro libro hasta finalizarlo.</p>
<% } else if ("ok".equals(msg)) { %>
<p style="color:green;">Acción realizada correctamente.</p>
<% } else if ("error".equals(msg)) { %>
<p style="color:red;">Ocurrió un error al procesar la acción.</p>
<% } %>

<a href="prestamos?accion=catalogo">Ver Catálogo</a>
<br><br>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Título del Libro</th>
        <th>Fecha Préstamo</th>
        <th>Estado</th>
        <th>Acción</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (prestamos != null && !prestamos.isEmpty()) {
            for (Prestamo p : prestamos) {
                Libro libro = libroDAO.buscarPorId(p.getIdLibro());
    %>
    <tr>
        <td><%= p.getIdPrestamo() %></td>
        <td><%= (libro != null) ? libro.getTitulo() : "Desconocido" %></td>
        <td><%= p.getFechaPrestamo() %></td>
        <td><%= p.getEstado() %></td>
        <td>
            <% if ("PENDIENTE".equals(p.getEstado())) { %>
            <form action="prestamos" method="post" style="display:inline;">
                <input type="hidden" name="accion" value="cancelar">
                <input type="hidden" name="idPrestamo" value="<%= p.getIdPrestamo() %>">
                <input type="hidden" name="idLibro" value="<%= p.getIdLibro() %>">
                <button type="submit" class="btnCancelar">Cancelar</button>
            </form>
            <% } else { %>
            <span>-</span>
            <% } %>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr><td colspan="5">No tienes préstamos registrados.</td></tr>
    <% } %>
    </tbody>
</table>

</body>
</html>

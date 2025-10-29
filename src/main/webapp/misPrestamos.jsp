<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Prestamo" %>
<%@ page import="models.Libro" %>
<%@ page import="dao.LibroDAO" %>
<%@ page import="models.Lector" %>
<%@ page import="java.util.ArrayList" %>

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


    // filtrar préstamos activos o reservados
    List<Prestamo> prestamosActivos = new ArrayList<>();
    List<Prestamo> prestamosDisponibles = new ArrayList<>();

    if (prestamos != null) {
        for (Prestamo p : prestamos) {
            if ("PENDIENTE".equals(p.getEstado()) || "RESERVADO".equals(p.getEstado())) {
                prestamosActivos.add(p);
            } else if ("DISPONIBLE".equals(p.getEstado())) {
                prestamosDisponibles.add(p);
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Préstamos</title>
    <style><%@include file="./WEB-INF/estilo/estilosPrestamos.css"%></style>
</head>
<body>

<nav id="navbar">
    <div id="logoynombre">
        <a href="dashboard.jsp">
            <img src="imgs/logo.jpg" width="100" height="100" alt="Logo Biblio-Tech-a">
        </a>
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="logout">Cerrar sesión</a>
    </div>
</nav>

<main class="main-content">
    <header class="page-header">
        <h2 class="page-title">Mis préstamos</h2>
        <a href="dashboard.jsp" class="btn btn-volver">Volver al inicio</a>
    </header>


    <div class="mensajes">
        <% if ("ya_tiene_prestamo".equals(msg)) { %>
        <p class="msg msg-error">Ya tenés un préstamo activo. No podés reservar otro libro hasta finalizarlo.</p>
        <% } else if ("ok".equals(msg)) { %>
        <p class="msg msg-exito">Acción realizada correctamente.</p>
        <% } else if ("error".equals(msg)) { %>
        <p class="msg msg-error">Ocurrió un error al procesar la acción.</p>
        <% } %>
    </div>



    <section class="tabla-prestamos">
        <h3>Préstamos activos o reservados</h3>
        <table class="tabla">
            <thead>
            <tr>
                <th>Título del libro</th>
                <th>Fecha del Préstamo</th>
                <th>Estado</th>
                <th>Acción</th>
            </tr>
            </thead>
            <tbody>
            <% if (!prestamosActivos.isEmpty()) {
                for (Prestamo p : prestamosActivos) {
                    Libro libro = libroDAO.buscarPorId(p.getIdLibro());
            %>
            <tr>
                <td><%= (libro != null) ? libro.getTitulo() : "Desconocido" %></td>
                <td><%= p.getFechaPrestamo() %></td>
                <td class="estado <%= p.getEstado().toLowerCase() %>"><%= p.getEstado() %></td>
                <td>
                    <form action="prestamos" method="post" class="form-accion">
                        <input type="hidden" name="accion" value="cancelar">
                        <input type="hidden" name="idPrestamo" value="<%= p.getIdPrestamo() %>">
                        <input type="hidden" name="idLibro" value="<%= p.getIdLibro() %>">
                        <button type="submit" class="btn btn-cancelar">Cancelar</button>
                    </form>
                </td>
            </tr>
            <%   }
            } else { %>
            <tr>
                <td colspan="4" class="sin-prestamos">No tenés préstamos activos o reservados.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </section>


    <section class="tabla-prestamos">
        <h3>Préstamos anteriores</h3>
        <table class="tabla">
            <thead>
            <tr>
                <th>Título del Libro</th>
                <th>Fecha del Préstamo</th>
                <th>Fecha de Devolución</th>
                <th>Estado</th>
                <th>Acción</th>
            </tr>
            </thead>
            <tbody>
            <% if (!prestamosDisponibles.isEmpty()) {
                for (Prestamo p : prestamosDisponibles) {
                    Libro libro = libroDAO.buscarPorId(p.getIdLibro());
            %>
            <tr>
                <td><%= (libro != null) ? libro.getTitulo() : "Desconocido" %></td>
                <td><%= p.getFechaPrestamo() %></td>
                <td><%= p.getFechaDevolucion()%></td>
                <td class="estado <%= p.getEstado().toLowerCase() %>"><%= p.getEstado() %></td>
                <td>
                    <span class="sin-accion">-</span>
                </td>
            </tr>
            <%   }
            } else { %>
            <tr>
                <td colspan="4" class="sin-prestamos">No hay préstamos anteriores.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </section>
</main>

<footer class="footer">
    <p>&copy; 2025 Biblio-Tech-a — Sistema de gestión de biblioteca</p>
</footer>
</body>
</html>

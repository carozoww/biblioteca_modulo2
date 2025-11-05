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


    // filtrar préstamos activos, reservados
    List<Prestamo> prestamosActivos = new ArrayList<>();
    // y disponibles (prestamos anteriores)
    List<Prestamo> prestamosFinalizados = new ArrayList<>();

    if (prestamos != null) {
        for (Prestamo p : prestamos) {
            if ("PENDIENTE".equals(p.getEstado()) || ("CONFIRMADO".equals(p.getEstado())) || ("RESERVADO".equals(p.getEstado()))){
                prestamosActivos.add(p);
            } else if ("FINALIZADO".equals(p.getEstado())) {
                prestamosFinalizados.add(p);
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
        <a href="dashboard">
            <img src="imgs/logo.jpg" width="100" height="100" alt="Logo Biblio-Tech-a">
        </a>
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="perfil">Cuenta</a>
        <a href="logout">Cerrar sesión</a>
    </div>
</nav>

<main class="main-content">
    <header class="page-header">
        <h2 class="page-title">Mis préstamos</h2>
        <a href="dashboard" class="btn btn-volver">Volver al inicio</a>
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


    <div class="tabs">
        <button class="tablink active" onclick="showTab('activos', this)">Pendiente / Reservado</button>
        <button class="tablink" onclick="showTab('finalizados', this)">Finalizados</button>
    </div>

    <div id="activos" class="tab-content active">
        <section class="tabla-prestamos">
            <h3>Préstamo pendiente o reservado</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>Título del libro</th>
                    <th>Fecha del Préstamo</th>
                    <th>Fecha devolución esperada</th>
                    <th>Estado</th>
                    <th>Acción</th>
                </tr>
                </thead>
                <tbody>
                <% if (!prestamosActivos.isEmpty()) {
                    for (Prestamo p : prestamosActivos) {
                        Libro libro = libroDAO.buscarPorId(p.getIdLibro());
                        String fechaPrestamoStr = p.getFechaPrestamo() != null
                                ? p.getFechaPrestamo().toString().replace("T", " ")
                                : "-";
                        String fechaDevStr = (p.getFechaDevolucionEsperada() != null)
                                ? p.getFechaDevolucionEsperada().toString().replace("T", " ")
                                : "-";
                %>
                <tr>
                    <td><%= (libro != null) ? libro.getTitulo() : "Desconocido" %></td>
                    <td><%= fechaPrestamoStr %></td>
                    <td><%= fechaDevStr %></td>
                    <td class="estado <%= p.getEstado().toLowerCase() %>"><%= p.getEstado() %></td>
                    <td>
                        <% if ("PENDIENTE".equalsIgnoreCase(p.getEstado())) { %>
                        <form action="prestamos" method="post" class="form-accion">
                            <input type="hidden" name="accion" value="cancelar">
                            <input type="hidden" name="idPrestamo" value="<%= p.getIdPrestamo() %>">
                            <input type="hidden" name="idLibro" value="<%= p.getIdLibro() %>">
                            <button type="submit" class="btn btn-cancelar">Cancelar</button>
                        </form>
                        <% } else { %>
                        <p>-</p>
                        <% } %>
                    </td>
                </tr>
                <% } } else { %>
                <tr>
                    <td colspan="5" class="sin-prestamos">No tenés préstamos activos o reservados.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </div>

    <div id="finalizados" class="tab-content">
        <section class="tabla-prestamos">
            <h3>Préstamos anteriores</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>Título del Libro</th>
                    <th>Fecha del Préstamo</th>
                    <th>Fecha de Devolución</th>
                </tr>
                </thead>
                <tbody>
                <% if (!prestamosFinalizados.isEmpty()) {
                    for (Prestamo p : prestamosFinalizados) {
                        Libro libro = libroDAO.buscarPorId(p.getIdLibro());
                %>
                <tr>
                    <td><%= (libro != null) ? libro.getTitulo() : "Desconocido" %></td>
                    <td><%= p.getFechaPrestamo().toString().replace("T", " ") %></td>
                    <td><%= p.getFechaDevolucion().toString().replace("T", " ") %></td>
                </tr>
                <% } } else { %>
                <tr>
                    <td colspan="3" class="sin-prestamos">No tenés préstamos anteriores.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </div>
</main>

<script>
    function showTab(tabId) {
        document.querySelectorAll('.tab-content').forEach(div => div.classList.remove('active'));
        document.querySelectorAll('.tablink').forEach(btn => btn.classList.remove('active'));
        document.getElementById(tabId).classList.add('active');
        event.target.classList.add('active');

        // guarda la pestaña seleccionada
        localStorage.setItem("tabActivaPrestamos", tabId);
    }

    // al cargar la página, revisa que pestaña estaba activa
    window.addEventListener('load', function () {
        const tabGuardada = localStorage.getItem("tabActivaPrestamos");
        if (tabGuardada && document.getElementById(tabGuardada)) {
            document.querySelectorAll('.tab-content').forEach(div => div.classList.remove('active'));
            document.getElementById(tabGuardada).classList.add('active');

            document.querySelectorAll('.tablink').forEach(btn => btn.classList.remove('active'));
            document.querySelector(`.tablink[onclick="showTab('${tabGuardada}')"]`).classList.add('active');
        } else {
            // si no hay pestaña guardada, mostrara la primera
            document.querySelector('.tab-content').classList.add('active');
            document.querySelector('.tablink').classList.add('active');
        }
    });

    // mensaje que desaparece automáticamente
    window.addEventListener('load', function() {
        const msg = document.querySelector('.msg');
        if (msg) {
            setTimeout(() => {
                msg.style.transition = 'opacity 0.8s ease';
                msg.style.opacity = '0';
                setTimeout(() => msg.remove(), 800);
            }, 3000);
        }
    });
</script>

</body>
</html>

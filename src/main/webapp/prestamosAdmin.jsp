<%@ page import="models.Administrador" %>
<%@ page import="models.Prestamo" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.LibroDAO" %>
<%@ page import="dao.LectorDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Libro" %>
<%@ page import="models.Lector" %>
<%
    // Verificar sesión del administrador
    Administrador admin = (Administrador) session.getAttribute("authAdmin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/login-admin");
        return;
    }
    String msg = request.getParameter("msg");
    List<Prestamo> prestamos = (List<Prestamo>) request.getAttribute("prestamos");
    LibroDAO libroDAO = new LibroDAO();
    LectorDAO lectorDAO = new LectorDAO();

    List<Prestamo> prestamosPendientes = new ArrayList<>();
    List<Prestamo> prestamosConfirmados = new ArrayList<>();
    List<Prestamo> prestamosReservados = new ArrayList<>();
    List<Prestamo> prestamosFinalizados = new ArrayList<>();

    // toma los prestamos y los clasifica entre pendientes, aceptados, reservados y finalizados
    if (prestamos != null) {
        for (Prestamo p : prestamos) {
            switch (p.getEstado()) {
                case "PENDIENTE":
                    prestamosPendientes.add(p);
                    break;
                case "CONFIRMADO":
                    prestamosConfirmados.add(p);
                    break;
                case "RESERVADO":
                    prestamosReservados.add(p);
                    break;
                case "FINALIZADO":
                    prestamosFinalizados.add(p);
                    break;
                case "CANCELADO":
                    break;
            }
        }
    }

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Préstamos</title>
    <style>
        <%@include file="./WEB-INF/estilo/estilosPrestamos.css" %>
    </style>
</head>
<body>

<nav id="navbar">
    <div id="logoynombre">
        <a href="dashboardAdmin">
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
        <h2 class="page-title">Gestión de Préstamos</h2>
        <a href="dashboardAdmin" class="btn btn-volver">Volver al inicio</a>
    </header>

    <div class="mensajes">
        <% if ("ok".equals(msg)) { %>
        <p class="msg msg-exito">Acción realizada correctamente.</p>
        <% } else if ("error".equals(msg)) { %>
        <p class="msg msg-error">Ocurrió un error al procesar la acción.</p>
        <% } %>
    </div>
    <div class="tabs">
        <button class="tablink active" onclick="showTab('pendientes', this)">Pendientes</button>
        <button class="tablink" onclick="showTab('confirmados', this)">Confirmados</button>
        <button class="tablink" onclick="showTab('reservados', this)">Reservados</button>
        <button class="tablink" onclick="showTab('finalizados', this)">Finalizados</button>
    </div>

    <div id="pendientes" class="tab-content active">
        <section class="tabla-prestamos">
            <h3>Préstamos pendientes de aprobación</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Lector</th>
                    <th>Libro</th>
                    <th>Fecha de solicitud</th>
                    <th>Acción</th>
                </tr>
                </thead>
                <tbody>
                <% if (!prestamosPendientes.isEmpty()) {
                    for (Prestamo p : prestamosPendientes) {
                        Libro libro = libroDAO.buscarPorId(p.getIdLibro());
                        Lector lector = lectorDAO.buscarPorId(p.getIdLector());
                %>
                <tr>
                    <td><%= p.getIdLector() %>
                    </td>
                    <td><%= lector != null ? lector.getNombre() : "Desconocido" %>
                    </td>
                    <td><%= libro != null ? libro.getTitulo() : "Desconocido" %>
                    </td>
                    <td><%= p.getFechaPrestamo().toString().replace("T", " ") %>
                    </td>
                    <td class="acciones">
                        <form action="prestamosAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="aceptar">
                            <input type="hidden" name="idPrestamo" value="<%= p.getIdPrestamo() %>">
                            <button type="submit" class="btn btn-aceptar">Aceptar</button>
                        </form>
                        <form action="prestamosAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="rechazar">
                            <input type="hidden" name="idPrestamo" value="<%= p.getIdPrestamo() %>">
                            <button type="submit" class="btn btn-cancelar">Rechazar</button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="5" class="sin-prestamos">No hay préstamos pendientes.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </div>

    <div id="confirmados" class="tab-content">
        <section class="tabla-prestamos">
            <h3>Préstamos confirmados</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Lector</th>
                    <th>Libro</th>
                    <th>Acción</th>
                </tr>
                </thead>
                <tbody>
                <% if (!prestamosConfirmados.isEmpty()) {
                    for (Prestamo p : prestamosConfirmados) {
                        Libro libro = libroDAO.buscarPorId(p.getIdLibro());
                        Lector lector = lectorDAO.buscarPorId(p.getIdLector());
                %>
                <tr>
                    <td><%= lector.getID() %>
                    </td>
                    <td><%= lector.getNombre() %>
                    </td>
                    <td><%= libro != null ? libro.getTitulo() : "Desconocido" %>
                    </td>
                    <td class="acciones">
                        <form action="prestamosAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="levantado">
                            <input type="hidden" name="idPrestamo" value="<%= p.getIdPrestamo() %>">
                            <button type="submit" class="btn btn-aceptar">Levantado</button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="4" class="sin-prestamos">No hay préstamos confirmados.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </div>

    <div id="reservados" class="tab-content">
        <section class="tabla-prestamos">
            <h3>Préstamos reservados</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Lector</th>
                    <th>Libro</th>
                    <th>Fecha de solicitud</th>
                    <th>Fecha de devolución esperada</th>
                    <th>Acción</th>
                </tr>
                </thead>
                <tbody>
                <% if (!prestamosReservados.isEmpty()) {
                    for (Prestamo p : prestamosReservados) {
                        Libro libro = libroDAO.buscarPorId(p.getIdLibro());
                        Lector lector = lectorDAO.buscarPorId(p.getIdLector());
                %>
                <tr>
                    <td><%= lector.getID() %>
                    </td>
                    <td><%= lector.getNombre() %>
                    </td>
                    <td><%= libro != null ? libro.getTitulo() : "Desconocido" %>
                    </td>
                    <td><%= p.getFechaPrestamo().toString().replace("T", " ") %>
                    </td>
                    <td><%= p.getFechaDevolucionEsperada().toString().replace("T", " ") %>
                    </td>
                    <td class="acciones">
                        <form action="prestamosAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="finalizar">
                            <input type="hidden" name="idPrestamo" value="<%= p.getIdPrestamo() %>">
                            <button type="submit" class="btn btn-aceptar">Devuelto</button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="6" class="sin-prestamos">No hay préstamos reservados.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </div>

    <div id="finalizados" class="tab-content">
        <section class="tabla-prestamos">
            <h3>Préstamos finalizados</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Lector</th>
                    <th>Libro</th>
                    <th>Fecha de solicitud</th>
                    <th>Fecha de devolución</th>
                </tr>
                </thead>
                <tbody>
                <% if (!prestamosFinalizados.isEmpty()) {
                    for (Prestamo p : prestamosFinalizados) {
                        Libro libro = libroDAO.buscarPorId(p.getIdLibro());
                        Lector lector = lectorDAO.buscarPorId(p.getIdLector());
                %>
                <tr>
                    <td><%= lector.getID() %>
                    </td>
                    <td><%= lector.getNombre() %>
                    </td>
                    <td><%= libro != null ? libro.getTitulo() : "Desconocido" %>
                    </td>
                    <td><%= p.getFechaPrestamo().toString().replace("T", " ") %>
                    </td>
                    <td><%= p.getFechaDevolucion().toString().replace("T", " ") %>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="5" class="sin-prestamos">No hay préstamos finalizados.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </div>

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
</main>
</body>
</html>
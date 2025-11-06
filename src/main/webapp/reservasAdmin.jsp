<%@ page import="models.Administrador" %>
<%@ page import="models.Reserva" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Libro" %>
<%@ page import="models.Lector" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Verificar sesión del administrador
    Administrador admin = (Administrador) session.getAttribute("authAdmin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/login-admin");
        return;
    }
    String msg = request.getParameter("msg");
    List<Reserva> reservas = (List<Reserva>) request.getAttribute("reservas");

    List<Reserva> reservasPendientes = new ArrayList<>();
    List<Reserva> reservasConfirmadas = new ArrayList<>();
    List<Reserva> reservasReservadas = new ArrayList<>();
    List<Reserva> reservasFinalizados = new ArrayList<>();

    // toma las reservas y los clasifica entre pendientes, aceptadas, reservadas y finalizadas
    if (reservas != null) {
        for (Reserva p : reservas) {
            switch (p.getEstado()) {
                case "PENDIENTE":
                    reservasPendientes.add(p);
                    break;
                case "CONFIRMADA":
                    reservasConfirmadas.add(p);
                    break;
                case "RESERVADA":
                    reservasReservadas.add(p);
                    break;
                case "FINALIZADA":
                    reservasFinalizados.add(p);
                    break;
                case "CANCELADA":
                    break;
            }
        }
    }
    reservasFinalizados.sort((a, b) -> b.getFecha_fin_real().compareTo(a.getFecha_fin_real()));

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Reservas</title>
    <style>
        <%@include file="./WEB-INF/estilo/estilosReservas.css" %>
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
        <h2 class="page-title">Gestión de Reservas</h2>
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
        <button class="tablink" onclick="showTab('confirmadas', this)">Confirmadas</button>
        <button class="tablink" onclick="showTab('reservadas', this)">Reservadas</button>
        <button class="tablink" onclick="showTab('finalizadas', this)">Finalizadas</button>
    </div>

    <div id="pendientes" class="tab-content active">
        <div class="search-container">
            <input type="text" id="buscarPendientes" placeholder="Buscar reserva...">
        </div>

        <section class="tabla-reservas">
            <h3>Reservas pendientes de aprobación</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>Lector</th>
                    <th>Sala</th>
                    <th>Inicio</th>
                    <th>Fin</th>
                    <th>Acción</th>
                </tr>
                </thead>
                <tbody>
                <% if (!reservasPendientes.isEmpty()) {
                    for (Reserva p : reservasPendientes) {
                %>
                <tr>
                    <td><%= p.getLector() %>
                    </td>
                    <td><%= p.getSala() %>
                    </td>
                    <td><%= p.getFecha_in().toString().replace("T", " ") %>
                    </td>
                    <td><%= p.getFecha_fin().toString().replace("T", " ") %>
                    </td>
                    <td class="acciones">
                        <form action="reservasAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="aceptar">
                            <input type="hidden" name="idReserva" value="<%= p.getId_Reserva() %>">
                            <button type="submit" class="btn btn-aceptar">Aceptar</button>
                        </form>
                        <form action="reservasAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="rechazar">
                            <input type="hidden" name="idReserva" value="<%= p.getId_Reserva() %>">
                            <button type="submit" class="btn btn-cancelar">Rechazar</button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="5" class="sin-reservas">No hay reservas pendientes.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
        <div id="paginacion">
            <button id="anterior">Anterior</button>
            <button id="siguiente">Siguiente</button>
        </div>
    </div>

    <div id="confirmadas" class="tab-content">
        <div class="search-container">
            <input type="text" id="buscarConfirmadas" placeholder="Buscar reserva...">
        </div>

        <section class="tabla-reservas">
            <h3>Reservas confirmadas</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>Lector</th>
                    <th>Sala</th>
                    <th>Fecha Inicio</th>
                    <th>Acción</th>
                </tr>
                </thead>
                <tbody>
                <% if (!reservasConfirmadas.isEmpty()) {
                    for (Reserva p : reservasConfirmadas) {
                %>
                <tr>
                    <td><%= p.getLector() %>
                    </td>
                    <td><%= p.getSala() %>
                    </td>
                    <td><%= p.getFecha_in().toString().replace("T", " ") %>
                    </td>
                    <td class="acciones">
                        <form action="reservasAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="levantada">
                            <input type="hidden" name="idReserva" value="<%= p.getId_Reserva() %>">
                            <button type="submit" class="btn btn-aceptar">Levantada</button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="4" class="sin-reservas">No hay reservas confirmadas.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
        <div id="paginacion">
            <button id="anterior">Anterior</button>
            <button id="siguiente">Siguiente</button>
        </div>
    </div>

    <div id="reservadas" class="tab-content">
        <div class="search-container">
            <input type="text" id="buscarReservadas" placeholder="Buscar reserva...">
        </div>

        <section class="tabla-reservas">
            <h3>Reservas reservadas</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>Lector</th>
                    <th>Sala</th>
                    <th>Fecha Inicio</th>
                    <th>Fecha Finalizacion esperada</th>
                    <th>Acción</th>
                </tr>
                </thead>
                <tbody>
                <% if (!reservasReservadas.isEmpty()) {
                    for (Reserva p : reservasReservadas) {
                %>
                <tr>
                    <td><%= p.getLector() %>
                    </td>
                    <td><%= p.getSala() %>
                    </td>
                    <td><%= p.getFecha_in().toString().replace("T", " ") %>
                    </td>
                    <td><%= p.getFecha_fin().toString().replace("T", " ") %>
                    </td>
                    <td class="acciones">
                        <form action="reservasAdmin" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="finalizar">
                            <input type="hidden" name="idReserva" value="<%= p.getId_Reserva() %>">
                            <button type="submit" class="btn btn-aceptar">Devuelta</button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="6" class="sin-reservas">No hay reservas reservadas.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
        <div id="paginacion">
            <button id="anterior">Anterior</button>
            <button id="siguiente">Siguiente</button>
        </div>
    </div>

    <div id="finalizadas" class="tab-content">
        <div class="search-container">
            <input type="text" id="buscarFinalizadas" placeholder="Buscar reserva...">
        </div>

        <section class="tabla-reservas">
            <h3>Reservas finalizadas</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>Lector</th>
                    <th>Sala</th>
                    <th>Fecha Inicio</th>
                    <th>Fecha Finalizacion</th>
                </tr>
                </thead>
                <tbody>
                <% if (!reservasFinalizados.isEmpty()) {
                    for (Reserva p : reservasFinalizados) {
                %>
                <tr>
                    <td><%= p.getLector() %>
                    </td>
                    <td><%= p.getSala() %>
                    </td>
                    <td><%= p.getFecha_in().toString().replace("T", " ") %>
                    </td>
                    <td><%= p.getFecha_fin_real().toString().replace("T", " ") %>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="5" class="sin-reservas">No hay reservas finalizadas.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
        <div id="paginacion">
            <button id="anterior">Anterior</button>
            <button id="siguiente">Siguiente</button>
        </div>
    </div>

    <script src="manejoReservas.js"></script>
</main>
</body>
</html>
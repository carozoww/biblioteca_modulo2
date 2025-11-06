<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Reserva" %>
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

    List<Reserva> reservas = (List<Reserva>) request.getAttribute("reservas");

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Reservas</title>
    <style><%@include file="./WEB-INF/estilo/estilosMisReservas.css"%></style>
</head>
<body>

<nav id="navbar">
    <div id="logoynombre">
            <img src="imgs/logo.jpg" width="100" height="100" alt="Logo Biblio-Tech-a">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="perfil">Cuenta</a>
        <a href="logout">Cerrar sesión</a>
    </div>
</nav>

<main class="main-content">
    <header class="page-header">
        <h2 class="page-title">Mis reservas</h2>
        <a href="salas" class="btn btn-volver">Volver a salas</a>
    </header>

    <div id="activos" class="tab-content active">
        <div class="search-container">
            <input type="text" id="buscarReserva" placeholder="Buscar reservas...">
        </div>
        <section class="tabla-reservas">
            <h3>Reservas Terminadas</h3>
            <table class="tabla">
                <thead>
                <tr>
                    <th>Sala</th>
                    <th>Fecha Inicio</th>
                    <th>Fecha Fin</th>
                </tr>
                </thead>
                <tbody>
                <% if (!reservas.isEmpty()) {
                    for (Reserva p : reservas) {
                %>
                <tr>
                    <td><%= p.getSala() %></td>
                    <td><%= p.getFecha_in().toString().replace("T", " ") %></td>
                    <td><%= p.getFecha_fin_real().toString().replace("T", " ") %></td>
                </tr>
                <% } } else { %>
                <tr>
                    <td colspan="3" class="sin-reservas">No tenés reservas finalizadas.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </div>
</main>
<script src="manejoReservas.js"></script>
</body>
</html>

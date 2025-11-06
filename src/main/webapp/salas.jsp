<%@ page import="models.Sala" %>
<%@ page import="models.Lector" %>
<%@ page import="models.Reserva" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Salas</title>
    <style>
        <%@include file="./WEB-INF/estilo/otrocss.css" %>
    </style>
    <style>
        <%@include file="./WEB-INF/estilo/estilosSala.css" %>
    </style>
</head>
<body>
<%
    Lector usuario = (Lector) session.getAttribute("authUser");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login-lector");
        return;
    }
    List<Sala> salasInfo = (List<Sala>) request.getAttribute("listaSalas");
    Reserva reservaActiva = (Reserva) request.getAttribute("reservaActiva");

    String fechaFormateada = "";
    if (reservaActiva != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        fechaFormateada = reservaActiva.getFecha_in().format(formatter);
    }
%>
<%
    String mensaje = (String) session.getAttribute("mensaje");
    if (mensaje != null)
        session.removeAttribute("mensaje");
%>

<nav>
    <div id="logoynombre">
        <button onclick="abrirBarra()" id="botton-barra"> click</button>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="perfil">Cuenta</a>
        <a href="logout">Cerrar sesión</a>
    </div>
</nav>
<main>
    <% if (mensaje != null && !mensaje.equals("null")) { %>
    <div class="mensaje <%= mensaje.contains("No se puede") || mensaje.contains("Error") ? "error" : "exito" %>">
        <%= mensaje %>
    </div>
    <% } %>
<header>
    <a href="dashboard" class="btn-volver ">Volver al inicio</a>
</header>
<input type="hidden" id="mostrarReservaActiva" value="<%= reservaActiva != null ? "conReservaActiva" : "sinReservaActiva" %>" >

    <% if (reservaActiva != null) { %>
    <h1>Reserva sin terminar</h1>
    <div class="reserva-container">
        <h1>Estado: <span class="fecha"><%= reservaActiva.getEstado() %></span></h1>
        <h2>Sala: <span class="sala"><%= reservaActiva.getSala() %></span></h2>
        <h2>Fecha: <span class="fecha"><%= fechaFormateada %></span></h2>
        <div>
            <span>Horas reservadas:</span>
            <span class="hora"><%= reservaActiva.getHora_in() %> - <%= reservaActiva.getHora_fin() %></span>
        </div>
        <% if (reservaActiva.getEstado().equals("PENDIENTE")) { %>
        <form id="realizarReserva" action="salas" method="post" class="form-reserva">
            <input type="hidden" id="finalizar-accion" name="accion" value="cancelar">
            <input type="hidden" id="finalizar-accion" name="accion" value="cancelar">
            <button id="finalizar-boton" class="cancelar-boton" type="submit">Cancelar</button>
        </form>
            <% } %>
    </div>
    <% } else { %>
    <div>
        <% }  %>
        <div>
            <h1><%= reservaActiva != null ? "Listado de salas:" : "Seleccionar sala:" %></h1>
            <div class="contenedorSalas" id="containerSalas"></div>
        </div>
        <% if (reservaActiva == null) { %>
        <h1>Realizar Reserva:</h1>
        <p id="mensajeReserva" class="mensajeReserva">
            <%= !usuario.isAutenticacion() ? "No estas autenticadao. No se puede Reservar" : "Selecciona una fecha y una sala para comenzar la reserva." %>
        </p>

        <form action="salas" method="post" id="formReserva">
            <div id="camposHoras">
                <input type="hidden" name="accion" value="reservar">
                <input type="hidden" id="fecha-enviar" name="fecha-enviar"/>
                <input type="hidden" id="sala-enviar" name="sala-enviar"/>
                <label for="hora-inicio">Hora inicio:</label><br>
                <select id="hora-inicio" name="hora-inicio" required></select>
                <br>
                <br>
                <label for="hora-fin">Hora fin:</label><br>
                <select id="hora-fin" name="hora-fin" required></select>
                <br>
                <br>
                <button type="submit" <%= !usuario.isAutenticacion() ? "disabled" : "" %>>
                    <%= !usuario.isAutenticacion() ? "No estas autenticadao" : "Reservar" %>
                </button>
            </div>
        </form>
        <% } %>
        <div class="contenedorContenido">
            <div>
                <div>
                <h1><%= reservaActiva != null ? "Horarios Disponibles:" : "Seleccionar fecha:" %> </h1>

                <label for="fecha">Fecha:</label>
                <input type="date" id="fecha"/>
                </div>
                <br>
                <br>
                <div id="tabla-container" class="tabla-scroll"></div>
            </div>
        </div>
    </div>



</main>
<script src="salaApp.js"></script>


</body>
</html>

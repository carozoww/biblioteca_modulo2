<%@ page import="models.Sala" %>
<%@ page import="models.Lector" %>
<%@ page import="models.Reserva" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <style><%@include file="./WEB-INF/estilo/stiloform.css"%></style>
</head>
<body>
<div id="encabezado">
    <h1>Biblioteca</h1>
</div>

<%
Lector usuario = (Lector) session.getAttribute("authUser");
if (usuario == null) {
    response.sendRedirect(request.getContextPath() + "/login-lector");
    return;
}
%>

<div id="camposytitulo">
    <h2>Reservar sala </h2>

    <%
    Sala salaInfo = (Sala) request.getAttribute("sala");
    List<Reserva> reservasInfo = (List<Reserva>) request.getAttribute("listaReservas");
    String fechaSeleccionada = (String) request.getAttribute("fechaSeleccionada");
    Integer numeroSala = (int) request.getAttribute("salaSeleccionada");
    %>

    <% if(salaInfo != null) { %>
    <ul>
        <li>
            <p>Numero sala: <%= salaInfo.getNumeroSala() %>
                Ubicacion: <%= salaInfo.getUbicacion() %>
                Limite personas: <%= salaInfo.getMaxPersonas() %> </p>
        </li>
    </ul>
    <% } else {
    response.sendRedirect(request.getContextPath() + "/salas");
    } %>

    <div>
        <form action="reservar" method="post">
            <input type="hidden" name="salaSeleccionada" value="<%= numeroSala %>">
            <label for="fecha">Seleccionar fecha</label>
            <input type="date" id="fecha" name="fecha" value="<%= fechaSeleccionada %>" required>
            <button action="submit"> Seleccionar</button>
        </form>
    </div>
    <p>Horarios ocupados: </p>
    <% if(fechaSeleccionada != null) { %>
    <% if(reservasInfo != null && !reservasInfo.isEmpty()) { %>
    <ul>
        <% for(Reserva reserva : reservasInfo) { %>
        <li>
            <form action="reservar" method="post">
                <p>Hora inicio: <%= reserva.getHora_in() %>
                    Hora Fin: <%= reserva.getHora_fin() %>
                    <% if(reserva.getId_Reserva() != 0) { %>
                    <input type="hidden" name="salaSeleccionada" value="<%= numeroSala %>">
                    <input type="hidden" name="id-reserva" value="<%= reserva.getId_Reserva() %>" required>
                    <button action="submit"> Cancelar reserva</button>
                </p>
            </form>

            <% } %>
        </li>
        <% } %>
    </ul>
    <% } else { %>
    <p>No hay reservas</p>
    <% } %>

    <p>Seleccionar hora de Reserva: </p>

    <form action="reservar" method="post">
        <div id="campos">
            <input type="hidden" name="salaSeleccionada" value="<%= numeroSala %>">
            <input type="hidden" name="fecha" value="<%= fechaSeleccionada %>">
            <div>
                <label for="hora-inicio">Seleccionar hora inicio</label>
                <br>
                <input type="time" id="hora-inicio" name="hora-inicio" required>
            </div>
            <div>
                <label for="hora-fin">Seleccionar hora fin</label>
                <br>
                <input type="time" id="hora-fin" name="hora-fin" required>
            </div>
        </div>
        <br>
        <button action="submit"> Reservar</button>
    </form>

    <% } else { %>
    <p>Fecha no seleccionada</p>
    <% } %>
</div>
</body>
</html>


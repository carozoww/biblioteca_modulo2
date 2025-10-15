<%@ page import="models.Sala" %>
<%@ page import="models.Lector" %>
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

List<Sala> salasInfo = (List<Sala>) request.getAttribute("listaSalas");
%>

<div id="camposytitulo">
    <div>
        <form action="salas" method="post">
            <label for="tipoDato">Elige la opcion</label>
            <select id="tipoDato" name="tipoDato">
                <option value="numeroSala">Numero de sala</option>
                <option value="maxPersonas">Maximo de personas</option>
                <option value="ubicacion">Ubicacion</option>
            </select>
            <br>
            <br>
            <input type="text" name="buscando">
            <button action="submit">Buscar sala</button>
        </form>
    </div>

    <h2>Listado de salas</h2>

    <% if(salasInfo != null && !salasInfo.isEmpty()) { %>
    <ul>
        <form action="reservar" method="post">
            <% for (Sala sala : salasInfo) { %>
            <li>
                <p>Numero sala: <%= sala.getNumeroSala() %>
                    Ubicacion: <%= sala.getUbicacion() %>
                    Limite personas: <%= sala.getMaxPersonas() %> </p>
                <button name="salaSeleccionada" type="submit" value="<%= sala.getNumeroSala() %>">Reservar</button>
            </li>
            <% } %>
        </form>
    </ul>
    <% } else { %>
    <p>No hay salas registrados</p>
    <% } %>

</div>
</body>
</html>


<%@ page import="models.Administrador" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Administrador admin = (Administrador) session.getAttribute("authAdmin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/login-admin");
        return;
    }%>
<%
    String mensaje = (String) request.getAttribute("mensaje");
    if (mensaje != null)
        request.removeAttribute("mensaje");
%>
<html>
<head>
    <title>Salas</title>
    <style>
        <%@include file="./WEB-INF/estilo/otrocss.css" %>
    </style>
    <style>
        <%@include file="./WEB-INF/estilo/estilosAdminSalas.css" %>
    </style>
    <style>
        main {
            width: min(90%, 1000px);
            margin: 10px auto;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
<nav>
    <div id="logoynombre">
        <a class="dashboardAdmin" href="dashboardAdmin">Regresar</a>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <!-- Botón de cierre de sesión -->
        <a href="logout">Cerrar sesion</a>
    </div>
</nav>
<main>
    <% if (mensaje != null) { %>
    <div class="mensaje <%= mensaje.contains("No se puede") || mensaje.contains("Error") ? "error" : "exito" %>">
        <%= mensaje %>
    </div>
    <% } %>
    <div class="form-container">
        <h2 id="textoAccion">Gestionar salas</h2>
        <div style="display: flex; align-items: flex-start; gap: 20px;">
            <img id="imagenSeleccionada" src='imgs/room.png' alt='Sala' width='150' height='150'>
            <form style="flex: 1;" id="editarSala" action="administradorSalas" method="post"
                  enctype="multipart/form-data">
                <input type="hidden" id="accion" name="accion" value="agregar">
                <input type="hidden" id="id-sala" name="id-sala" required>
                <input type="hidden" id="imagen-anterior" name="imagen-anterior" required>
                <div class="container-img">
                    <label for="imagen-url">Imagen:</label>
                    <input type="file" id="imagen-url" name="image" accept="image/*" required>
                </div>
                <div class="form-group">
                    <label for="numero-sala">Número Sala:</label>
                    <input type="text" id="numero-sala" name="numero-sala" maxlength="5" required>
                </div>
                <div class="form-group">
                    <label for="ubicacion">Ubicación:</label>
                    <input id="ubicacion" name="ubicacion" maxlength="30" required>
                </div>
                <div class="form-group">
                    <label for="max-personas">Máximo Personas:</label>
                    <input type="text" id="max-personas" name="max-personas" maxlength="5" required>
                </div>
                <div class="form-group">
                    <label for="habilitada">Habilitada:</label>
                    <select id="habilitada" name="habilitada" required>
                        <option value="">Seleccionar...</option>
                        <option value="true" selected>SI</option>
                        <option value="false">NO</option>
                    </select>
                </div>
                <div class="form-buttons">
                    <button id="boton" class="btn-agregar" action="submit">Agregar</button>
                    <button id="cancelar" class="btn-cancelar hidden" type="button">Cancelar</button>
                </div>
            </form>
        </div>
    </div>
    <div>
        <br>
        <h1>Seleccionar sala</h1>
        <h2>Filtro</h2>
        <br>
        <div id="filtros-reserva-sala">
            <div>
                <label for="filtro-habilitada">Opcion:</label>
                <select id="filtro-habilitada" name="filtro-habilitada" required>
                    <option value="true">Habilitadas</option>
                    <option value="false">Deshabilitadas</option>
                </select>
            </div>

        </div>

        <div id="contenedor-tabla" class="contenedor-tabla">
            <table>
                <thead>
                <tr>
                    <th>Imagen</th>
                    <th>Número Sala</th>
                    <th>Ubicacion</th>
                    <th>Max Personas</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
    <form id="eliminarSala" action="administradorSalas" method="post">
        <input type="hidden" name="accion" value="eliminar">
        <input type="hidden" id="eliminar-sala" name="eliminar-sala">
    </form>
</main>
<footer>

</footer>
<script src="manejoSalas.js"></script>
</body>
</html>

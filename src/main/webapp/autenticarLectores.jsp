<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Lector" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Penalizacion" %>
<%@ page import="dao.PenalizacionDAO" %>
<%@ page import="models.Administrador" %>
<%
    List<Lector> lectores = (List<Lector>) request.getAttribute("lectores");
    Administrador admin = (Administrador) request.getSession().getAttribute("authAdmin");
    if (admin == null) {
        response.sendRedirect("loginAdmin.jsp");
        return;
    }


    String msg = request.getParameter("msg");

%>
<html>
<head>
    <title>Autenticar lectores</title>
    <style>
        <%@include file="./WEB-INF/estilo/estilosListaLectores.css" %>
    </style>
    <link rel="icon" type="image/png" href="imgs/logo.jpg">
</head>
<body>
<nav id="navbar">
    <div id="logoynombre">
        <img src="imgs/logo.jpg" width="100" height="100" alt="Logo Biblio-Tech-a">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="logout">Cerrar sesión</a>
    </div>
</nav>
<main class="main-content">

    <header class="page-header">
        <h2 class="page-title">Lista de lectores</h2>
        <a href="dashboardAdmin" class="btn btn-volver">Volver al inicio</a>
    </header>


    <div id="container">
        <div class="mensajes">
            <% if ("ok".equals(msg)) { %>
            <p class="msg msg-exito">Acción realizada correctamente.</p>
            <% } else if ("error".equals(msg)) { %>
            <p class="msg msg-error">Ocurrió un error al procesar la acción.</p>
            <% } %>
        </div>
        <div id="search-bar">
            <a href="penalizaciones" class="btn btn-volver">Gestionar penalizaciones</a>
            <input type="text" class="search" id="buscar" placeholder="Buscar lector...">
        </div>
        <table id="tabla">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre completo</th>
                <th>Autenticado</th>
                <th>Acción</th>
            </tr>
            </thead>
            <tbody>
            <% if (lectores != null && !lectores.isEmpty()) {
                for (Lector lector : lectores) {
                    { %>
            <tr>
                <td><%= lector.getID() %>
                </td>
                <td><%= lector.getNombre() %>
                <td class="status <%= lector.isAutenticacion() %>">
                    <%= lector.isAutenticacion() ? "Sí" : "No" %>
                </td>
                <td>
                    <form action="autenticarLector" method="post" style="display:inline;">
                        <input type="hidden" name="accion" value="autenticar">
                        <input type="hidden" name="idLector" value="<%= lector.getID() %>">
                        <button type="submit" <%= lector.isAutenticacion() ? "disabled" : "" %>>
                            Autenticar
                        </button>
                    </form>
                </td>

            </tr>
            <% }
            }
            } else { %>
            <tr>
                <td colspan="6">No hay lectores registrados</td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <div id="paginacion">
            <button id="anterior">Anterior</button>
            <button id="siguiente">Siguiente</button>
        </div>
    </div>
</main>
<script src="manejoLectores.js"></script>
</body>
</html>

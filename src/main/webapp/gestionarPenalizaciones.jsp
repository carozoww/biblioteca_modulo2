<%@ page import="models.Lector" %>
<%@ page import="models.Penalizacion" %>
<%@ page import="dao.PenalizacionDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Administrador" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String msg = request.getParameter("msg");
    PenalizacionDAO penadao = new PenalizacionDAO();
    List<Lector> lectores = (List<Lector>) request.getAttribute("lectores");
    String idLector = request.getParameter("idLector");
    Administrador admin = (Administrador) session.getAttribute("authAdmin");
    int idAdmin = (admin != null) ? admin.getId() : 0;
    if (admin == null) {
        response.sendRedirect("loginAdmin.jsp");
        return;
    }
%>
<html>
<head>
    <title>Penalizaciones</title>
    <style>
        <%@include file="./WEB-INF/estilo/estilosPenalizaciones.css" %>
    </style>
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
        <h2 class="page-title">Gestión de penalizaciones</h2>
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
            <a href="autenticarLector" class="btn btn-volver">Auntenticar lectores</a>
            <input type="text" class="search" id="buscar" placeholder="Buscar lector...">
        </div>
        <table id="tabla">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre completo</th>
                <th>Penalización</th>
                <th>Acción</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (lectores != null && !lectores.isEmpty()) {
                    for (Lector lector : lectores) {
                        Penalizacion pena = penadao.obtenerPenalizacionActivaPorUsuario(lector.getID());
            %>
            <tr>
                <td><%= lector.getID() %>
                </td>
                <td><%= lector.getNombre() %>
                </td>
                <td><%= (pena != null) ? pena.getMotivo() : "-" %>
                </td>
                <td>
                    <% if (pena != null && pena.isActiva()) { %>
                    <!-- quitar -->
                    <form action="${pageContext.request.contextPath}/penalizaciones" method="post" style="display:inline;">
                        <input type="hidden" name="accion" value="quitar">
                        <input type="hidden" name="idPena" value="<%= pena.getId_pena() %>">
                        <button type="submit" class="btn-cancelar">Quitar</button>
                    </form>
                    <% } else { %>
                    <!-- aplicar -->
                    <form action="${pageContext.request.contextPath}/aplicarPenalizaciones.jsp" method="get"
                          style="display:inline;">
                        <input type="hidden" name="idLector" value="<%= lector.getID() %>">
                        <button type="submit" class="btn-aceptar">Aplicar</button>
                    </form>
                    <% } %>
                </td>
            </tr>
            <%
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
<script>
    window.addEventListener('load', function() {
    const msg = document.querySelector('.msg');
    if (msg) {
        setTimeout(() => {
            msg.style.transition = 'opacity 0.8s ease';
            msg.style.opacity = '0';
            setTimeout(() => msg.remove(), 800);
        }, 3000);
    }
});</script>
</body>
</html>

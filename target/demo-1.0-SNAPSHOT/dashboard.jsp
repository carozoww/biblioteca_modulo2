<%@ page import="models.Lector" %>
<%
    Lector usuario = (Lector) session.getAttribute("authUser");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login-lector");
        return;
    }
%>

<h1>Bienvenido, <%= usuario.getNombre() %></h1>
<p>Cedula: <%= usuario.getCedula() %></p>

<a href="logout">Cerrar Sesión</a>
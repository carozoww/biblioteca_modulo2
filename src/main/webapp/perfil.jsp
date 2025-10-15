<%@ page import="models.Lector" %>
<%
    Lector usuario = (Lector) request.getAttribute("usuario");
%>

<h1>Perfil de Usuario</h1>
<p><strong>Nombre:</strong> <%= usuario.getNombre() %></p>
<p><strong>Cédula:</strong> <%= usuario.getCedula() %></p>
<p><strong>Email:</strong> <%= usuario.getCorreo() %></p>
<p><strong>Teléfono:</strong> <%= usuario.getTelefono() %></p>
<p><strong>Dirección:</strong> <%= usuario.getDireccion() %></p>
<p><strong>Fecha de nacimiento:</strong> <%= usuario.getFechaNac() %></p>

<a href="logout">Cerrar sesión</a>

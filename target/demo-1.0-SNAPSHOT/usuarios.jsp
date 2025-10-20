<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Usuarios</title>
</head>
<body>

<%@include file="WEB-INF/components/header.jsp"%>

<%
    HttpSession sesion = request.getSession();
    String usuarioLogueado = (String) sesion.getAttribute("logueado");

    List<String> nombres = (List<String>) request.getAttribute("listaLectores");
%>

<h1>Bienvenido <%= usuarioLogueado != null ? usuarioLogueado : "Invitado" %></h1>

<h2>${mensajeBienvenida}</h2>

<% if(nombres != null && !nombres.isEmpty()) { %>
<ul>
    <% for(String nombre : nombres) { %>
    <li><%= nombre %></li>
    <% } %>
</ul>
<% } else { %>
<p>No hay lectores registrados</p>
<% } %>

</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 10/9/2025
  Time: 20:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean exito = (boolean) request.getAttribute("exito");

    String nombreUsuario = (String) request.getParameter("nombreDeusuario");

%>

<%
    if(exito != true && exito){
%>

<h1>Bienvenido <%=nombreUsuario %></h1>

<%
    }else{
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="users" method="post">
        <label for="nombre">Nombre</label>
        <input type="text" name="nombre">

        <label for="apellido">Apellido</label>
        <input type="text" name="apellido">

        <label for="pass">Contrasenia</label>
        <input type="password" name="pass">

        <button type="submit">Click</button>
    </form>
</body>
</html>

<%
    }
%>
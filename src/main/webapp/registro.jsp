<p>Ingrese sus datos en el LOGIN</p>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Boolean exito = (Boolean) request.getAttribute("exito");

    String nombreUsuario = (String) request.getParameter("nombre");

%>

<%
    if(exito != null && exito){
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
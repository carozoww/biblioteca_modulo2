<p>Ingrese sus datos en el LOGIN</p>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Boolean exito = (Boolean) request.getAttribute("exito");

    String nombreUsuario = (String) request.getParameter("nombre");
    Boolean existeLector = false;

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
<form action="register" method="post">
    <% if(request.getParameter("existeLector") != null) {
        existeLector = (Boolean) request.getAttribute("existeLector");
    } %>

    <% if(existeLector && existeLector != null){ %>
    <h1>Ya existe un lector con esa cedula!!!!</h1>
    <% }%>

    <h1><%= existeLector %> </h1>



    <label for="nombre">Nombre</label>
    <input type="text" name="nombre">

    <br>

    <label for="cedula">cedula</label>
    <input type="number" name="ced">

    <br>

    <label for="tele">Telefono</label>
    <input type="text" name="tele">

    <br>

    <label for="tele">Dirección</label>
    <textarea for="dir" name="dir"></textarea>

    <br>

    <label for="fecha">Fecha</label>
    <input type="date" name="fecha">

    <br>

    <label for="correo">Correo Electronico</label>
    <input type="text" name="correo">

    <br>

    <label for="pass">Contraseña</label>
    <input type="password" name="pass">

    <br>

    <button type="submit">Click</button>
</form>


</body>
</html>

<%
    }
%>
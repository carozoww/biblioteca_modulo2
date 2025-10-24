<%@ page import="models.Lector" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%
    // Obtenemos el objeto usuario (tipo Lector) enviado por el servlet
    Lector usuario = (Lector) session.getAttribute("authUser");

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil de Usuario</title>

    <!-- Se enlaza el CSS general del sitio -->
    <style><%@include file="./WEB-INF/estilo/estiloPerfil.css"%></style>

</head>

<body>
<nav>
    <div id="logoynombre">
        <a class="dashboard" href="dashboard">Regresar</a>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <!-- Botón de cierre de sesión -->
        <a href="logout">Cerrar sesion</a>
    </div>
</nav>
<main>
    <!-- Contenedor principal del perfil -->
    <div class="perfil-container">
        <img src="imgs/iconouser.png" alt="Foto de usuario">

        <h1>Perfil de Usuario</h1>

        <!-- Muestra de los datos personales -->
        <div class="perfil-datos">
            <p><strong>Nombre:</strong> <%= usuario.getNombre() %></p>
            <p><strong>Cédula:</strong> <%= usuario.getCedula() %></p>
            <p><strong>Email:</strong> <%= usuario.getCorreo()%></p>
            <p><strong>Teléfono:</strong> <%= usuario.getTelefono() %></p>
            <p><strong>Dirección:</strong> <%= usuario.getDireccion() %></p>
            <p><strong>Fecha de nacimiento:</strong> <%= usuario.getFechaNac() %></p>

        </div>

         <!-- Contenedor de los botones -->
        <div class="botones-acciones">
           <!-- Botón de editar y eliminar de cuenta -->
           <a class="boton" href="editarPerfil?cedula=<%=usuario.getCedula()%>">Editar Perfil</a>

            <form action="eliminarLector" method="post" onsubmit="return confirmarEliminacion();">
                <input type="hidden" name="cedula" value="<%= usuario.getCedula() %>">
                <button type="submit" class="boton">Eliminar Perfil</button>
            </form>

        </div>

        <script>
            function confirmarEliminacion() {
                return confirm("⚠️ ¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.");
            }
        </script>

    </div>
</main>


<footer></footer>

</body>
</html>

<%@ page import="models.Lector" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Lector lector = (Lector) request.getAttribute("lector");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Perfil</title>
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
    <div class="perfil-container">
        <img src="imgs/iconouser.png" alt="Foto de usuario">
        <h1>Editar Perfil</h1>

        <form action="editarPerfil" method="post" class="form-perfil">
            <input type="hidden" name="cedula" value="<%= lector.getCedula() %>">
            <div class="perfil-datos">
                <label>Nombre:</label>
                <input type="text" name="nombre" value="<%= lector.getNombre() %>"><br>

                <label>Correo:</label>
                <input type="email" name="correo" value="<%= lector.getCorreo() %>"><br>

                <label>Teléfono:</label>
                <input type="text" name="telefono" value="<%= lector.getTelefono() %>"><br>

                <label>Dirección:</label>
                <input type="text" name="direccion" value="<%= lector.getDireccion() %>"><br>

                <label>Fecha de nacimiento:</label>
                <input type="date" name="fechaNac" value="<%= lector.getFechaNac() %>"><br><br>

                <label>Contraseña:</label>
                <div class="password-container">
                    <input id="contrasenia" type="password" name="contrasenia" value="" autocomplete="off">
                    <button type="button" class="toggle-password" onclick="togglePassword()" title="Mostrar contraseña">👁️</button>
                </div>
                <small>Dejala vacía si no querés cambiarla.</small>
            </div>

            <div class = "botones-acciones">
                <button type="submit" class="boton">Guardar cambios</button>
                <a href="perfil?cedula=<%= lector.getCedula() %>">Cancelar</a>
            </div>
        </form>
    </div>
</main>
<script>
    function togglePassword() {
        const input = document.getElementById('contrasenia');
        if (!input) return; // protección por si algo falla

        const isPassword = input.getAttribute('type') === 'password';
        input.setAttribute('type', isPassword ? 'text' : 'password');



        // alternar icono del botón
        const btn = document.querySelector('.toggle-password');
        if (btn) {
            btn.title = isPassword ? 'Ocultar contraseña' : 'Mostrar contraseña';
            btn.textContent = isPassword ? '🙈' : '👁️';
        }
    }
</script>
</body>
</html>

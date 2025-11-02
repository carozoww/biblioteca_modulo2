<%@ page import="models.Lector" %><%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 22/10/2025
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Lector usuario = (Lector) session.getAttribute("authUser");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login-lector");
        return;
    }
%>
<html>
<head>
    <title>Title</title>
    <style><%@include file="./WEB-INF/estilo/form.css"%></style>

</head>
<body>
<div>

    <header>
        <div id="logoynombre">
            <img src="imgs/logo.jpg" width="100px" height="100px">
            <h1>Biblio-Tech-a</h1>
        </div>
        <div id="elementos_derecha">
            <a href="">Cuenta</a>
            <a href="">Cerrar sesion</a>
        </div>
    </header>
    <main>
        <div id="container-sugerencia">
            <form action="sugerencia" method="POST">
            <div id="contenido">
                <h1>Sugerencia</h1>
                <p>¡Cuentanos sobre el libro o libros, que estas deseando poder ver en la biblioteca!</p>
                <textarea name="areatexto" id="areatexto" maxlength="250" required></textarea>
            </div>
            <div class="contador">
                <span id="contador">250</span> caracteres restantes
            </div>
            <div id="botones">

                <button type="button"  onclick="window.location.href='dashboard'">Volver</button>
                <button type="submit">Enviar Sugerencia</button>

            </div>
            </form>
        </div>

    </main>
</div>

<script>
    var limite = 250;
    const contador = document.getElementById('contador');
    const textoarea = document.getElementById('areatexto');
    textoarea.addEventListener('input', function(){
        const caracteres = limite - this.value.length;
        console.log(caracteres);
        contador.textContent = caracteres;
        if(limite=== 0){
            return;
        }
    })

</script>
</body>
</html>

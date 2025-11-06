<%@ page import="models.Administrador" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%


%>
<html>
<head>
    <title>Aplicar penalizaciones</title>
    <style>
        <%@include file="./WEB-INF/estilo/estilosAplicarPenas.css" %>
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
<main>

    <h2>Aplicar penalización</h2>

    <form action="${pageContext.request.contextPath}/penalizaciones" method="post">
        <input type="hidden" name="accion" value="aplicar">
        <input type="hidden" name="idLector" value="<%= request.getParameter("idLector") %>">

        <label>Duración (días):</label>
        <input type="number" name="duracion" required>

        <label>Motivo:</label>
        <input type="text" name="motivo" maxlength="255" required>

        <button type="submit">Aplicar</button>
    </form>

    <a href="penalizaciones" class="volver">Volver a la lista</a>
</main>

</body>
</html>

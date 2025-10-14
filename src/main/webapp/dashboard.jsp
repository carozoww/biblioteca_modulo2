<%@ page import="models.Lector" %>
<%
    Lector usuario = (Lector) session.getAttribute("authUser");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login-lector");
        return;
    }
%>
<html>


<header>

</header>
<body>
    <div id="header">
        <div>
            <a>
                <img src="logo.jpg" width="100px" height="100px">
            </a>
            <h1>Biblio-Tech-a</h1>
        </div>

    </div>
    <h1>Bienvenido, <%= usuario.getNombre() %></h1>
    <p>Cedula: <%= usuario.getCedula() %></p>



    <a href="logout">Cerrar Sesión</a>
</body>



</html>
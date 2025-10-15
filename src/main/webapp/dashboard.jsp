<%@ page import="models.Lector" %>
<%
    Lector usuario = (Lector) session.getAttribute("authUser");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login-lector");
        return;
    }
%>
<html>


<head>
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>

</head>

<body>
<nav>
    <div id="logoynombre">
        <button onclick="abrirBarra()" id="botton-barra"> click</button>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="">Cuenta</a>
        <a href="logout">Cerrar sesion</a>
    </div>
</nav>
<aside id="sidebar">
    <div id="columna_contenido">
        <div>
            <img src="imgs/iconouser.png" alt="" width="50px" height="50px">
            <a href="">Usuarios</a>
        </div>
        <div>
            <img src="imgs/resenia.png" alt="" width="50px" height="50px">
            <a href="">Reseñas</a>
        </div>
        <div>
            <img src="imgs/prestamo.png" alt="" width="50px" height="50px">
            <a href="">Prestamos</a>
        </div>
        <div>
            <img src="imgs/room.png" alt="" width="50px" height="50px">
            <a href="">Salas</a>
        </div>
    </div>
</aside>
<main>
    <h1>Libros</h1>
    <div class="contenedor-libros">
        <div class="libro">
            <h1>primer libro</h1>
            <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
            <p>Este es el mejor libro de todos los libros </p>
            <button>ver mas</button>
        </div>
        <div class="libro">
            <h1>segundo libro</h1>
            <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
            <p>hdwq8dgwqd9gwqd</p>
            <button>ver mas</button>
        </div>
        <div class="libro">
            <h1>tercer libro</h1>
            <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
            <p>hdwq8dgwqd9gwqd</p>
            <button>ver mas</button>
        </div>
        <div class="libro">
            <h1>tercer libro</h1>
            <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
            <p>hdwq8dgwqd9gwqd</p>
            <button>ver mas</button>
        </div>
        <div class="libro">
            <h1>tercer libro</h1>
            <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
            <p>hdwq8dgwqd9gwqd</p>
            <button>ver mas</button>
        </div>
        <div class="libro">
            <h1>tercer libro</h1>
            <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
            <p>hdwq8dgwqd9gwqd</p>
            <button>ver mas</button>
        </div>
    </div>
</main>
<footer>pie de pagina</footer>
<script>
    const sidebar = document.getElementById("sidebar");

    function abrirBarra(){
        sidebar.classList.toggle('show')
    }
</script>
</body>



</html>
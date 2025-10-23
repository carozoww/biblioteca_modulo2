<%@ page import="models.Lector" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Autor" %>
<%@ page import="models.Editorial" %>
<%
    List<String> generos = (List<String>) request.getAttribute("listaGeneros");
    List<Autor> autores1 = (List<Autor>) request.getAttribute("listaAutores2");
    List<Editorial> editoriales = (List<Editorial>) request.getAttribute("listaEditoriales");

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
        <a href="perfil">Cuenta</a>
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
            <a href="prestamos?accion=catalogo">Prestamos</a>
        </div>
        <div>
            <img src="imgs/room.png" alt="" width="50px" height="50px">
            <a href="">Salas</a>
        </div>
    </div>
</aside>
<main>
    <div id="filtros">
        <h1>Filtrado</h1>
        <div class="select-filtro">
            <select id="lista">
                <option value="1">Todos los Libros</option>
                <% for(String genero : generos){ %>
                <option value="<%= genero %>"> <%= genero %></option>
                <%} %>
            </select>
            <button onclick="enviarGenero()" id="botonGenero">click</button>
        </div>
    </div>
    <div id="buscador-seccion">
        <h1>Buscador</h1>
        <label for="genero">Titulo</label>
        <input type="search" id="inputBuscador">
        <div class="select-filtro">
            <p>Seleccion de filtro para busqueda</p>
            <select name="" id="selectTipo">
                <option value="Titulo">Titulo</option>
                <option value="Editorial" selected>Editorial</option>
                <option value="Autor">Autor</option>
            </select>
        </div>
    </div>

    <h1>Libros</h1>
    <div class="contenedor-libros" id="containerLibro">

    </div>
    <div id="paginacion">
        <button id="anterior">Anterior</button>
        <button id="siguiente">Siguiente</button>
    </div>

</main>
<footer>
    <div id ="pie-pagina">
        <h2>¿No encuentra un libro en nuestro catálogo? Aceptamos sugerencias por medio del siguiente formulario</h2>
        <form action="sugerencia">
            <button type="submit"> Acceder Formulario</button>
        </form>
    </div>
</footer>
<script src="libroapp.js"></script>
<script>

    const sidebar = document.getElementById("sidebar");

    function abrirBarra(){
        sidebar.classList.toggle('show')
    }
    async function enviarGenero(){
        const genero = document.getElementById('lista').value;
        if(genero == "1"){
            offset = 0;
            cargarLibros();
            return;
        }

        fetch('librosGenero', {
            method: 'POST',
            headers:{
                'Content-Type':'application/x-www-form-urlencoded',
            },
            body: 'genero=' + encodeURIComponent(genero)
        })
            .then(async response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor');
                }
                librosFiltrados = await response.json();
                offset = 0;
                mostrarLibros();


            })
    }



</script>
</body>



</html>
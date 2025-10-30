<%@ page import="models.Lector" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Autor" %>
<%@ page import="models.Editorial" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%
    List<String> generos = (List<String>) request.getAttribute("listaGeneros");

    Lector usuario = (Lector) session.getAttribute("authUser");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login-lector");
        return;
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>
    <style><%@include file="./WEB-INF/estilo/estiloReview.css"%></style>
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
        <a href="logout">Cerrar sesión</a>
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
            <a href="#" id="linkResenas">Reseñas</a>
        </div>
        <div>
            <img src="imgs/prestamo.png" alt="" width="50px" height="50px">
            <a href="prestamos?accion=listar">Préstamos</a>
        </div>
        <div>
            <img src="imgs/room.png" alt="" width="50px" height="50px">
            <a href="salas">Salas</a>
        </div>
    </div>
</aside>

<main>

    <div id="mainContent"></div>

    <div id="seccion-filtros">
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
                    <option value="Titulo" selected>Titulo</option>
                    <option value="Editorial">Editorial</option>
                    <option value="Autor">Autor</option>
                </select>
            </div>
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
    <div id="pie-pagina">
        <h2>¿No encuentra un libro en nuestro catálogo? Aceptamos sugerencias por medio del siguiente formulario</h2>
        <form action="sugerencia">
            <button type="submit">Acceder Formulario</button>
        </form>
    </div>
</footer>

<script src="libroapp.js"></script>
<script>
    const sidebar = document.getElementById("sidebar");
    function abrirBarra() {
        sidebar.classList.toggle('show')
    }

    async function enviarGenero() {
        const genero = document.getElementById('lista').value;
        if (genero == "1") {
            offset = 0;
            cargarLibros();
            return;
        }

        fetch('librosGenero', {
            method: 'POST',
            headers:{ 'Content-Type':'application/x-www-form-urlencoded' },
            body: 'genero=' + encodeURIComponent(genero)
        })
            .then(async response => {
                if (!response.ok) throw new Error('Error en la respuesta del servidor');
                librosFiltrados = await response.json();
                offset = 0;
                mostrarLibros();
            })
    }
</script>

<!-- Carga dinámica de reseñas -->
<script>
    const mainContent = document.getElementById('mainContent');
    const linkResenas = document.getElementById('linkResenas');

    linkResenas.addEventListener('click', async (e) => {
        e.preventDefault();


        const contenidoLibros = document.getElementById('contenidoLibros');
        if (contenidoLibros) contenidoLibros.style.display = 'none';


        mainContent.innerHTML = '';

        try {
            const res = await fetch('review.jsp');
            let html = await res.text();

            const bodyMatch = html.match(/<body[^>]*>([\s\S]*?)<\/body>/i);
            if (!bodyMatch) throw new Error("No se pudo extraer el body de review.jsp");
            mainContent.innerHTML = bodyMatch[1];


            if (!document.getElementById('estiloReview')) {
                const link = document.createElement('link');
                link.id = 'estiloReview';
                link.rel = 'stylesheet';
                link.href = './WEB-INF/estilo/estiloReview.css';
                document.head.appendChild(link);
            }

            const script = document.createElement('script');
            script.src = 'reviewApp.js';
            script.type = 'module';
            document.body.appendChild(script);

        } catch (err) {
            console.error('Error cargando reseñas:', err);
            mainContent.innerHTML = '<p>Error al cargar las reseñas.</p>';
        }
    });
</script>

</body>
</html>

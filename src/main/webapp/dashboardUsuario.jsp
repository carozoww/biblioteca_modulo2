<%@ page import="models.Lector" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Autor" %>
<%@ page import="models.Editorial" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%
    List<String> generos = (List<String>) request.getAttribute("listaGeneros");


%>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>
    <style><%@include file="./WEB-INF/estilo/estiloReview.css"%></style>
    <title>Pagina Principal</title>
</head>

<body>
<nav>
    <div id="logoynombre">
        <button onclick="abrirBarra()" id="botton-barra"> click</button>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="login-lector">Iniciar sesión</a>
    </div>
</nav>

<aside id="sidebar">
    <div id="columna_contenido">
        <div>
            <img src="imgs/resenia.png" alt="" width="50px" height="50px">
            <a href="" id="resenia">Reseñas</a>
        </div>
        <div>
            <img src="imgs/prestamo.png" alt="" width="50px" height="50px">
            <a href="" id="prestamo">Prestamos</a>
        </div>
        <div id="mensaje-error">

        </div>
        <div id="link">

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

</footer>

<script src="libroapp.js"></script>
<script>
    const reseniA = document.getElementById('resenia');
    const seccionLink = document.getElementById('link');
    const PrestamoA = document.getElementById('prestamo');


    reseniA.addEventListener('click', (e) =>{
        e.preventDefault();
        const error = document.getElementById('mensaje-error');
        const mensaje = document.createElement('p');
        error.innerHTML = "";
        seccionLink.innerHTML = "";
        mensaje.innerText="Necesitas iniciar sesión para ver las reseñas de la comunidad!!!";
        mensaje.style.fontSize = "24px";
        const linkIS = document.createElement('a');
        linkIS.innerText="Iniciar Sesión";
        linkIS.href="login-lector";

        error.appendChild(mensaje);
        seccionLink.appendChild(linkIS);

    })

    PrestamoA.addEventListener('click',(e) =>{
        e.preventDefault();
        const error = document.getElementById('mensaje-error');
        const mensaje = document.createElement('p');
        error.innerHTML = "";
        seccionLink.innerHTML = "";
        mensaje.innerText="Necesitas iniciar sesión para realizar un prestamo de libro!!!";
        mensaje.style.fontSize = "24px";
        const linkIS = document.createElement('a');
        linkIS.innerText="Iniciar Sesión";
        linkIS.href="login-lector";

        error.appendChild(mensaje);
        seccionLink.appendChild(linkIS);


    })


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


</body>
</html>

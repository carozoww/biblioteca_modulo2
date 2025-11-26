<%@ page import="models.Lector" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Autor" %>
<%@ page import="models.Editorial" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%
    List<String> generos = new ArrayList<>();

    if(request.getAttribute("listaGeneros") != null){
        generos = (List<String>) request.getAttribute("listaGeneros");
    }



%>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>
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
        <a href="login-admin">Iniciar sesión Admin</a>
    </div>
</nav>

<aside id="sidebar">
    <div id="columna_contenido">
        <div class="sidebar-item">
            <i class="fa-solid fa-star"></i>
            <a href="" id="resenia">Reseñas</a>
        </div>
        <div class="sidebar-item">
            <i class="fa-solid fa-handshake fa-2x"></i>
            <a href="" id="prestamo">Prestamos</a>
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
    // ================== MODAL ==================
    document.addEventListener('DOMContentLoaded', () => {
        const modal = document.getElementById('modal-error');
        const modalMensaje = document.getElementById('modal-mensaje');
        const modalCerrar = document.querySelector('.modalError-close');

        function mostrarModal(mensaje) {
            modalMensaje.innerText = mensaje;
            modal.style.display = 'block';
        }

        modalCerrar.addEventListener('click', () => modal.style.display = 'none');
        window.addEventListener('click', (e) => { if (e.target == modal) modal.style.display = 'none'; });

        const linksProtegidos = [
            { id: 'resenia', mensaje: 'Necesitas iniciar sesión para ver las reseñas de la comunidad!!!' },
            { id: 'prestamo', mensaje: 'Necesitas iniciar sesión para realizar un prestamo de libro!!!' }
        ];

        linksProtegidos.forEach(link => {
            const elem = document.getElementById(link.id);
            if (elem) {
                elem.addEventListener('click', e => {
                    e.preventDefault();
                    mostrarModal(link.mensaje);
                });
            }
        });
    });

    // ================== SIDEBAR TOGGLE ==================
    const sidebar = document.getElementById("sidebar");
    function abrirBarra() {
        sidebar.classList.toggle('show');
    }

    // ================== FILTRADO DE LIBROS ==================
    async function enviarGenero() {
        const genero = document.getElementById('lista').value;
        if (genero == "1") {
            offset = 0;
            cargarLibros();
            return;
        }

        try {
            const response = await fetch('librosGenero', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'genero=' + encodeURIComponent(genero)
            });

            if (!response.ok) throw new Error('Error en la respuesta del servidor');

            librosFiltrados = await response.json();
            offset = 0;
            mostrarLibros();
        } catch (error) {
            console.error(error);
        }
    }

</script>


<div id="modal-error" class="modalError">
    <div class="modalError-content">
        <span class="modalError-close">&times;</span>
        <h2 id="modal-error-title">Atención</h2>
        <p id="modal-mensaje">Debés iniciar sesión para continuar.</p>
        <a id="modal-link" href="login-lector" class="button">Iniciar Sesión</a>
    </div>
</div>

</body>
</html>

<%@ page import="models.Administrador" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 25/10/2025
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  List<String> generos = (List<String>) request.getAttribute("listaGeneros");
    Administrador admin = (Administrador) session.getAttribute("authAdmin");
    String error = (String) session.getAttribute("error");
    if(error != null){
        session.removeAttribute("error");
    }


    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/login-admin");
        return;
    }%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
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
    <div id="elementos_medio">
        <a href="generosAdmin">Generos</a>
        <a href="autoresAdmin">Autores</a>
        <a href="editorialesAdmin">Editoriales</a>
        <a href="administradorSalas">Salas</a>
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
            <a href="autenticarLector">Lectores</a>
        </div>
        <div>
            <img src="imgs/resenia.png" alt="" width="50px" height="50px">
            <a href="#" id="linkResenas">Reseñas</a>
        </div>
        <div>
            <img src="imgs/prestamo.png" alt="" width="50px" height="50px">
            <a href="prestamosAdmin">Préstamos</a>
        </div>
        <div>
            <img src="imgs/reserva.png" alt="" width="50px" height="50px">
            <a href="reservasAdmin">Reservas</a>
        </div>
    </div>
</aside>
<main>

    <div id="mainContent">

        <div id="seccion-filtros">
            <div id="filtros">
                <h1>Filtrado</h1>
                <div class="select-filtro">
                    <p>Filtrar por Genero</p>
                    <select name="" id="lista">
                        <option value="1">Todos los Libros</option>
                        <% for(String genero : generos){ %>
                        <option value="<%= genero %>"> <%= genero %></option>
                        <%} %>
                    </select>
                    <button onclick="enviarGenero()" id="botonGenero">Filtrar</button>
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
                        <option value="Editorial" >Editorial</option>
                        <option value="Autor">Autor</option>
                    </select>
                </div>
            </div>
        </div>
        <div id="boton-alta">
            <button onclick="window.location.href='alta-libro-servlet'">Crear Libro</button>
            <button onclick="window.location.href='alta-lector-servlet'">Registrar Lector</button>
        </div>

        <h1 id="tituloLibro">Libros</h1>
        <div class="contenedor-libros" id="containerLibro">

        </div>
        <div id="paginacion">
            <button id="anterior">Anterior</button>
            <button id="siguiente">Siguiente</button>
        </div>
    </div>
</main>
<footer>

</footer>
</body>
<script src="libroAdmin.js"></script>
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
<script>
    const linkResenas = document.getElementById('linkResenas');
    linkResenas.addEventListener('click', async (e) => {
        e.preventDefault();
        const mainContent = document.getElementById('mainContent');

        mainContent.innerHTML = '';

        const res = await fetch('ReviewAdmin.jsp');
        const html = await res.text();
        const bodyMatch = html.match(/<body[^>]*>([\s\S]*?)<\/body>/i);
        if (bodyMatch) {
            mainContent.innerHTML = bodyMatch[1];
        }


        const script = document.createElement('script');
        script.src = 'reviewAdmin.js';
        script.type = 'module';
        document.body.appendChild(script);
    });

</script>

</html>

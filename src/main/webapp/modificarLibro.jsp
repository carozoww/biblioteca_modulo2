<%@ page import="models.Libro" %>
<%@ page import="models.Autor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Genero" %><%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 30/10/2025
  Time: 23:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Libro libro = null;
    List<Autor> autores = new ArrayList<>();
    List<Genero> generos = new ArrayList<>();
    if(request.getAttribute("libro") != null){
        libro = (Libro) request.getAttribute("libro");
    }
    if(request.getAttribute("autores") != null){
        autores = (List<Autor>) request.getAttribute("autores");
    }
    if(request.getAttribute("generos") != null){
        generos = (List<Genero>) request.getAttribute("generos");
    }

%>
<html>
<head>
    <title>Edición de Libro</title>
    <style><%@include file="./WEB-INF/estilo/formLibro.css"%></style>
</head>
<body>
<header>
    <div id="logoynombre">
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
    </div>
</header>
<div id="seccion-form">
    <form  method="POST" id="formulario" enctype="multipart/form-data">
        <h1>Modificar Libro</h1>
        <div class="container-campo">
            <label for="titulo">Titulo Actual:</label>
            <input type="text" name="titulo" maxlength="29" value="<%=libro.getTitulo()%>" required>
            <input type="hidden" value="<%=libro.getIdLibro()%>" name="id_libro">
        </div>
        <div class="container-campo">
            <label for="isbn">ISBN Actual:</label>
            <input type="text" name="isbn"  maxlength="16" value="<%=libro.getIsbn()%>" required>
        </div>
        <div class="container-campo">
            <label for="fecha">Fecha de Publicación Actual:</label>
            <input type="date" name="fecha" value="<%=libro.getFechaPublicacion()%>" required >
        </div>
        <div class="container-campo">
            <label for="editorial">Editorial Actual:</label>
            <div id="buscar-editorial">
                <div id="seccion-buscador-ed">
                    <input type="search" name="idEditorial" id="edSearch" value="<%=libro.getEditorial()%>" required>
                    <input type="hidden" name="editorialid" id="idEditorial" value="<%=libro.getIdEditorial()%>">
                </div>
                <div id="listado-editoriales">

                </div>
            </div>
        </div>
        <div class="container-campo">
            <label for="">Autor:</label>
            <div id="buscar-editorial">
                <div id="seccion-buscador-ed">
                    <input type="search" name="idAutor" id="auSearch" >
                    <input type="hidden" name="autorid" id="idAutor">
                </div>
                <div id="listado-autores">

                </div>
            </div>
        </div>
        <div class="container-campo">
            <label> Autores Asignados: </label>
            <div >
                <ul id="autores-seleccion">
                    <% for(Autor autor : autores){%>
                    <li><%=autor.getNombre()%> <%=autor.getApellido()%><button value="<%=autor.getId_autor()%>" type="button" class="del">Eliminar</button></li>
                    <%} %>
                </ul>
            </div>

        </div>

        <div class="container-campo">
            <label for="">Genero:</label>
            <div id="buscar-editorial">
                <div id="seccion-buscador-ed">
                    <input type="search" name="idGenero" id="genSearch">
                    <input type="hidden" name="generoid" id="idGenero">
                </div>
                <div id="listado-generos">

                </div>
            </div>
        </div>
        <div class="container-campo">
            <label> Generos Asignados: </label>
            <div >
                <ul id="generos-seleccion">
                    <% for(Genero genero : generos){%>
                    <li><%=genero.getNombre()%><button value="<%=genero.getId_genero()%>" type="button" class="del">Eliminar</button></li>
                    <%} %>
                </ul>
            </div>

        </div>
        <div class="container-campo">
            <label for="sinopsis">Sinopsis Actual:</label>
            <textarea name="sinopsis" id="" maxlength="254"><%=libro.getSinopsis()%></textarea>
        </div>
        <div class="container-campo">
            <label for="numpaginas">Número de páginas actual:</label>
            <input type="number" name="numpaginas" value="<%=libro.getNumpaginas()%>" required>
        </div>
        <div class="container-img">
            <label for="">Imagen del libro actual:</label>
            <br>
            <img src="<%=libro.getImagen_url()%>"  width="150" height="250">

            <input type="file" id="imagenInput" name="image" accept="image/*" >
            <input type="hidden" value="<%=libro.getImagen_url()%>" name="imagenVieja">
        </div>
        <div id="botones">
            <button type="button" onclick="window.location.href='dashboardAdmin'"> Cancelar</button>
            <button id="submitBtn" type="submit">Registrar Libro</button>
        </div>
    </form>
    <script src="altaLibro.js"></script>
    <script>
        <%for(Autor autor : autores){ %>
        autoresSeleccionados.push("<%=autor.getId_autor()%>")
        <%}%>
        <%for(Genero genero : generos){ %>
        generosSeleccionados.push("<%=genero.getId_genero()%>")
        <%}%>
        console.log(autoresSeleccionados);


        document.getElementById('formulario').addEventListener('submit', async function(e) {
            e.preventDefault();

            const formData = new FormData(this);
            const submitBtn = document.getElementById('submitBtn');
            const originalText = submitBtn.textContent;

            try {
                submitBtn.disabled = true;
                submitBtn.textContent = 'Subiendo...';

                const response = await fetch('modificar-libro-servlet', {
                    method: 'POST',
                    body: formData
                });

                const result = await response.json();

                if (result.success) {
                    window.location.href = '${pageContext.request.contextPath}/dashboardAdmin';
                } else {
                    throw new Error(result.message);
                }
            } catch (error) {
                console.error('Error:', error);
            }
        });
    </script>
</div>
</body>
</html>

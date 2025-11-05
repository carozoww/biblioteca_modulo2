<%--
  Created by IntelliJ IDEA.
  User: cardo
  Date: 27/10/2025
  Time: 15:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
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
        <h1>Registrar Libro</h1>
        <div class="container-campo">
            <label for="titulo">Titulo:</label>
            <input type="text" name="titulo" maxlength="29" required>
        </div>
        <div class="container-campo">
            <label for="isbn">ISBN:</label>
            <input type="text" name="isbn" maxlength="16" required>
        </div>
        <div class="container-campo">
            <label for="fecha">Fecha de Publicación:</label>
            <input type="date" name="fecha" required>
        </div>
        <div class="container-campo">
            <label for="editorial">Editorial:</label>
            <div id="buscar-editorial">
                <div id="seccion-buscador-ed">
                    <input type="search" name="idEditorial" id="edSearch" required>
                    <input type="hidden" name="editorialid" id="idEditorial">
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

                </ul>
            </div>

        </div>

        <div class="container-campo">
            <label for="sinopsis">Sinopsis:</label>
            <textarea name="sinopsis" id="" maxlength="254"></textarea>
        </div>
        <div class="container-campo">
            <label for="numpaginas">Número de páginas:</label>
            <input type="number" name="numpaginas" required>
        </div>
        <div class="container-img">
            <label for="">Imagen del libro:</label>
            <input type="file" id="imagenInput" name="image" accept="image/*" required>
        </div>
        <div id="botones">
            <button type="button" onclick="window.location.href='dashboardAdmin'">Cancelar</button>
            <button id="submitBtn" type="submit">Registrar Libro</button>
        </div>
    </form>
    <script src="altaLibro.js"></script>
    <script>
        document.getElementById('formulario').addEventListener('submit', async function(e) {
            e.preventDefault();

            const formData = new FormData(this);
            const submitBtn = document.getElementById('submitBtn');
            const originalText = submitBtn.textContent;

            try {
                submitBtn.disabled = true;
                submitBtn.textContent = 'Subiendo...';

                const response = await fetch('alta-libro-servlet', {
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Review" %>
<%@ page import="models.Libro" %>
<%@ page import="dao.LibroDAO" %>

<html>
<head>
    <title>Reseñas de libros</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        <%@ include file="./WEB-INF/estilo/estiloReview.css" %>
        body { font-family: Arial, sans-serif; }
    </style>
</head>
<body>

<h1>Reseñas</h1>

<button id="nuevaReviewBtn">Nueva Reseña</button>
<div id="reviewsList"></div>

<div id="paginacion">
    <button id="prevPage">Anterior</button>
    <span id="paginaActual">1</span>
    <button id="nextPage">Siguiente</button>
</div>

<!-- Modal para crear/editar review -->
<div id="reviewModal" class="modal">
    <div class="modal-content">
        <span id="reviewModalClose" class="modal-close">&times;</span>
        <h3>Nueva reseña</h3>

        <label>Libro:</label><br>
        <div class="input-container">
            <input type="text" id="inputLibro" placeholder="Escribe el título..." autocomplete="off">
            <div id="sugerenciasLibros" class="sugerencias"></div>
        </div>

        <label>Valoración: <input type="number" id="valoracion" min="1" max="5"></label><br>
        <label>Reseña:</label><br>
        <textarea id="resenia" rows="4" cols="40" placeholder="Escribe tu reseña..."></textarea><br>
        <button id="guardarReview">Guardar</button>
    </div>
</div>

<!-- Modal para mostrar detalles de review -->
<div id="detalleModal" class="modal">
    <div class="modal-content">
        <span id="detalleModalClose" class="modal-close">&times;</span>

        <div class="modal-top">
            <div class="libro-info">
                <img id="modal-libro-img" class="libro-img" alt="Imagen del libro">
                <div class="libro-titulo-stars">
                    <h3 id="modal-libro"></h3>
                    <div id="modal-stars"></div>
                    <div class="lector-info">
                        <img id="modal-lector-img" class="lector-img" alt="Imagen del lector">
                        <p id="modal-lector"></p>
                    </div>
                </div>
            </div>

        </div>

        <p id="modal-resenia"></p>

        <div id="modal-like-section">
            <i class="fas fa-thumbs-up" id="modal-like-btn" data-id="..."></i>
            <span id="modal-like-count" data-id="...">0</span>
            <i class="fas fa-comment" style="margin-left:10px;"></i>
            <span id="modal-comment-count">0</span>
        </div>

        <!-- Caja para nuevo comentario -->
        <div id="nuevoComentario">
            <textarea id="inputComentario" rows="2" placeholder="Escribí un comentario..." ></textarea><br>
            <button id="btnAgregarComentario">Comentar</button>
        </div>

        <!-- Sección de comentarios -->
        <div id="comentariosList" class="comentarios-list"></div>
    </div>
</div>

</body>
</html>

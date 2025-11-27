<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<div id="botonesHeader">
<h1>Reseñas</h1>
<a href="dashboardAdmin" class="btn btn-volver">Volver al inicio</a>
</div>

<div id="reviewsList"></div>

<div id="paginacion">
    <button id="prevPage">Anterior</button>
    <span id="paginaActual">1</span>
    <button id="nextPage">Siguiente</button>
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
        <div id="modal-contadores"></div>
        <!-- Sección de comentarios -->
        <div id="comentariosList" class="comentarios-list"></div>
    </div>
</div>

<script src="reviewAdmin.js"></script>
</body>
</html>

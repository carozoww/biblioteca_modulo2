let pagina = 1;
let cargandoDetalle = false;

// Generar estrellas
function generarEstrellas(valor) {
    let stars = '';
    for (let i = 1; i <= 5; i++) {
        stars += i <= valor ? '<i class="fas fa-star"></i>' : '<i class="far fa-star"></i>';
    }
    return `<span class="stars">${stars}</span>`;
}

// CARGAR REVIEWS PAGINADAS
async function cargarReviews() {
    const container = document.getElementById('reviewsList');
    container.innerHTML = '';

    const res = await fetch(`reviewAdmin?accion=listar&pagina=${pagina}`);
    const data = await res.json();

    data.forEach(async r => {
        const libroImg = r.imagenLibro || 'imgs/libro.jpg';
        const lectorImg = r.imagenLector || 'imgs/iconouser.png';
        let resumenResenia = r.resenia ? r.resenia.slice(0, 90) : '';
        if (r.resenia && r.resenia.length > 90) resumenResenia += '...';

        const div = document.createElement('div');
        div.className = 'review-card';
        div.innerHTML = `
            <div class="card-header">
                <img src="${libroImg}" class="libro-img" alt="Libro">
                <div>
                    <h3>${r.nombreLibro}</h3>
                    <div style="display:flex; align-items:center; gap:5px; margin-top:4px;">
                        <img src="${lectorImg}" class="lector-img" alt="Lector">
                        <span>${r.nombreLector}</span>
                    </div>
                    ${generarEstrellas(r.valoracion)}
                </div>
            </div>
            <p style="margin-top:8px; color:#555; font-size:0.9em;">${resumenResenia}</p>
            <div class="like-section" style="margin-top:5px; display:flex; gap:10px; align-items:center;">
                <div>
                    <i class="fas fa-thumbs-up like-btn" style="font-size:14px;"></i>
                    <span class="like-count" id="like-count-${r.idReview}" style="font-size:0.85em;">${r.likes || 0}</span>
                </div>
                <div>
                    <i class="fas fa-comment"></i>
                    <span class="comment-count" id="comment-count-${r.idReview}" style="font-size:0.85em;">0</span>
                </div>
            </div>
            <button class="detalle-btn" data-id="${r.idReview}" style="margin-top:8px;">Ver detalle</button>
            <button class="eliminar-btn" data-id="${r.idReview}" style="margin-top:4px;">Eliminar</button>
        `;
        container.appendChild(div);

        try {
            const resComentarios = await fetch(`reviewAdmin?accion=listarComentarios&id_review=${r.idReview}`);
            const comentarios = await resComentarios.json();
            const contador = comentarios.length;
            const spanComentario = document.getElementById(`comment-count-${r.idReview}`);
            if (spanComentario) spanComentario.textContent = contador;
        } catch (err) {
            console.error(`Error al contar comentarios de review ${r.idReview}:`, err);
        }
    });

    document.getElementById('paginaActual').innerText = pagina;
}

// VER DETALLE DE REVIEW
async function verDetalle(idReview) {
    if (cargandoDetalle) return;
    cargandoDetalle = true;

    try {
        const res = await fetch(`reviewAdmin?accion=detalle&id_review=${idReview}`);
        const r = await res.json();

        const libroImg = r.imagenLibro || 'imgs/libro.jpg';
        const lectorImg = r.imagenLector || 'imgs/iconouser.png';

        const libroEl = document.getElementById('modal-libro-img');
        libroEl.src = libroImg;
        libroEl.onerror = () => { libroEl.src = 'imgs/libro.jpg'; };

        const lectorEl = document.getElementById('modal-lector-img');
        lectorEl.src = lectorImg;
        lectorEl.onerror = () => { lectorEl.src = 'imgs/iconouser.png'; };

        document.getElementById('modal-libro').innerText = r.nombreLibro;
        document.getElementById('modal-lector').innerText = `Lector: ${r.nombreLector}`;
        document.getElementById('modal-stars').innerHTML = generarEstrellas(r.valoracion);
        document.getElementById('modal-resenia').innerText = r.resenia || "Sin comentario";

        // Guardar el idReview en el modal para recargar comentarios después de eliminar uno
        const modal = document.getElementById('detalleModal');
        modal.dataset.idReview = idReview;

        // LIMPIAR LA SECCIÓN ANTES DE AGREGAR CONTADORES NUEVOS
        const contadores = document.getElementById('modal-contadores');
        if (contadores) contadores.innerHTML = '';

        // contadores de likes y comentarios
        const contadoresHTML = `
            <div style="margin-top:8px; display:flex; gap:15px; align-items:center; font-size:0.9em;">
                <span><i class="fas fa-thumbs-up"></i> <span id="modal-like-count">${r.likes || 0}</span></span>
                <span><i class="fas fa-comment"></i> <span id="modal-comment-count">${r.comentarios || 0}</span></span>
            </div>
        `;
        contadores.innerHTML = contadoresHTML;

        await cargarComentarios(idReview);

        modal.style.display = 'flex';
    } catch (err) {
        console.error(err);
        alert("Error al cargar detalles de la review");
    } finally {
        cargandoDetalle = false;
    }
}

// COMENTARIOS
async function cargarComentarios(idReview) {
    const cont = document.getElementById('comentariosList');
    cont.innerHTML = '<p style="color:#666;">Cargando comentarios...</p>';

    try {
        const res = await fetch(`reviewAdmin?accion=listarComentarios&id_review=${idReview}`);
        const comentarios = await res.json();

        if (!comentarios || comentarios.length === 0) {
            cont.innerHTML = '<p style="color:#777;">No hay comentarios aún.</p>';
        } else {
            cont.innerHTML = comentarios.map(c => `
                <div class="comentario-item" style="display:flex; gap:10px; margin-bottom:10px; align-items:flex-start; position:relative;">
                    <img src="${c.imagenLector || 'imgs/iconouser.png'}" class="lector-img" style="width:35px; height:35px;">
                    <div>
                        <strong>${c.nombreLector}</strong>
                        <span style="font-size:0.8em; color:#888;"> (${new Date(c.fecha).toLocaleString()})</span>
                        <p style="margin:5px 0; color:#333;">${c.contenido}</p>
                    </div>
                    <button class="borrar-comentario-btn" data-id="${c.idComentario}" 
                            style="position:absolute; top:0; right:0; border:none; background:none; color:red; cursor:pointer;">&times;</button>
                </div>
            `).join('');
        }

        // Actualizar contador en la tarjeta
        const spanTarjeta = document.getElementById(`comment-count-${idReview}`);
        if (spanTarjeta) spanTarjeta.textContent = comentarios.length;

        // Actualizar contador en el modal
        const spanModalComment = document.getElementById('modal-comment-count');
        if (spanModalComment) spanModalComment.textContent = comentarios.length;

    } catch (e) {
        cont.innerHTML = '<p style="color:red;">Error al cargar comentarios</p>';
        console.error(e);
    }
}

// ELIMINAR REVIEW
async function eliminarReview(idReview) {
    if (!confirm("¿Seguro querés eliminar esta review?")) return;

    try {
        const res = await fetch('reviewAdmin', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `accion=eliminar&id_review=${idReview}`
        });

        let data;
        try {
            data = await res.json();
        } catch (err) {
            const text = await res.text();
            console.error("Respuesta no válida del servidor:", text);
            alert("Error al eliminar: respuesta no válida");
            return;
        }

        if (data.exito) {
            alert("Review eliminada");
            cargarReviews();
        } else {
            alert(data.error || "No se pudo eliminar la review");
        }
    } catch (e) {
        console.error(e);
        alert("Error al eliminar la review");
    }
}

// ELIMINAR COMENTARIO
async function eliminarComentario(idComentario) {
    if (!confirm("¿Seguro querés eliminar este comentario?")) return;

    try {
        const res = await fetch('reviewAdmin', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `accion=eliminarComentario&id_comentario=${idComentario}`
        });
        const data = await res.json();
        if (data.exito) {
            // Recargar comentarios del modal usando el idReview guardado
            const idReview = document.getElementById('detalleModal').dataset.idReview;
            if (idReview) await cargarComentarios(idReview);
        } else {
            alert("No se pudo eliminar el comentario");
        }
    } catch (err) {
        console.error(err);
        alert("Error al eliminar el comentario");
    }
}

// EVENTOS
document.getElementById('reviewsList').addEventListener('click', (e) => {
    if (e.target.classList.contains('detalle-btn')) {
        verDetalle(e.target.dataset.id);
    }
    if (e.target.classList.contains('eliminar-btn')) {
        eliminarReview(e.target.dataset.id);
    }
});

document.getElementById('comentariosList').addEventListener('click', (e) => {
    if (e.target.classList.contains('borrar-comentario-btn')) {
        eliminarComentario(e.target.dataset.id);
    }
});

// PAGINACIÓN
document.getElementById('prevPage').addEventListener('click', () => {
    if (pagina > 1) { pagina--; cargarReviews(); }
});
document.getElementById('nextPage').addEventListener('click', () => {
    pagina++; cargarReviews();
});

// CERRAR MODAL
document.getElementById('detalleModalClose').addEventListener('click', () => {
    document.getElementById('detalleModal').style.display = 'none';
});

cargarReviews();

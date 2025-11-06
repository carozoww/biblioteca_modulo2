let pagina = 1;
let cargandoDetalle = false;
let verSoloMias = false;

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

    const res = await fetch(
        verSoloMias
            ? `reviews?accion=filtrarMisReviews`
            : `reviews?accion=listar&pagina=${pagina}`
    );

    const data = await res.json();
    data.forEach(r => {
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
                    <i class="fas fa-thumbs-up like-btn" data-id="${r.idReview}" style="cursor:pointer; font-size:14px;"></i>
                    <span class="like-count" id="like-count-${r.idReview}" style="font-size:0.85em;">${r.likes || 0}</span>
                </div>
                <div>
                    <i class="fas fa-comment"></i>
                    <span class="comment-count" id="comment-count-${r.idReview}" style="font-size:0.85em;">${r.comentarios || 0}</span>
                </div>
            </div>
            <button class="detalle-btn" data-id="${r.idReview}" style="margin-top:8px;">Ver detalle</button>
        `;
        container.appendChild(div);
    });

    document.getElementById('paginaActual').innerText = pagina;
}


// DAR LIKE

async function darLike(idReview) {
    const btn = document.querySelector(`.like-btn[data-id="${idReview}"]`);
    if (btn.classList.contains('disabled')) return;
    btn.classList.add('disabled');

    try {
        const res = await fetch('reviews', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `accion=like&id_review=${idReview}`
        });
        const data = await res.json();

        if (data.exito) {
            const cardCount = document.getElementById(`like-count-${idReview}`);
            if (cardCount) cardCount.innerText = data.nuevoConteo;

            const modalCount = document.getElementById('modal-like-count');
            if (modalCount && modalCount.dataset.id === String(idReview)) {
                modalCount.innerText = data.nuevoConteo;
            }
        } else {
            alert(data.error || "Error al dar like");
        }
    } finally {
        btn.classList.remove('disabled');
    }
}


// VER DETALLE DE REVIEW

async function verDetalle(idReview) {
    if (cargandoDetalle) return;
    cargandoDetalle = true;

    try {
        const res = await fetch(`reviews?accion=detalle&id_review=${idReview}`);
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

        const likeSection = document.getElementById('modal-like-section');
        likeSection.innerHTML = `
            <i class="fas fa-thumbs-up" 
               id="modal-like-btn" 
               data-id="${r.idReview}" 
               style="cursor:pointer; font-size:20px;"></i>
            <span id="modal-like-count" data-id="${r.idReview}" style="margin-left:5px;">${r.likes || 0}</span>
            <span style="margin-left:15px;"><i class="fas fa-comment"></i> ${r.comentarios || 0}</span>
        `;

        document.getElementById('modal-like-btn').onclick = async () => {
            await darLike(r.idReview);
            document.getElementById('modal-like-count').innerText =
                document.getElementById(`like-count-${r.idReview}`).innerText;
        };

        document.getElementById('detalleModal').style.display = 'flex';
        await cargarComentarios(idReview);

        // Guardar ID actual para comentar
        document.getElementById('btnAgregarComentario').dataset.idreview = idReview;

    } catch (err) {
        console.error(err);
        alert("Error al cargar detalles de la review");
    } finally {
        cargandoDetalle = false;
    }
}


// COMENTARIOS


// Cargar comentarios de una review
async function cargarComentarios(idReview) {
    const cont = document.getElementById('comentariosList');
    cont.innerHTML = '<p style="color:#666;">Cargando comentarios...</p>';

    try {
        const res = await fetch(`reviews?accion=listarComentarios&id_review=${idReview}`);
        const comentarios = await res.json();

        // Actualizar lista de comentarios en el modal
        if (!comentarios || comentarios.length === 0) {
            cont.innerHTML = '<p style="color:#777;">No hay comentarios aún.</p>';
        } else {
            cont.innerHTML = comentarios.map(c => `
                <div class="comentario-item" style="display:flex; gap:10px; margin-bottom:10px; align-items:flex-start;">
                    <img src="${c.imagenLector || 'imgs/iconouser.png'}" class="lector-img" style="width:35px; height:35px;">
                    <div>
                        <strong>${c.nombreLector}</strong>
                        <span style="font-size:0.8em; color:#888;"> (${new Date(c.fecha).toLocaleString()})</span>
                        <p style="margin:5px 0; color:#333;">${c.contenido}</p>
                    </div>
                </div>
            `).join('');
        }

        // Actualizar contador en el modal
        const spanModalIcon = document.getElementById('modal-comment-count');
        if (spanModalIcon) spanModalIcon.textContent = comentarios.length;

        // Actualizar contador en la tarjeta
        const spanTarjeta = document.getElementById(`comment-count-${idReview}`);
        if (spanTarjeta) spanTarjeta.textContent = comentarios.length;

    } catch (e) {
        cont.innerHTML = '<p style="color:red;">Error al cargar comentarios</p>';
        console.error(e);
    }
}

// Enviar nuevo comentario
async function enviarComentario(idReview) {
    const textarea = document.getElementById('inputComentario');
    const contenido = textarea.value.trim();
    if (!contenido) return;

    const body = `accion=crearComentario&id_review=${idReview}&id_lector=${idLector}&contenido=${encodeURIComponent(contenido)}`;

    try {
        const res = await fetch('reviews', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body
        });
        const data = await res.json();

        if (data.exito) {
            textarea.value = '';
            await cargarComentarios(idReview);
        } else {
            alert(data.error || "Error al agregar comentario");
        }
    } catch (e) {
        console.error(e);
        alert("Error al enviar comentario");
    }
}

// Boton para agregar comentario
document.getElementById('btnAgregarComentario').addEventListener('click', () => {
    const idReview = document.getElementById('btnAgregarComentario').dataset.idreview;
    if (!idReview) { console.error("idReview vacío!"); return; }
    enviarComentario(idReview);
});

//Boton del filtro
document.getElementById("btnToggleReviews").addEventListener("click", async () => {
    verSoloMias = !verSoloMias;
    document.getElementById("btnToggleReviews").innerText = verSoloMias ? "Ver Todas las Reseñas" : "Ver Mis Reseñas";
    await cargarReviews();
});

// MODALES

// Modal nueva review
const inputLibro = document.getElementById('inputLibro');
const contenedorSugerencias = document.getElementById('sugerenciasLibros');
let librosCache = [];

document.getElementById('nuevaReviewBtn').addEventListener('click', () => {
    inputLibro.value = '';
    inputLibro.dataset.id = '';
    contenedorSugerencias.innerHTML = '';
    document.getElementById('valoracion').value = '';
    document.getElementById('resenia').value = '';
    document.getElementById('guardarReview').innerText = 'Guardar';
    document.getElementById('reviewModal').style.display = 'flex';
});

// Autocompletado
inputLibro.addEventListener('input', async () => {
    const query = inputLibro.value.trim().toLowerCase();
    if (!query) {
        contenedorSugerencias.innerHTML = '';
        return;
    }

    if (librosCache.length === 0) {
        const res = await fetch(`libros?accion=buscar&titulo=${encodeURIComponent(query)}`);
        librosCache = await res.json();
    }

    const filtrados = librosCache.filter(l => l.titulo.toLowerCase().includes(query));
    contenedorSugerencias.innerHTML = filtrados.map(l =>
        `<div class="sugerencia-item" data-id="${l.idLibro}">${l.titulo}</div>`
    ).join('');

    document.querySelectorAll('.sugerencia-item').forEach(item => {
        item.onclick = () => {
            inputLibro.value = item.innerText;
            inputLibro.dataset.id = item.dataset.id;
            contenedorSugerencias.innerHTML = '';
            cargarReviewExistente(item.dataset.id);
        };
    });
});

// Cargar review existente
async function cargarReviewExistente(idLibro) {
    const res = await fetch(`reviews?accion=porLibroYLector&id_libro=${idLibro}&id_lector=${idLector}`);
    const existente = await res.json();

    if (existente && existente.idReview) {
        document.getElementById('valoracion').value = existente.valoracion;
        document.getElementById('resenia').value = existente.resenia;
        document.getElementById('guardarReview').innerText = 'Actualizar';
    } else {
        document.getElementById('valoracion').value = '';
        document.getElementById('resenia').value = '';
        document.getElementById('guardarReview').innerText = 'Guardar';
    }
}

// Guardar / actualizar review
document.getElementById('guardarReview').addEventListener('click', async () => {
    const idLibro = inputLibro.dataset.id;
    const valoracion = document.getElementById('valoracion').value;
    const resenia = document.getElementById('resenia').value;

    if (!idLibro) {
        alert("Seleccioná un libro de la lista.");
        return;
    }

    const checkRes = await fetch(`reviews?accion=porLibroYLector&id_libro=${idLibro}&id_lector=${idLector}`);
    const existente = await checkRes.json();

    let body;
    if (existente && existente.idReview) {
        body = `id_review=${existente.idReview}&valoracion=${valoracion}&resenia=${resenia}`;
    } else {
        body = `id_lector=${idLector}&id_libro=${idLibro}&valoracion=${valoracion}&resenia=${resenia}`;
    }

    const response = await fetch('reviews', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body
    });

    const data = await response.json();
    alert(data.mensaje || data.error);
    cargarReviews();
    document.getElementById('reviewModal').style.display = 'none';
});



document.getElementById('reviewModalClose').addEventListener('click', () => {
    document.getElementById('reviewModal').style.display = 'none';
});
document.getElementById('detalleModalClose').addEventListener('click', () => {
    document.getElementById('detalleModal').style.display = 'none';
});


// BOTONES DETALLE Y LIKE EN TARJETA

document.getElementById('reviewsList').addEventListener('click', (e) => {
    if (e.target.classList.contains('detalle-btn')) {
        verDetalle(e.target.dataset.id);
    }
    if (e.target.classList.contains('like-btn')) {
        darLike(e.target.dataset.id);
    }
});


// PAGINACIÓN

document.getElementById('prevPage').addEventListener('click', () => {
    if (pagina > 1) { pagina--; cargarReviews(); }
});
document.getElementById('nextPage').addEventListener('click', () => {
    pagina++; cargarReviews();
});

cargarReviews();

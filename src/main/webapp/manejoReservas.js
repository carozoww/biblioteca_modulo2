function showTab(tabId) {
    document.querySelectorAll('.tab-content').forEach(div => div.classList.remove('active'));
    document.querySelectorAll('.tablink').forEach(btn => btn.classList.remove('active'));
    document.getElementById(tabId).classList.add('active');
    event.target.classList.add('active');

    // guarda la pestaña seleccionada
    localStorage.setItem("tabActivaReservas", tabId);
}

// al cargar la página, revisa que pestaña estaba activa
window.addEventListener('load', function () {
    const tabGuardada = localStorage.getItem("tabActivaReservas");
    if (tabGuardada && document.getElementById(tabGuardada)) {
        document.querySelectorAll('.tab-content').forEach(div => div.classList.remove('active'));
        document.getElementById(tabGuardada).classList.add('active');

        document.querySelectorAll('.tablink').forEach(btn => btn.classList.remove('active'));
        document.querySelector(`.tablink[onclick="showTab('${tabGuardada}')"]`).classList.add('active');
    } else {
        // si no hay pestaña guardada, mostrara la primera
        document.querySelector('.tab-content').classList.add('active');
        document.querySelector('.tablink').classList.add('active');
    }
});
// mensaje que desaparece automáticamente
window.addEventListener('load', function() {
    const msg = document.querySelector('.msg');
    if (msg) {
        setTimeout(() => {
            msg.style.transition = 'opacity 0.8s ease';
            msg.style.opacity = '0';
            setTimeout(() => msg.remove(), 800);
        }, 3000);
    }
});

// === PAGINACIÓN Y BÚSQUEDA PARA RESERVAS ===
document.addEventListener('DOMContentLoaded', function() {
    const limit = 10; // cantidad de filas por página

    // Aplica paginación y búsqueda a todas las pestañas
    document.querySelectorAll('.tab-content').forEach(tab => {
        const tabla = tab.querySelector('.tabla');
        if (!tabla) return;

        const tbody = tabla.querySelector('tbody');
        const filas = Array.from(tbody.querySelectorAll('tr'));
        const buscador = tab.querySelector('input[type="text"]');
        const paginacion = tab.querySelector('#paginacion');

        let offset = parseInt(localStorage.getItem(tab.id + "_offset")) || 0;

        function mostrarPagina() {
            filas.forEach(f => f.style.display = 'none');
            const inicio = offset * limit;
            const fin = inicio + limit;
            filas.slice(inicio, fin).forEach(f => f.style.display = '');
            actualizarBotones();
            localStorage.setItem(tab.id + "_offset", offset); // guarda la página actual
        }

        function actualizarBotones() {
            if (!paginacion) return;
            const anterior = paginacion.querySelector('#anterior');
            const siguiente = paginacion.querySelector('#siguiente');
            if (!anterior || !siguiente) return;

            anterior.style.visibility = offset === 0 ? 'hidden' : 'visible';
            siguiente.style.visibility = (offset + 1) * limit >= filas.length ? 'hidden' : 'visible';

            anterior.onclick = () => {
                if (offset > 0) {
                    offset--;
                    mostrarPagina();
                }
            };
            siguiente.onclick = () => {
                if ((offset + 1) * limit < filas.length) {
                    offset++;
                    mostrarPagina();
                }
            };
        }

        // Buscador específico para cada pestaña
        if (buscador) {
            buscador.addEventListener('input', () => {
                const termino = buscador.value.toLowerCase().trim();
                let hayResultados = false;

                filas.forEach(f => {
                    if (f.classList.contains('sin-reservas')) return; // ignora este mensaje
                    if (f.classList.contains('btn-cancelar')) return;
                    if (f.classList.contains('btn-aceptar')) return;

                    const visible = f.textContent.toLowerCase().includes(termino);
                    f.style.display = visible ? '' : 'none';
                    if (visible) hayResultados = true;
                });

                // mostrar/ocultar el mensaje "No hay reservas..." según haya resultados
                const filaMensaje = tbody.querySelector('.sin-reservas');
                if (filaMensaje) filaMensaje.style.display = hayResultados ? 'none' : '';
            });
        }

        mostrarPagina();
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const tabla = document.querySelector('#tabla tbody');
    const filas = Array.from(tabla.querySelectorAll('tr'));
    const buscador = document.getElementById('buscar');
    const botonAnterior = document.getElementById('anterior');
    const botonSiguiente = document.getElementById('siguiente');
    const limit = 10;
    let offset = parseInt(sessionStorage.getItem('paginaLectores') || 0);
    let lectoresFiltrados = [...filas];

    function mostrarLectores() {
        filas.forEach(f => f.style.display = 'none');
        const inicio = offset * limit;
        const fin = inicio + limit;
        lectoresFiltrados.slice(inicio, fin).forEach(f => f.style.display = '');
        checkBotones();
        sessionStorage.setItem('paginaLectores', offset); // guardamos la página actual
    }

    function checkBotones() {
        botonAnterior.style.visibility = offset === 0 ? 'hidden' : 'visible';
        botonSiguiente.style.visibility = (offset + 1) * limit >= lectoresFiltrados.length ? 'hidden' : 'visible';
    }

    botonSiguiente.addEventListener('click', () => {
        offset++;
        mostrarLectores();
    });

    botonAnterior.addEventListener('click', () => {
        if (offset > 0) offset--;
        mostrarLectores();
    });

    buscador.addEventListener('input', () => {
        const termino = buscador.value.toLowerCase().trim();
        lectoresFiltrados = filas.filter(fila => fila.textContent.toLowerCase().includes(termino));
        offset = 0;
        mostrarLectores();
    });

    // Evitar volver a primera página después de autenticar
    document.querySelectorAll('form[action="autenticarLector"]').forEach(form => {
        form.addEventListener('submit', () => {
            sessionStorage.setItem('paginaLectores', offset);
        });
    });

    mostrarLectores();
});

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
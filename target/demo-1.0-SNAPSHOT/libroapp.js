let misLibros = [];
let offset = 0;
const limit = 6;
const divLibros = document.getElementById('containerLibro');
const botonSiguiente = document.getElementById('siguiente');
const botonAnterior = document.getElementById('anterior');





async function cargarLibros(){
    try {
        const response = await fetch('libros');
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        misLibros = await response.json();
        mostrarLibros();
    } catch (error) {
        console.error('Error:', error);
    }
}

function mostrarLibros(){
    divLibros.classList.add('fade-out');

    setTimeout(()=>{
        limpiarLibros();
        const librosPagina = misLibros.slice(offset * limit, (offset * limit) + limit);
        librosPagina.forEach(libro =>{
            const libroDiv = document.createElement('div');
            libroDiv.className = 'libro';
            libroDiv.innerHTML = `
                <h1>${libro.titulo}</h1>
                <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
                <p>${libro.fechaPublicacion}</p>
                <button>ver mas</button>
            `;
            divLibros.appendChild(libroDiv);
        });
        // Agregar fade-in después de agregar los elementos
        divLibros.classList.remove('fade-out');
        divLibros.classList.add('fade-in');

        setTimeout(() => {
            divLibros.classList.remove('fade-in');
        }, 300);
    },150)
}


function limpiarLibros(){
    divLibros.innerHTML = '';
}

botonSiguiente.addEventListener('click', () =>{
    offset++;
    limpiarLibros();
    mostrarLibros();
})

botonAnterior.addEventListener('click', () =>{
    if(offset == 0){
        return;
    }
    offset--;
    limpiarLibros();
    mostrarLibros();
})

document.addEventListener('DOMContentLoaded', function() {
    cargarLibros();
});



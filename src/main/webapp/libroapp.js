let misLibros = [];
let offset = 0;
const limit = 6;
const divLibros = document.getElementById('containerLibro');

document.addEventListener('DOMContentLoaded', function() {
    cargarLibros();
});

function cargarLibros(){
    fetch('libros')
        .then(response =>{
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(libros => {
            misLibros = libros;
            const libros2 = misLibros.slice(offset, limit);
            libros2.forEach(libro =>{
                const bookElement = document.createElement('div');
                bookElement.className = 'libro';
                bookElement.innerHTML = `
                    <h1>${libro.titulo}</h1>
                    <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
                    <p>${libro.fechaPublicacion}</p>
                    <button>ver mas</button>
                `;
                divLibros.appendChild(bookElement);
            })
        })
}
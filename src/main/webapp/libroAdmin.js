let misLibros = [];
let librosFiltrados = [];
let offset = 0;
const limit = 6;
const divLibros = document.getElementById('containerLibro');
const botonSiguiente = document.getElementById('siguiente');
const botonAnterior = document.getElementById('anterior');
const buscador = document.getElementById('inputBuscador')


document.addEventListener('DOMContentLoaded', function() {
    cargarLibros();

});
buscador.addEventListener('input', (e) => {
    const termino = e.target.value.toLowerCase().trim();
    var tipoSelect = document.getElementById('selectTipo');

    console.log("Término buscado:", termino);
    console.log("Tipo seleccionado:", tipoSelect.value);

    if(tipoSelect.value=== "Titulo"){
        librosFiltrados = misLibros.filter(libro =>
            libro.titulo.toLowerCase().includes(termino)
        );
    }else if(tipoSelect.value === "Editorial"){
        librosFiltrados = misLibros.filter(libro =>
            libro.editorial.toLowerCase().includes(termino)
        );
    }else if(tipoSelect.value === "Autor"){
        librosFiltrados = misLibros.filter(libro =>
            libro.nombreAutor && libro.nombreAutor.toLowerCase().includes(termino)
        );
    }


    offset = 0;
    mostrarLibros();
});



async function cargarLibros(){
    try {
        const response = await fetch('libros');
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        misLibros = await response.json();
        librosFiltrados = [...misLibros];
        mostrarLibros();

    } catch (error) {
        console.error('Error:', error);
    }
}

function mostrarLibros(){
    checkBotones();
    divLibros.classList.add('fade-out');

    setTimeout(()=>{
        limpiarLibros();
        const librosPagina = librosFiltrados.slice(offset * limit, (offset * limit) + limit);
        librosPagina.forEach(libro =>{
            const libroDiv = document.createElement('div');
            libroDiv.className = 'libro';
            if(libro.imagen_url == null){
                libroDiv.innerHTML = `
                <h1>${libro.titulo}</h1>
                <img src="imgs/libro.jpg" alt="" width="150px" height="150px">
                <p>${libro.fechaPublicacion}</p>
                <div class="div-botones">
                    <button class="editar" onclick="window.location.href='modificar-libro-servlet?id=${libro.idLibro}'">Editar</button>
                    <button class="eliminar" onclick="window.location.href='baja-libro-servlet?id=${libro.idLibro}'">Eliminar</button>
                </div>
            `;
            }else{
                libroDiv.innerHTML = `
                <h1>${libro.titulo}</h1>
                <img src="${libro.imagen_url}" alt="" width="150px" height="150px">
                <p>${libro.fechaPublicacion}</p>
                <div class="div-botones">
                    <button class="editar" onclick="window.location.href='modificar-libro-servlet?id=${libro.idLibro}'">Editar</button>
                    <button class="eliminar" onclick="window.location.href='baja-libro-servlet?id=${libro.idLibro}'" >Eliminar</button>
                </div>
            `;
            }

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





function checkBotones(){
    if(offset === 0){
        botonAnterior.style.visibility = 'hidden';
    }else{
        botonAnterior.style.visibility = 'visible';
    }

    if((offset*limit) >= (librosFiltrados.length - limit)){
        botonSiguiente.style.visibility = 'hidden';
    }else{
        botonSiguiente.style.visibility = 'visible';
    }
}


function limpiarLibros(){
    divLibros.innerHTML = '';
}

botonSiguiente.addEventListener('click', () =>{
    offset++;
    console.log(offset);
    limpiarLibros();
    mostrarLibros();
    checkBotones();
})

botonAnterior.addEventListener('click', () =>{
    if(offset == 0){
        return;
    }
    offset--;
    console.log(misLibros.length);
    console.log(offset);
    limpiarLibros();
    mostrarLibros();
    checkBotones();
})

document.addEventListener('DOMContentLoaded', function() {
    cargarLibros();
    if(offset === 0){
        botonAnterior.style.visibility = 'hidden';
    }
});




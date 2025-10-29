const buscadorEditorial = document.getElementById('edSearch');
let misEditoriales = [];
let editorialesFiltro = [];
let offset = 0;
const limit = 6;
const divEditoriales = document.getElementById('listado-editoriales');
var editorial = document.getElementById('idEditorial')



document.addEventListener('DOMContentLoaded', function() {
    cargarEditoriales();

});

let editorialIdSeleccionada = null;

buscadorEditorial.addEventListener('input', (e) =>{

    const termino = e.target.value.toLowerCase().trim();
    if(termino === ''){
        editorialesFiltro = [...misEditoriales];
        editorialIdSeleccionada = null;
        limpiarEditoriales();
        return;
    }
    editorialesFiltro = misEditoriales.filter(ed =>
        ed.nombre.toLowerCase().includes(termino)
    );
    offset = 0;
    mostrarEditoriales();
})


async function cargarEditoriales(){
    try {
        const response = await fetch('editoriales');
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        misEditoriales = await response.json();
        editorialesFiltro = [...misEditoriales];

    } catch (error) {
        console.error('Error:', error);
    }
}

function mostrarEditoriales(){
        limpiarEditoriales();
        const EditorialesPagina = editorialesFiltro.slice(offset * limit, (offset * limit) + limit);
        EditorialesPagina.forEach(ed =>{
            const edDiv = document.createElement('div');
            edDiv.className = 'editorial';
                edDiv.innerHTML = `
                <p>${ed.nombre}</p>
                <button type="button" class="asignarEditorial" data-id="${ed.idEditorial}" data-nombre="${ed.nombre}">Asignar</button>
            `;

            divEditoriales.appendChild(edDiv);

            asignarEventListenersEditoriales();
        });
}

function limpiarEditoriales(){
    divEditoriales.innerHTML = '';
}

function asignarEventListenersEditoriales() {
    const botonesAsignar = document.querySelectorAll('.asignarEditorial');

    botonesAsignar.forEach(boton => {
        boton.addEventListener('click', (e) => {
            // Obtener los datos del botón clickeado
            const idEditorial = e.target.getAttribute('data-id');
            const nombreEditorial = e.target.getAttribute('data-nombre');

            // Asignar valores al input
            buscadorEditorial.value = nombreEditorial;
            editorialIdSeleccionada = idEditorial;
            editorial.value = idEditorial;

            // Ocultar la lista de editoriales
            limpiarEditoriales();

            console.log('Editorial seleccionada:', nombreEditorial, 'ID:', idEditorial);
        });
    });
}
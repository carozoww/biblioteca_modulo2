const buscadorEditorial = document.getElementById('edSearch');
const buscadorAutor = document.getElementById('auSearch');
const buscadorGenero = document.getElementById('genSearch');

let misAutores = [];
let autoresSeleccionados = [];
let generosSeleccionados = [];

let misEditoriales = [];
let editorialesFiltro = [];

let offset = 0;
const limit = 6;
const divAutores = document.getElementById('listado-autores')

let autoresFiltro = [];
const divEditoriales = document.getElementById('listado-editoriales');
var editorial = document.getElementById('idEditorial')
var autor = document.getElementById('idAutor');
var genero = document.getElementById('idGenero');
const autoreSeleccion = document.getElementById('autores-seleccion');
const generoSeleccion = document.getElementById('generos-seleccion');

let misGeneros = [];
let generosFiltro = [];
const divGeneros = document.getElementById('listado-generos');




document.addEventListener('DOMContentLoaded', function() {
    cargarEditoriales();
    cargarAutores();
    cargarGeneros();
    autor.value = autoresSeleccionados;
    genero.value = generosSeleccionados;

    divAutores.addEventListener('click', (e) => {
        if (e.target.classList.contains('asignarAutor')) {
            const idAutor = e.target.getAttribute('data-id');
            const nombreAutor = e.target.getAttribute('data-nombre');

            if (!autoresSeleccionados.includes(idAutor)) {
                autoresSeleccionados.push(idAutor);
                autor.value = autoresSeleccionados;

                const lista = document.createElement('li');
                lista.innerText = nombreAutor;
                autoreSeleccion.appendChild(lista);

                const delAutor = document.createElement('button');
                delAutor.value = idAutor;
                delAutor.type = 'button';
                delAutor.innerText = "Eliminar";
                delAutor.className = 'del';
                lista.appendChild(delAutor);


                limpiarAutores();

                console.log('Autor seleccionado:', nombreAutor, 'ID:', idAutor);
                console.log(autor.value);
            } else {
                console.log('Este autor ya fue seleccionado');
            }
        }
    });

    autoreSeleccion.addEventListener('click',(e) =>{
        if(e.target.classList.contains('del')){
            const id_autor = e.target.value;
            for(var i = 0;i<autoresSeleccionados.length;i++){
                if(autoresSeleccionados[i] == id_autor){
                    console.log(id_autor);
                    autoresSeleccionados.splice(i,1);
                    e.target.parentElement.remove();
                    break;
                }
            }
            autor.value = autoresSeleccionados;
            console.log(autoresSeleccionados);



        }
    })

    divGeneros.addEventListener('click', (e) => {
        if (e.target.classList.contains('asignarGenero')) {
            const idGenero = e.target.getAttribute('data-id');
            const nombreGenero = e.target.getAttribute('data-nombre');

            if (!generosSeleccionados.includes(idGenero)) {
                generosSeleccionados.push(idGenero);
                genero.value = generosSeleccionados;

                const lista1 = document.createElement('li');
                lista1.innerText = nombreGenero;
                generoSeleccion.appendChild(lista1);

                const delGenero = document.createElement('button');
                delGenero.value = idGenero;
                delGenero.type = 'button';
                delGenero.innerText = "Eliminar";
                delGenero.className = 'del';
                lista1.appendChild(delGenero);


                limpiarGeneros();

                console.log('Genero seleccionado:', nombreGenero, 'ID:', idGenero);
                console.log(genero.value);
            } else {
                console.log('Este genero ya fue seleccionado');
            }
        }
    });

    generoSeleccion.addEventListener('click',(e) =>{
        if(e.target.classList.contains('del')){
            const id_genero = e.target.value;
            for(var i = 0;i<generosSeleccionados.length;i++){
                if(generosSeleccionados[i] == id_genero){
                    console.log(id_genero);
                    generosSeleccionados.splice(i,1);
                    e.target.parentElement.remove();
                    break;
                }
            }
            genero.value = generosSeleccionados;
            console.log(generosSeleccionados);



        }
    })





});

let editorialIdSeleccionada = null;

buscadorEditorial.addEventListener('input', (e) =>{

    const termino = e.target.value.toLowerCase().trim();
    console.log(termino);
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
            const idEditorial = e.target.getAttribute('data-id');
            const nombreEditorial = e.target.getAttribute('data-nombre');

            buscadorEditorial.value = nombreEditorial;
            editorialIdSeleccionada = idEditorial;
            editorial.value = idEditorial;

            limpiarEditoriales();

            console.log('Editorial seleccionada:', nombreEditorial, 'ID:', idEditorial);
        });
    });
}


buscadorAutor.addEventListener('input', (e) =>{

    const termino1 = e.target.value.toLowerCase().trim();
    if(termino1 === ''){
        autoresFiltro = [...misAutores];
        limpiarAutores();
        return;
    }
    autoresFiltro = misAutores.filter(autor =>
        autor.nombre.toLowerCase().includes(termino1)
    );
    offset = 0;
    mostrarAutores();
})

async function cargarAutores(){
    try {
        const response = await fetch('autores');
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        misAutores = await response.json();
        autoresFiltro = [...misAutores];

    } catch (error) {
        console.error('Error:', error);
    }
}

function mostrarAutores(){
    limpiarAutores();
    const autoresPagina = autoresFiltro.slice(offset * limit, (offset * limit) + limit);
    autoresPagina.forEach(autor =>{
        const auDiv = document.createElement('div');
        auDiv.className = 'autor';
        auDiv.innerHTML = `
                <p>${autor.nombre} ${autor.apellido}</p>
                <button type="button" class="asignarAutor" data-id="${autor.id_autor}" data-nombre="${autor.nombre} ${autor.apellido}">Asignar</button>
            `;

        divAutores.appendChild(auDiv);

    });
}

function limpiarAutores(){
    divAutores.innerHTML = '';
}

buscadorGenero.addEventListener('input', (e) =>{

    const termino2 = e.target.value.toLowerCase().trim();
    if(termino2 === ''){
        generosFiltro = [...misGeneros];
        limpiarGeneros();
        return;
    }
    generosFiltro = misGeneros.filter(genero =>
        genero.nombre.toLowerCase().includes(termino2)
    );
    offset = 0;
    mostrarGeneros();
})


async function cargarGeneros(){
    try {
        const response = await fetch('generos');
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        misGeneros = await response.json();
        generosFiltro = [...misGeneros];

    } catch (error) {
        console.error('Error:', error);
    }
}

function mostrarGeneros(){
    limpiarGeneros();
    const generosPagina = generosFiltro.slice(offset * limit, (offset * limit) + limit);
    generosPagina.forEach(genero =>{
        const genDiv = document.createElement('div');
        genDiv.className = 'genero';
        genDiv.innerHTML = `
                <p>${genero.nombre}</p>
                <button type="button" class="asignarGenero" data-id="${genero.id_genero}" data-nombre="${genero.nombre} ">Asignar</button>
            `;

        divGeneros.appendChild(genDiv);

    });
}

function limpiarGeneros(){
    divGeneros.innerHTML = '';
}

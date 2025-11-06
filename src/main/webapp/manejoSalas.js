var datosSalasHabilitadas = null;
var datosSalasDeshabilitadas = null;

const accion = document.getElementById("accion");

const idSala = document.getElementById("id-sala");
const urlImg = document.getElementById("imagen-url");
const numSala = document.getElementById("numero-sala");
const ubicacion = document.getElementById("ubicacion");
const maxPersona = document.getElementById("max-personas");
const habilitada = document.getElementById("habilitada");
const imagenAnterior = document.getElementById("imagen-anterior");

const eliminarSalaForm = document.getElementById("eliminarSala");
const eliminarSala = document.getElementById("eliminar-sala");

const imagenSeleccionada = document.getElementById("imagenSeleccionada");

const cancelar = document.getElementById("cancelar");
const boton = document.getElementById("boton");

const filtroHabilitada = document.getElementById("filtro-habilitada");

numSala.addEventListener('input', function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});

maxPersona.addEventListener('input', function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});

filtroHabilitada.addEventListener('change', function () {
    if (this.value === "false") {
        cargarSalasDeshabilitadas()
    } else {
        cargarSalasHabilitadas();
    }
});

function editarSalaCampos(salaId) {
    let datos;
    if (filtroHabilitada.value === "false") {
        datos = datosSalasDeshabilitadas.filter(sala => sala.idSala === salaId);
    } else {
        datos = datosSalasHabilitadas.filter(sala => sala.idSala === salaId);
    }


    idSala.value = datos[0].idSala || "";
    //urlImg.value = datos[0].imagen || "";

    numSala.value = datos[0].numeroSala || "";
    ubicacion.value = datos[0].ubicacion || "";
    maxPersona.value = datos[0].maxPersonas || "";

    habilitada.value = ("" + datos[0].habilitada) || "";

    if (!datos[0].imagen || datos[0].imagen.trim() === "") {
        imagenSeleccionada.src = "imgs/room.png";
    } else {
        imagenSeleccionada.src = datos[0].imagen;
    }
    imagenAnterior.value = datos[0].imagen;
    cancelar.classList.remove("hidden");
    boton.className = "btn-modificar";
    urlImg.required = false;
    boton.innerText = "Actualizar";
    accion.value = "editar";

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

function eliminar(idSala) {
    eliminarSala.value = idSala;
    const confirmado = confirm("¿Eliminar la sala?");
    if (confirmado) {
        eliminarSalaForm.submit();
    }
}

cancelar.addEventListener('click', (e) => {
    cancelar.classList.add("hidden");
    boton.className = "btn-agregar";
    urlImg.required = true;
    boton.innerText = "Agregar";
    accion.value = "agregar";

    imagenAnterior.value = "";
    idSala.value = "";
    urlImg.value = "";
    numSala.value = "";
    ubicacion.value = "";
    maxPersona.value = "";
    imagenSeleccionada.src = "imgs/room.png";
    habilitada.value = true;
});

function cargarSalas() {
    fetch("administradorSalas?cargarSalas=true")
        .then(res => res.json())
        .then(data => {
            datosSalasHabilitadas = data.filter(s => s.habilitada === true);
            datosSalasDeshabilitadas = data.filter(s => s.habilitada === false);
            cargarSalasHabilitadas();
        })
        .catch(err => console.error("Error cargando salas:", err));
}

function cargarSalasHabilitadas() {
    const tbody = document.querySelector("#contenedor-tabla table tbody");
    tbody.innerHTML = "";
    if (datosSalasHabilitadas.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6">No hay salas habilitadas</td></tr>`;
    } else {
        datosSalasHabilitadas.forEach(s => {
            var row = `<tr>`;
            if (!s.imagen || s.imagen.trim() === "") {
                row += `<td><img src='imgs/room.png' alt='Sala' width='150' height='150'></td>`;
            } else {
                row += `<td><img src='` + s.imagen + `' alt='Sala' width='150' height='150'></td>`;
            }

            row += `<td data-label="Numero Sala">` + s.numeroSala + `</td>
                       <td data-label="Ubicacion">` + s.ubicacion + `</td>
                       <td data-label="Max Personas">` + s.maxPersonas + `</td>
                       <td><button class="btn-modificar" onclick="editarSalaCampos(` + s.idSala + `)">Editar </button></td>
                       <td><button class="btn-eliminar" onclick="eliminar(` + s.idSala + `)">Eliminar </button></td>
                    </tr>`;
            tbody.innerHTML += row;
        });
    }
}

function cargarSalasDeshabilitadas() {
    const tbody = document.querySelector("#contenedor-tabla table tbody");
    tbody.innerHTML = "";
    if (datosSalasDeshabilitadas.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6">No hay salas deshabilitadas</td></tr>`;
    } else {
        datosSalasDeshabilitadas.forEach(s => {
            var row = `<tr>`;
            if (!s.imagen || s.imagen.trim() === "") {
                row += `<td><img src='imgs/room.png' alt='Sala' width='150' height='150'></td>`;
            } else {
                row += `<td><img src='` + s.imagen + `' alt='Sala' width='150' height='150'></td>`;
            }

            row += `<td data-label="Numero Sala">` + s.numeroSala + `</td>
                       <td data-label="Ubicacion">` + s.ubicacion + `</td>
                       <td data-label="Max Personas">` + s.maxPersonas + `</td>
                       <td><button class="btn-modificar" onclick="editarSalaCampos(` + s.idSala + `)">Editar </button></td>
                       <td><button class="btn-eliminar" onclick="eliminar(` + s.idSala + `)">Eliminar </button></td>
                    </tr>`;
            tbody.innerHTML += row;
        });
    }
}

window.addEventListener('load', function() {
    const msg = document.querySelector('.mensaje');
    if (msg) {
        setTimeout(() => {
            msg.style.transition = 'opacity 0.8s ease';
            msg.style.opacity = '0';
            setTimeout(() => msg.remove(), 800);
        }, 3000);
    }
});

cargarSalas();
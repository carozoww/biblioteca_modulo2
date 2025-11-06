const inputFecha = document.getElementById("fecha");
const contenedorReservas = document.getElementById("tabla-container");
const contenedorSalas = document.getElementById("containerSalas");
const horaInicio = document.getElementById("hora-inicio");
const horaFin = document.getElementById("hora-fin");
const mensajeReserva = document.getElementById("mensajeReserva");
const camposHoras = document.getElementById("camposHoras");
const salaEnviar = document.getElementById("sala-enviar");
const fechaEnviar = document.getElementById("fecha-enviar");
const mostrarReservaActiva = document.getElementById("mostrarReservaActiva").value;
let reservasPorFecha = null;


const salaItems = document.querySelectorAll('.sala-item');


// Cargar lista de salas/*

function initSalaItems() {
    const salaItems = document.querySelectorAll('.sala-item');

    salaItems.forEach(function (item) {
        const img = item.querySelector('.sala-img');
        const info = item.querySelector('.info');
        const btn = item.querySelector('.btn-info');

        function toggleInfo() {
            // Toggle usando clase
            info.classList.toggle('visible');
            item.classList.toggle('seleccionada');
        }

        img.addEventListener('click', toggleInfo);
        btn.addEventListener('click', toggleInfo);
    });
}

document.addEventListener('DOMContentLoaded', initSalaItems);


// Cargar tabla reservas
function cargarTablaReservas() {
    if (!reservasPorFecha) return;

    if (mostrarReservaActiva === "sinReservaActiva") {
        fechaEnviar.value = inputFecha.value;
    }

    const {horas, salas} = reservasPorFecha;
    const fechaActual = new Date();
    let horariosValidos = horas.filter(hora => {
        const fechaHora = new Date(inputFecha.value + `T` + hora + `:00`);
        return fechaHora > fechaActual;
    });
    let html = "<table><thead><tr><th>Hora</th>";

    salas.forEach(s => {
        html += `<th>Sala ` + s.numeroSala + `</th>`;
    });

    html += "</tr></thead><tbody>";

    horas.forEach((hora, i) => {
        html += `<tr><td>` + hora.toString() + `</td>`;
        salas.forEach(s => {
            if (horariosValidos.includes(hora)) {
                const disponible = s.disponibilidad[i];
                html += disponible
                    ? "<td class='disponible'>&#10004;</td>"
                    : "<td class='no-disponible'>&#10008;</td>";
            } else {
                html += "<td class='no-disponible'>&#10008;</td>";
            }
        });

        html += "</tr>";
    });

    html += "</tbody></table>";
    contenedorReservas.innerHTML = html;
}


function cargarSalas() {
    fetch("salas?action=listar")
        .then(res => res.json())
        .then(data => {
            let html = "";
            data.forEach(s => {
                if (mostrarReservaActiva === "sinReservaActiva") {
                    html += "<div class='sala-item' id='" + s.idSala + "' onclick='actualizarValor(" + s.idSala + ")'>";
                } else {
                    html += "<div class='sala-item' id='" + s.idSala + "' >";
                }
                const img = s.imagen && s.imagen.trim() !== "" ? s.imagen : "imgs/room.png";

                html += " <p>Sala " + s.numeroSala + "</p>";
                html += " <img src=" + img + " alt='Sala' width='150' height='150'>";
                html += "<div class='info'>";
                html += "<p>Sala: " + s.numeroSala + "</p>";
                html += "<p>Max Personas: " + s.maxPersonas + "</p>";
                html += " <p>Ubicación:" + s.ubicacion + "</p></div></div>";
            });
            contenedorSalas.innerHTML = html;
        })
        .catch(err => console.error("Error al cargar salas:", err));
}

async function actualizarEstadoReserva() {
    if (mostrarReservaActiva === "conReservaActiva") {
        return
     }
    const fecha = inputFecha.value.trim();
    const sala = salaEnviar.value.trim();


    if (fecha !== "" && sala && sala !== "") {
        const {horas, salas} = reservasPorFecha;
        const fechaActual = new Date();

        let horariosValidos = horas.filter(hora => {
            const fechaHora = new Date(fecha + `T` + hora + `:00`);
            return fechaHora > fechaActual;
        });

        mensajeReserva.style.display = "none";
        camposHoras.style.display = "block";

        // Obtener las horariosValidos ocupadas de la sala actual
        const salaSeleccionada = salas.find(s => s.idSala == sala) || [];

        horaInicio.value = "";
        horaFin.value = "";

        const opcionInicial = document.createElement("option");
        opcionInicial.value = "";
        opcionInicial.textContent = "Selecciona hora";
        opcionInicial.selected = true;

        horaInicio.innerHTML = "";
        horaInicio.appendChild(opcionInicial);

        // Recorrer las horariosValidos y agregar solo las disponibles
        for (const hora of horariosValidos) {
            const index = horariosValidos.indexOf(hora);
            if (salaSeleccionada.disponibilidad[index]) {
                const option = document.createElement("option");
                option.value = hora;
                option.textContent = hora;
                horaInicio.appendChild(option);
            }
        }

        // Generar horas +1
        const horasMasUno = horariosValidos.map(hora => {
            const partes = hora.trim().split(":");
            const h = parseInt(partes[0], 10);
            const nuevaHora = (h + 1).toString().padStart(2, "0");
            return nuevaHora + ":00";
        });

        const opcionFinal = document.createElement("option");
        opcionFinal.value = "";
        opcionFinal.textContent = "Selecciona hora";
        opcionFinal.selected = true;

        horaFin.innerHTML = "";
        horaFin.appendChild(opcionFinal);

        // Recorrer las horas y agregar solo las disponibles
        for (const hora of horasMasUno) {
            const index = horasMasUno.indexOf(hora);
            if (salaSeleccionada.disponibilidad[index]) {
                const option = document.createElement("option");
                option.value = hora;
                option.textContent = hora;
                horaFin.appendChild(option);
            }
        }
    } else {
        mensajeReserva.style.display = "block";
        camposHoras.style.display = "none";
    }
}

async function actualizarValoresReservas() {
    if (inputFecha.value === "")
        return;
    const url = "reservas?fecha=" + encodeURIComponent(inputFecha.value);
    await fetch(url)
        .then(res => res.json())
        .then(data => {
            reservasPorFecha = data;
            cargarTablaReservas(data);
            actualizarEstadoReserva(data);
        })
        .catch(err => console.error("Error al cargar reservas:", err));
}

inputFecha.addEventListener("change", () => {

    actualizarValoresReservas();
});

// Al seleccionar una sala
function actualizarValor(idSala) {
    salaEnviar.value = idSala;

    // Quitar clase 'seleccionada' de todas las salas
    document.querySelectorAll(".sala-item").forEach(sala => {
        sala.classList.remove("seleccionada");
    });

    // Añadir clase 'seleccionada' a la sala clickeada
    const salaSeleccionada = document.getElementById(idSala);
    if (salaSeleccionada) {
        salaSeleccionada.classList.add("seleccionada");
    }

    actualizarEstadoReserva();
}

function actualizarHoraFin() {
    const valorInicio = horaInicio.value;

    Array.from(horaFin.options).forEach(opt => opt.hidden = false);
    if (!valorInicio) return;

    const hInicio = parseInt(valorInicio.split(":")[0]);
    let horaReferencia = hInicio;
    let consecutivo = true;

    Array.from(horaFin.options).forEach(opt => {
        if (!opt.value) return;
        const h = parseInt(opt.value.split(":")[0]);

        if (h <= hInicio) {
            opt.hidden = true;
            return;
        }

        if (!consecutivo) {
            opt.hidden = true;
            return;
        }

        if (h - horaReferencia === 1) {
            horaReferencia = h;
        } else if (h - horaReferencia > 1) {
            consecutivo = false;
            opt.hidden = true;
        }
    });
}

function actualizarHoraInicio() {
    const valorFin = horaFin.value;

    Array.from(horaInicio.options).forEach(opt => opt.hidden = false);
    if (!valorFin) return;

    const hFin = parseInt(valorFin.split(":")[0]);
    let horaReferencia = hFin;
    let consecutivo = true;

    Array.from(horaInicio.options).reverse().forEach(opt => {
        if (!opt.value) return;
        const h = parseInt(opt.value.split(":")[0]);

        if (h >= hFin) {
            opt.hidden = true;
            return;
        }

        if (!consecutivo) {
            opt.hidden = true;
            return;
        }

        if (horaReferencia - h === 1) {
            horaReferencia = h;
        } else if (horaReferencia - h > 1) {
            consecutivo = false;
            opt.hidden = true;
        }
    });
}

if (mostrarReservaActiva === "sinReservaActiva") {

    horaInicio.addEventListener("change", actualizarHoraFin);
    horaFin.addEventListener("change", actualizarHoraInicio);
 }


// Al cargar la página
window.addEventListener("DOMContentLoaded", () => {
    const ahora = new Date();
    const year = ahora.getFullYear();
    const month = String(ahora.getMonth() + 1).padStart(2, "0"); // meses 0-11
    const day = String(ahora.getDate()).padStart(2, "0");
    const hoy = year + `-` + month + `-` + day;

    inputFecha.value = hoy;
    inputFecha.min = hoy;

    const horaActiva = "<%= reservaActiva.getHora_in() %>";
    const horaActual = ahora.toTimeString().split(':').slice(0, 2).join(':');
    const fechaReserva = "<%= reservaActiva.getFecha_in() %>".split("T")[0];

        cargarSalas();
        initSalaItems();
    actualizarValoresReservas();
});

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
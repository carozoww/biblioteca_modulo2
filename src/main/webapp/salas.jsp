<%@ page import="models.Sala" %>
<%@ page import="models.Lector" %>
<%@ page import="models.Reserva" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Salas</title>
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>
    <style><%@include file="./WEB-INF/estilo/estilosSala.css"%></style>

</head>
<body>
<%
Lector usuario = (Lector) session.getAttribute("authUser");
if (usuario == null) {
response.sendRedirect(request.getContextPath() + "/login-lector");
return;
}
List<Sala> salasInfo = (List<Sala>) request.getAttribute("listaSalas");
Reserva reservaActiva = (Reserva) request.getAttribute("reservaActiva");
String mensaje = (String) request.getAttribute("mensaje");

String fechaFormateada = "";
if (reservaActiva != null) {
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
fechaFormateada = reservaActiva.getFecha_in().format(formatter);
}
%>

<main>
    <button class="cancelar-boton" ><a  href="dashboard" >Pagina Principal</a></button>
    <div id="mensaje" class="mensaje error"><%= mensaje %></div>
    <% if (reservaActiva != null) { %>
    <h1>Reserva sin terminar</h1>
    <div>
        <h2>Sala: <%= reservaActiva.getSala() %></h2>
        <h2>Fecha: <%= fechaFormateada %></h2>
        <h2>Horas reservadas: <%= reservaActiva.getHora_in() %> - <%= reservaActiva.getHora_fin() %> </h2>
    </div>

    <form id="realizarReserva" action="salas" method="post">
        <input type="hidden" id="finalizar-accion" name="accion">
        <button id="finalizar-boton" class="cancelar-boton" type="submit">Cancelar</button>
    </form>
    <% } else { %>
    <div>
        <h1>Seleccionar hora de Reserva:</h1>

        <p id="mensajeReserva" class="mensajeReserva">Selecciona una fecha y una sala para comenzar la reserva.</p>
        <form action="salas" method="post" id="formReserva">
            <div id="camposHoras">
                <input type="hidden" name="accion" value="reservar">
                <input type="hidden" id="fecha-enviar" name="fecha-enviar"/>
                <input type="hidden" id="sala-enviar" name="sala-enviar"/>
                <label for="hora-inicio">Hora inicio:</label><br>
                <select id="hora-inicio" name="hora-inicio" required></select>
                <br>
                <br>
                <label for="hora-fin">Hora fin:</label><br>
                <select id="hora-fin" name="hora-fin" required></select>
                <br>
                <br>
                <button type="submit">Reservar</button>
            </div>
        </form>
    </div>

    <div class="contenedorContenido">
        <% } %>
        <div>
            <h1>Horarios</h1>
            <label for="fecha">Selecciona fecha:</label>
            <input type="date" id="fecha"/>
            <br>
            <br>
            <div id="tabla-container" class="tabla-scroll"></div>
        </div>
        <% if (reservaActiva == null) { %>
        <div>
            <h1>Seleccioar sala</h1>
            <div class="contenedorSalas" id="containerSalas"></div>
        </div>
    </div>
    <% } %>
</main>

<!-- ✅ SCRIPT FINAL -->
<script>
    const inputFecha = document.getElementById("fecha");
    const contenedorReservas = document.getElementById("tabla-container");
    const contenedorSalas = document.getElementById("containerSalas");
    const horaInicio = document.getElementById("hora-inicio");
    const horaFin = document.getElementById("hora-fin");
    const mensajeReserva = document.getElementById("mensajeReserva");
    const camposHoras = document.getElementById("camposHoras");
    const salaEnviar = document.getElementById("sala-enviar");
    const fechaEnviar = document.getElementById("fecha-enviar");
    const finalizarBoton = document.getElementById("finalizar-boton");
    const finalizarAccion = document.getElementById("finalizar-accion");
    let reservasPorFecha = null;

    // Cargar lista de salas
    function cargarSalas() {
      fetch("listadoSalas")
        .then(res => res.text())
        .then(html => contenedorSalas.innerHTML = html)
        .catch(err => console.error("Error al cargar salas:", err));
    }

    // Cargar tabla reservas
    function cargarTablaReservas() {
      if (!reservasPorFecha) return;

    <% if (reservaActiva == null) { %>
      fechaEnviar.value = inputFecha.value;
    <% } %>

      const { horas, salas } = reservasPorFecha;
      let html = "<table><thead><tr><th>Hora</th>";

      salas.forEach(s => {
        html += `<th>Sala ` + s.numeroSala + `</th>`;
      });

      html += "</tr></thead><tbody>";

      horas.forEach((hora, i) => {
        html += `<tr><td>` + hora.toString() + `</td>`;
        salas.forEach(s => {
          const disponible = s.disponibilidad[i];
          html += disponible
          ? "<td class='disponible'>&#10004;</td>"
          : "<td class='no-disponible'>&#10008;</td>";
        });
        html += "</tr>";
      });

      html += "</tbody></table>";
      contenedorReservas.innerHTML = html;
    }

    async function actualizarEstadoReserva() {
    <% if (reservaActiva != null) { %>
    return
    <% } %>
      const fecha = inputFecha.value.trim();
      const sala = salaEnviar.value.trim();
      const { horas, salas } = reservasPorFecha;

      if (fecha !== "" && sala && sala !== "") {
        mensajeReserva.style.display = "none";
        camposHoras.style.display = "block";

        // Obtener las horas ocupadas de la sala actual
        const salaSeleccionada = salas.find(s => s.idSala == sala ) || [];

        horaInicio.value = "";
        horaFin .value = "";

        const opcionInicial = document.createElement("option");
        opcionInicial.value = "";
        opcionInicial.textContent = "Selecciona hora";
        opcionInicial.selected = true;

        horaInicio.innerHTML = "";
        horaInicio.appendChild(opcionInicial);

        // Recorrer las horas y agregar solo las disponibles
        for (const hora of horas) {
          const index = horas.indexOf(hora);
          if (salaSeleccionada.disponibilidad[index]) {
            const option = document.createElement("option");
            option.value = hora;
            option.textContent = hora;
            horaInicio.appendChild(option);
          }
        }

        // Generar horas +1
        const horasMasUno = horas.map(hora => {
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
        .then(data => {reservasPorFecha = data; cargarTablaReservas(data); actualizarEstadoReserva(data);})
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
    <% if (reservaActiva == null) { %>

    horaInicio.addEventListener("change", actualizarHoraFin);
    horaFin.addEventListener("change", actualizarHoraInicio);
    <% } %>

    function mostrarMensaje() {
        const mensaje = document.getElementById("mensaje");
        mensaje.style.display = "block";

        // Ocultar después de 10 segundos
        setTimeout(() => {
            mensaje.style.display = "none";
        }, 10000);
    }

    // Al cargar la página
    window.addEventListener("DOMContentLoaded", () => {
      const ahora = new Date();
      const hoy = ahora.toISOString().split("T")[0];
      inputFecha.value = hoy;
      inputFecha.min = hoy;

    <% if (mensaje != null) { %>
      mostrarMensaje();
    <% } %>

    <% if (reservaActiva != null) { %>
      const horaActiva = "<%= reservaActiva.getHora_in() %>";
      const horaActual = ahora.toTimeString().split(':').slice(0,2).join(':');
      const fechaReserva = "<%= reservaActiva.getFecha_in() %>".split("T")[0];

      if (fechaReserva == hoy && horaActual < horaActiva) {
        finalizarAccion.value = "cancelar"
        finalizarBoton.textContent = "Cancelar";
      } else {
        finalizarAccion.value = "terminar"
        finalizarBoton.textContent = "Terminar";
      }
    <% } else { %>
        cargarSalas();
    <% } %>
      actualizarValoresReservas();
    });
</script>
</body>
</html>

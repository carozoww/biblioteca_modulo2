<%@ page import="models.Reserva" %>
<%@ page import="models.Lector" %>
<%@ page import="models.Reserva" %>
<%@ page import="models.Administrador" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  List<String> generos = (List<String>) request.getAttribute("listaGeneros");
    Administrador admin = (Administrador) session.getAttribute("authAdmin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/login-admin");
        return;
    }%>

<html>
<head>
    <title>Reservas</title>
    <style><%@include file="./WEB-INF/estilo/otrocss.css"%></style>
    <style><%@include file="./WEB-INF/estilo/estilosAdministradorReserva.css"%></style>
</head>
<body>
<nav>
    <div id="logoynombre">
        <button onclick="abrirBarra()" id="botton-barra"> click</button>
        <img src="imgs/logo.jpg" width="100px" height="100px">
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_medio">
        <a href="">Generos</a>
        <a href="">Autores</a>
        <a href="">Editoriales</a>
        <a href="">Salas</a>
    </div>
    <div id="elementos_derecha">
        <a href="">Cuenta</a>
        <a href="logout">Cerrar sesion</a>
    </div>
</nav>
<aside id="sidebar">
    <div id="columna_contenido">
        <div>
            <img src="imgs/iconouser.png" alt="" width="50px" height="50px">
            <a href="">Lectores</a>
        </div>
        <div>
            <img src="imgs/resenia.png" alt="" width="50px" height="50px">
            <a href="">Reseñas</a>
        </div>
        <div>
            <img src="imgs/prestamo.png" alt="" width="50px" height="50px">
            <a href="">Prestamos</a>
        </div>
        <div>
            <img src="imgs/reserva.png" alt="" width="50px" height="50px">
            <a href="">Reservas</a>
        </div>
    </div>
</aside>
<main>
    <div class="form-container">
        <h2 id="textoAccion">Gestionar reservas</h2>
        <form id="editarReserva" action="administradorReservas" method="post">
            <input type="hidden" id="accion" name="accion" value="agregar">
            <input type="hidden" id="id-reserva" name="id-reserva">
            <div class="form-group">
                <label for="id-sala">Id Sala:</label>
                <select id="id-sala" name="id-sala" required></select>
            </div>
            <div class="form-group">
                <label for="id-lector">Id Lector:</label>
                <select id="id-lector" name="id-lector" required></select>
            </div>
            <div class="form-group">
                <label for="fecha">Fecha:</label>
                <input id="fecha" type="date" name="fecha" required>
            </div>
            <div class="time-row">
                <div class="form-group">
                    <label for="hora-inicio">Inicio:</label>
                    <input id="hora-inicio" type="time" name="hora-inicio" required>
                </div>
                <div class="form-group">
                    <label for="hora-fin">Fin:</label>
                    <input id="hora-fin" type="time" name="hora-fin" required>
                </div>
            </div>
            <div class="form-group">
                <label for="estado">Estado:</label>
                <select id="estado" name="estado" required>
                    <option value="">Seleccionar...</option>
                    <option value="FINALIZADA">FINALIZADA</option>
                    <option value="RESERVADA">RESERVADA</option>
                    <option value="CANCELADA">CANCELADA</option>
                </select>
            </div>
            <div class="form-buttons">
                <button id="boton" class="btn-agregar" action="submit">Agregar</button>
                <button id="cancelar" class="btn-cancelar hidden" type="button">Cancelar</button>
            </div>
        </form>
    </div>
    <div>
        <br>
        <h1>Seleccionar reserva</h1>
        <div id="contenedor-tabla" class="contenedor-tabla">
            <table>
                <thead>
                <tr>
                    <th>Sala</th>
                    <th>Lector</th>
                    <th>Inicio</th>
                    <th>Fin</th>
                    <th>Estado</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
    <div id="msg-container"></div>
</main>
<footer>

</footer>
<%
String mensaje = (String) session.getAttribute("mensaje");
if (mensaje != null)
session.removeAttribute("mensaje");
%>

<script>
    const boton = document.getElementById("boton");
    const cancelar = document.getElementById("cancelar");
    const accion = document.getElementById("accion");

    const idReserva = document.getElementById("id-reserva");
    const idSala = document.getElementById("id-sala");
    const idLector = document.getElementById("id-lector");
    const estado = document.getElementById("estado");

    const fecha = document.getElementById("fecha");
    const horaInicial = document.getElementById("hora-inicio");
    const horaFinal = document.getElementById("hora-fin");

    var datosReservas = null;
    var datosSalas = null;
    var datosLectores = null;

    function modificarReservaCampos(reservaId) {
        const datos = datosReservas.filter(reserva => reserva.id_Reserva === reservaId);
        idReserva.value = datos[0].id_Reserva || "";
        idSala.value = datos[0].id_sala || "";
        idLector.value = datos[0].id_usuario || "";
        estado.value = datos[0].estado || "";

        const [fechaInicio, horaInicioCompleta] = datos[0].fecha_in.split("T");
        const horaInicio = horaInicioCompleta.substring(0,5);

        const [fechaFin, horaFinCompleta] = datos[0].fecha_fin.split("T");
        const horaFin = horaFinCompleta.substring(0,5);

        fecha.value = fechaInicio || "";
        horaInicial.value = horaInicio || "";
        horaFinal.value = horaFin || "";

        cancelar.classList.remove("hidden");
        boton.className = "btn-modificar";
        boton.innerText = "Actualizar";
        accion.value = "editar";

        window.scrollTo({
          top: 0,
          behavior: "smooth"
        });
    }

    cancelar.addEventListener('click', (e) => {
        cancelar.classList.add("hidden");
        boton.className = "btn-agregar";
        boton.innerText = "Agregar";
        accion.value = "agregar";

        idReserva.value = "";
        idSala.value = "";
        idLector.value = "";
        estado.value = "";
        fecha.value = "";
        horaInicial.value = "";
        horaFinal.value = "";
    });

    fetch("administradorReservas?cargarReservas=true")
    .then(res => res.json())
    .then(data => {
          console.log(data);
        datosReservas = data.reservas;
        datosSalas = data.salas;
        datosLectores = data.lectores;
        cargarTabla();
        salaSelect();
        lectorSelect();
        })
    .catch(err => console.error("Error cargando tabla:", err));

    function cargarTabla() {
        const tbody = document.querySelector("#contenedor-tabla table tbody");
        tbody.innerHTML = ""; // limpiar por si acaso
        datosReservas.forEach(r => {
            const [fechaInicio, horaInicioCompleta] = r.fecha_in.split("T");
            const horaInicio = horaInicioCompleta.substring(0,5);

            const [fechaFin, horaFinCompleta] = r.fecha_fin.split("T");
            const horaFin = horaFinCompleta.substring(0,5);

            var row = `<tr><td data-label="Sala">` + r.sala + `</td>
                    <td data-label="Lector">` + r.lector + `</td>
                    <td data-label="Fin">` + fechaInicio + ` ` + horaInicio + `</td>
                    <td data-label="Inicio">` + fechaFin + ` ` + horaFin + `</td>
                    <td data-label="Estado">` + r.estado + `</td>
                    <td><button class= "btn-modificar"; onclick="modificarReservaCampos(` + r.id_Reserva + `)">Modificar </button></td>
                </tr>`;
                tbody.innerHTML += row;
            });
    }

    function showMensaje(message, duration = 3000) {
      const container = document.getElementById('msg-container');

      const msg = document.createElement('div');
      msg.className = 'msg';
      msg.innerText = message;

      container.appendChild(msg);

      setTimeout(() => msg.classList.add('show'), 100);

      setTimeout(() => {
        msg.classList.remove('show');
        setTimeout(() => container.removeChild(msg), 500);
      }, duration);
    }

      window.addEventListener('DOMContentLoaded', () => {
      const mensaje = "<%= mensaje %>";

      if (mensaje && mensaje !== "null" && mensaje.trim() !== "") {
          showMensaje(mensaje);
      }
      });

    function salaSelect() {
        idSala.innerHTML = "";

        const option = document.createElement("option");
        option.value = "";
        option.textContent = "Seleccione una opción";
        idSala.appendChild(option);

        datosSalas.sort((a, b) => Number(a.numeroSala) - Number(b.numeroSala))
        .forEach(s => {
            const option = document.createElement("option");
            option.value = s.idSala;
            option.textContent = s.numeroSala;
            idSala.appendChild(option);
        });
    }

    function lectorSelect() {
        idLector.innerHTML = "";

        const option = document.createElement("option");
        option.value = "";
        option.textContent = "Seleccione una opción";
        idLector.appendChild(option);

        datosLectores.sort((a, b) => a.nombre.localeCompare(b.nombre))
        .forEach(l => {
            const option = document.createElement("option");
            option.value = l.ID;
            option.textContent = l.nombre;
            idLector.appendChild(option);
        });
    }
</script>
</body>
</html>

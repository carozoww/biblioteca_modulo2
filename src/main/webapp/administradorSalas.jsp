<%@ page import="models.Sala" %>
<%@ page import="models.Lector" %>
<%@ page import="models.Reserva" %>
<%@ page import="models.Administrador" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
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
    <title>Salas</title>
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
        <h2 id="textoAccion">Gestionar salas</h2>
        <div style="display: flex; align-items: flex-start; gap: 20px;">
            <img id="imagenSeleccionada" src='imgs/room.png' alt='Sala' width='150' height='150'>
            <form style="flex: 1;" id="editarSala" action="administradorSalas" method="post">
                <input type="hidden" id="accion" name="accion" value="agregar">
                <input type="hidden" id="id-sala" name="id-sala" required>
                <div class="form-group">
                    <label for="imagen-url">URL:</label>
                    <input id="imagen-url" name="imagen-url" required>
                </div>
                <div class="form-group">
                    <label for="numero-sala">Numero Sala:</label>
                    <input type="number" id="numero-sala" name="numero-sala" required>
                </div>
                <div class="form-group">
                    <label for="ubicacion">Ubicacion:</label>
                    <input id="ubicacion" name="ubicacion" required>
                </div>
                <div class="form-group">
                    <label for="max-personas">Maximo Personas:</label>
                    <input type="number" id="max-personas" name="max-personas" required>
                </div>
                <div class="form-buttons">
                    <button id="boton" class="btn-agregar" action="submit">Agregar</button>
                    <button id="cancelar" class="btn-cancelar hidden" type="button">Cancelar</button>
                </div>
            </form>
        </div>
    </div>
    <div>
        <br>
        <h1>Seleccionar sala</h1>
        <div id="contenedor-tabla" class="contenedor-tabla">
            <table>
                <thead>
                <tr>
                    <th>Imagen</th>
                    <th>Numero Sala</th>
                    <th>Ubicacion</th>
                    <th>Max Personas</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
    <form id="eliminarSala" action="administradorSalas" method="post">
        <input type="hidden" name="accion" value="eliminar">
        <input type="hidden" id="eliminar-sala" name="eliminar-sala">
    </form>
</main>
<footer>

</footer>
<script>
    var datosSalas = null;

    const accion = document.getElementById("accion");

    const idSala = document.getElementById("id-sala");
    const urlImg = document.getElementById("imagen-url") ;
    const numSala = document.getElementById("numero-sala");
    const ubicacion = document.getElementById("ubicacion");
    const maxPersona = document.getElementById("max-personas");

    const eliminarSalaForm = document.getElementById("eliminarSala");
    const eliminarSala = document.getElementById("eliminar-sala");

    const imagenSeleccionada = document.getElementById("imagenSeleccionada");

    const cancelar = document.getElementById("cancelar");
    const boton = document.getElementById("boton");

    function editarSalaCampos(salaId) {
        const datos = datosSalas.filter(sala => sala.idSala === salaId);
        idSala.value = datos[0].idSala || "";
        urlImg.value = datos[0].imagen || "";
        numSala.value = datos[0].numeroSala || "";
        ubicacion.value = datos[0].ubicacion || "";
        maxPersona.value = datos[0].maxPersonas || "";

        if (!datos[0].imagen || datos[0].imagen.trim() === "") {
            imagenSeleccionada.src = "imgs/room.png" ;
        } else {
            imagenSeleccionada.src = datos[0].imagen ;
        }

        cancelar.classList.remove("hidden");
        boton.className = "btn-modificar";
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
        boton.innerText = "Agregar";
        accion.value = "agregar";

        idSala.value = "";
        urlImg.value = "";
        numSala.value = "";
        ubicacion.value = "";
        maxPersona.value = "";
    });

        fetch("administradorSalas?cargarSalas=true")
        .then(res => res.json())
        .then(data => {
            const tbody = document.querySelector("#contenedor-tabla table tbody");
            tbody.innerHTML = ""; // limpiar por si acaso
            datosSalas = data;
            data.forEach(s => {
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
            })
        .catch(err => console.error("Error cargando tabla:", err));
</script>
</body>
</html>

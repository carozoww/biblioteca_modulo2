<%@ page import="models.Libro, models.Autor, dao.LibroDAO, dao.LibroAutorDAO, dao.PrestamoDAO" %>
<%@ page import="models.Lector" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.EditorialDAO" %>
<%@ page import="models.Editorial" %>
<%@ page import="java.util.Collections" %>
<%
    int idLibro = Integer.parseInt(request.getParameter("id"));
    PrestamoDAO prestamoDAO = new PrestamoDAO();
    LibroDAO libroDAO = new LibroDAO();
    LibroAutorDAO libroAutorDAO = new LibroAutorDAO();
    EditorialDAO editorial = new EditorialDAO();

    // Obtener sesión e idLector
    Lector usuario = (Lector) session.getAttribute("authUser");
    if(usuario == null){
        response.sendRedirect("login.jsp");
        return;
    }
    int idLector = usuario.getID();

    // Obtener libro, autores y editorial
    Libro libro = libroDAO.buscarPorId(idLibro);
    List<Autor> autores = libroAutorDAO.listarAutoresDeUnLibro(idLibro);
    int idE = libro.getIdEditorial();
    List<Editorial> editoriales = Collections.singletonList(editorial.buscarEditorialPorId(idE));

    boolean tienePrestamo = prestamoDAO.prestamoActivoPorLector(idLector);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><%= libro.getTitulo() %> - Detalles</title>
    <style><%@include file="./WEB-INF/estilo/estilosDetalleLibro.css"%></style>
</head>
<body>

<nav id="navbar">
    <div id="logoynombre">
        <a href="dashboard">
            <img src="imgs/logo.jpg" width="100" height="100" alt="Logo Biblio-Tech-a">
        </a>
        <h1>Biblio-Tech-a</h1>
    </div>
    <div id="elementos_derecha">
        <a href="perfil">Cuenta</a>
        <a href="logout">Cerrar sesión</a>
    </div>
</nav>

<main id="detalle-libro">
    <div class="container">
        <h2 class="titulo-libro"><%= libro.getTitulo() %></h2>

        <div class="info-libro">
            <p><span class="label">Autores:</span>
                <% for(Autor a : autores){ %>
                <%= a.getNombre() %> <%= a.getApellido() %><% if(autores.indexOf(a) < autores.size() - 1){ %>, <% } %>
                <% } %>
            </p>

            <p><span class="label">Editorial:</span>
                <% for(Editorial e : editoriales){ %>
                <%= e.getNombre() %>
                <% } %>
            </p>

            <p><span class="label">ISBN:</span> <%= libro.getIsbn() %></p>
            <p><span class="label">Fecha de publicación:</span> <%= libro.getFechaPublicacion() %></p>
        </div>

        <div class="acciones">
            <form action="prestamos" method="post" class="form-reservar">
                <input type="hidden" name="accion" value="reservar">
                <input type="hidden" name="idLibro" value="<%= libro.getIdLibro() %>">

                <%
                    boolean puedeReservar = usuario.isAutenticacion() && !tienePrestamo;
                    String textoBoton;
                    if (!usuario.isAutenticacion()) {
                        textoBoton = "No estás autenticado";
                    } else if (tienePrestamo) {
                        textoBoton = "Ya tenés un préstamo";
                    } else {
                        textoBoton = "Reservar";
                    }
                %>

                <button type="submit"
                        class="<%= puedeReservar ? "btn-reservar" : "btn-disabled" %>"
                        <%= puedeReservar ? "" : "disabled" %>>
                    <%= textoBoton %>
                </button>
            </form>

            <a href="dashboard" class="btn-volver">Volver al inicio</a>
        </div>
    </div>
</main>

</body>
</html>

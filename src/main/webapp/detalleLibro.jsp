<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="models.*" %>
<%@ page import="dao.*" %>
<%
    int idLibro = Integer.parseInt(request.getParameter("id"));
    PrestamoDAO prestamoDAO = new PrestamoDAO();
    LibroDAO libroDAO = new LibroDAO();
    LibroAutorDAO libroAutorDAO = new LibroAutorDAO();
    LibroGeneroDAO librogenerodao = new LibroGeneroDAO();
    EditorialDAO editorial = new EditorialDAO();
    PenalizacionDAO penadao = new PenalizacionDAO();
    List<Genero> generos = librogenerodao.listarGenerosDeLibro(idLibro);

    // Obtener sesión e idLector
    Lector usuario = (Lector) session.getAttribute("authUser");
    if(usuario == null){
        response.sendRedirect("login.jsp");
        return;
    }
    int idLector = usuario.getID();

    // Obtener libro, autores y editorial
    Libro libro = libroDAO.buscarLibroPorID(idLibro);
    List<Autor> autores = libroAutorDAO.listarAutoresDeUnLibro(idLibro);
    int idE = libro.getIdEditorial();
    List<Editorial> editoriales = Collections.singletonList(editorial.buscarEditorialPorId(idE));

    boolean tienePrestamo = prestamoDAO.prestamoActivoPorLector(idLector);
    boolean estaPenalizado = penadao.tienePenalizacionActiva(idLector);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><%= libro.getTitulo() %> - Detalles</title>
    <style><%@ include file="./WEB-INF/estilo/estilosDetalleLibro.css" %></style>
</head>
<body>

<nav id="navbar">
    <div id="logoynombre">
        <a href="dashboard">
            <img src="imgs/logo.jpg" alt="Logo Biblio-Tech-a">
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

        <div class="libro-header">
            <img src="<%=libro.getImagen_url()!=null ? libro.getImagen_url() : "imgs/libro.jpg"%>" class="imagen">

            <div class="info-libro">
                <p><b>Número de páginas:</b> <%= libro.getNumpaginas() %></p>

                <p><b>Autores:</b>
                    <% if(!autores.isEmpty()){
                        for(Autor a : autores){ %>
                    <%= a.getNombre() %> <%= a.getApellido() %><% if(autores.indexOf(a) < autores.size() - 1){ %>, <% } %>
                    <% }
                    } else { %>
                    Sin autores asignados
                    <% } %>
                </p>

                <p><b>Géneros:</b>
                    <% if(!generos.isEmpty()){
                        for(Genero g : generos){ %>
                    <%= g.getNombre() %><% if(generos.indexOf(g) < generos.size() - 1){ %>, <% } %>
                    <% }
                    } else { %>
                    Sin géneros asignados
                    <% } %>
                </p>

                <p><b>Editorial:</b>
                    <% for(Editorial e : editoriales){ %>
                    <%= e.getNombre() %>
                    <% } %>
                </p>

                <p><b>ISBN:</b> <%= libro.getIsbn() %></p>
                <p><b>Fecha de publicación:</b> <%= libro.getFechaPublicacion() %></p>

                <div style="max-height:150px; overflow-y:auto; margin-top:10px;">
                    <p><b>Sinopsis:</b> <%= libro.getSinopsis() %></p>
                </div>
            </div>
        </div>

        <div class="acciones">
            <form action="prestamos" method="post" class="form-reservar">
                <input type="hidden" name="accion" value="reservar">
                <input type="hidden" name="idLibro" value="<%= libro.getIdLibro() %>">

                <%
                    boolean puedeReservar = usuario.isAutenticacion() && !tienePrestamo && !estaPenalizado;
                    String textoBoton;
                    if (!usuario.isAutenticacion()) {
                        textoBoton = "No estás autenticado";
                    } else if (tienePrestamo) {
                        textoBoton = "Ya tenés un préstamo";
                    } else if(estaPenalizado){
                        textoBoton = "Estás penalizado";
                    } else{
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

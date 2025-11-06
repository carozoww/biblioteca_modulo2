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

    boolean autenticado = usuario.isAutenticacion();
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
        <%if(libro.getImagen_url() != null){ %>
        <img src="<%=libro.getImagen_url()%>" width="300" heigth="375px" class="imagen">
        <%}else{%>
        <img src="imgs/libro.jpg" width="300" heigth="375px" class="imagen">
        <%}%>
        <div class="info-libro">
            <p><span class="label"> <b>Número de páginas:</b></span> <%=libro.getNumpaginas()%></p>

            <p><span class="label"><b>Autores:</b></span>

                <% if(!autores.isEmpty()){
                    for(Autor a : autores){ %>
                <%= a.getNombre() %> <%= a.getApellido() %><% if(autores.indexOf(a) < autores.size() - 1){ %>, <% } %>
                <%
                    }
                }else{%>
                Sin autores asignados
                <%}
                %>
            </p>

            <p><span class="label"><b>Generos:</b></span>

                <% if(!generos.isEmpty()){
                    for(Genero g : generos){ %>
                <%= g.getNombre() %> <% if(generos.indexOf(g) < generos.size() - 1){ %>, <% } %>
                <%
                    }
                }else{%>
                Sin generos asignados
                <%}    %>
            </p>

            <p><span class="label"> <b>Editorial:</b></span>
                <% for(Editorial e : editoriales){ %>
                <%= e.getNombre() %>
                <% } %>
            </p>

            <p><span class="label"><b>ISBN:</b></span> <%= libro.getIsbn() %></p>
            <p><span class="label"><b>Fecha de publicación:</b></span> <%= libro.getFechaPublicacion() %></p>
            <div>
                <label><b>Sinopsis:</b></label>
                <p><%=libro.getSinopsis()%> </p>
            </div>

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

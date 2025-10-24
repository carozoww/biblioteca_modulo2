<%@ page import="models.Libro, models.Autor, dao.LibroDAO, dao.LibroAutorDAO, dao.PrestamoDAO" %>
<%@ page import="models.Lector" %>
<%@ page import="java.util.List" %>
<%
    int idLibro = Integer.parseInt(request.getParameter("id"));
    PrestamoDAO prestamoDAO = new PrestamoDAO();
    LibroDAO libroDAO = new LibroDAO();
    LibroAutorDAO libroAutorDAO = new LibroAutorDAO();

    // Obtener sesión y idLector
    Lector usuario = (Lector) session.getAttribute("authUser");
    if(usuario == null){
        response.sendRedirect("login.jsp");
        return;
    }
    int idLector = usuario.getID();

    // Obtener libro y autores
    Libro libro = libroDAO.buscarPorId(idLibro);
    List<Autor> autores = libroAutorDAO.listarAutoresDeUnLibro(idLibro);

    boolean tienePrestamo = prestamoDAO.prestamoActivoPorLector(idLector);
%>

<h1><%= libro.getTitulo() %></h1>
<p><b>Autores:</b>
    <% for(Autor a : autores){ %>
    <%= a.getNombre() %> <%= a.getApellido() %>,
    <% } %>
</p>
<p><b>ISBN:</b> <%= libro.getIsbn() %></p>
<p><b>Fecha publicación:</b> <%= libro.getFechaPublicacion() %></p>

<form action="prestamos" method="post">
    <input type="hidden" name="accion" value="reservar">
    <input type="hidden" name="idLibro" value="<%= libro.getIdLibro() %>">
    <button type="submit" <%= tienePrestamo ? "disabled style='color:gray;'" : "" %>>
        <%= tienePrestamo ? "No disponible" : "Reservar" %>
    </button>
</form>

<a href="dashboard.jsp">← Volver al catálogo</a>

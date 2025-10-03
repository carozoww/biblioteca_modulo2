package models;

public class LibroAutor {

    private  Libro libro;
    private  Autor autor;

    public LibroAutor(Libro libro, Autor autor){
        this.libro = libro;
        this.autor = autor;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public void mostrarInformacion(){
        System.out.printf("%-20s %-20s%n", getLibro(), getAutor());
    }
}

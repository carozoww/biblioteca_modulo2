package models;

public class LibroGenero {
    private Libro libro;
    private Genero genero;


    public LibroGenero(Libro lib, Genero gen){
        this.libro = lib;
        this.genero = gen;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public void mostrarInformacion(){
        System.out.printf("%-20s %-20s%n", getLibro(), getGenero());
    }
}

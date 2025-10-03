package models;

public class Autor {
    private int id_autor;
    private String nombre;
    private String apellido;

    public Autor(int id, String nom, String ape){
        this.id_autor = id;
        this.nombre = nom;
        this.apellido = ape;
    }

    public int getId_autor() {
        return id_autor;
    }

    public void setId_autor(int id_autor) {
        this.id_autor = id_autor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void mostrarInfo(){
        System.out.printf("%-5d %-20s %-20s%n",
            getId_autor(),
            getNombre(),
            getApellido());
    }
}

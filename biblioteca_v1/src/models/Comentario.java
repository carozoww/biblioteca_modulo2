package models;

public class Comentario {
    private int id_comentario;
    private String contenido;

    public Comentario(int id_comentario, String contenido) {
        this.id_comentario = id_comentario;
        this.contenido = contenido;
    }

    public int getId_comentario() {return id_comentario;}
    public String getContenido() {return contenido;}

    public void setContenido(String contenido) {this.contenido = contenido;}
    public void setId_comentario(int id_comentario) {this.id_comentario = id_comentario;}



}

package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Reserva {
    private int id_Reserva;
    private LocalDateTime fecha_in;
    private LocalDateTime fecha_fin;
    private int id_sala;
    private int id_usuario;
    private String estado;
    private String lector;
    private int sala;

    public Reserva(int id_Reserva, LocalDateTime fecha_in, LocalDateTime fecha_fin, int id_sala, int id_usuario, String estado,  String lector, int sala) {
        this.id_Reserva = id_Reserva;
        this.fecha_in = fecha_in;
        this.fecha_fin = fecha_fin;
        this.id_sala = id_sala;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.lector = lector;
        this.sala = sala;
    }

    public int getId_Reserva() {return id_Reserva;}

    public LocalDateTime getFecha_in() {return fecha_in;}

    public LocalDateTime getFecha_fin() {return fecha_fin;}

    public int getId_sala() {return id_sala;}

    public int getId_usuario() {return id_usuario;}

    public String getEstado() {return estado;}

    public String getLector() {return lector;}
    public int getSala() {return sala;}

    public void mostrarInformacion(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.printf("%-5d %-20s %-20s %-20s %-5d %-20s %-5d %-20s %-10s%n",
                getId_Reserva(),
                getFecha_in().format(formatter),
                getFecha_fin()!= null ? getFecha_fin().format(formatter) : "-",
                getId_sala(),
                getSala(),
                getId_usuario(),
                getLector(),
                getEstado());

    }
}

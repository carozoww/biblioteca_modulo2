package models;

import java.time.LocalDate;
import java.util.Date;

public class Administrador {
    private int  id;
    private String nombre;
    private String correo;
    private Date fechaNacimiento;
    private String contrasenia;

    public Administrador(int id, String nombre, String correo, Date fechaNacimiento,String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.contrasenia = contrasenia;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    public String getContrasenia() {return contrasenia;}


    public void setId(int id) {
    this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setFechaNacimiento(Date fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
    public void setContrasenia(String contrasenia) {this.contrasenia = contrasenia;}

    public void mostrarInfo(){
        System.out.printf("%-5d %-10s %-30s %-30s %-20s%n",
                getId(),
                getNombre(),
                getFechaNacimiento(),
                getCorreo(),
                getContrasenia());

        }

}

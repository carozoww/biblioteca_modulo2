package models;

import java.time.LocalDate;

public class Lector {
    private int ID;
    private String nombre;
    private String cedula;
    private String telefono;
    private String direccion;
    private boolean autenticacion;
    private LocalDate fechaNac;
    private boolean membresia;
    private String correo;
    private String contrasenia;

    public Lector() {
    }

    public Lector(int ID, String nombre, String cedula, String telefono, String direccion, boolean autenticacion,
                  LocalDate fechaNac, boolean membresia, String correo, String contrasenia) {
        this.ID = ID;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.autenticacion = autenticacion;
        this.fechaNac = fechaNac;
        this.membresia = membresia;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    public Lector(String nombre, String cedula, String telefono, String direccion, boolean autenticacion,
                  LocalDate fechaNac, boolean membresia, String correo, String contrasenia) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.autenticacion = autenticacion;
        this.fechaNac = fechaNac;
        this.membresia = membresia;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }


    public Lector(int ID, String nombre, String cedula, String telefono, String direccion,
                  String autenticacionStr, String fechaNacStr, String membresiaStr, String correo) {
        this.ID = ID;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.autenticacion = Boolean.parseBoolean(autenticacionStr); // "true" o "false"
        this.fechaNac = LocalDate.parse(fechaNacStr); // YYYY-MM-DD
        this.membresia = Boolean.parseBoolean(membresiaStr);
        this.correo = correo;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(boolean autenticacion) {
        this.autenticacion = autenticacion;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public boolean isMembresia() {
        return membresia;
    }

    public void setMembresia(boolean membresia) {
        this.membresia = membresia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void mostrarInformacion() {
        System.out.printf("%-5d %-20s %-15s %-15s %-20s %-12s %-12s %-8s %-25s%n",
                getID(),
                getNombre(),
                getCedula(),
                getTelefono(),
                getDireccion(),
                isAutenticacion(),
                getFechaNac(),
                isMembresia(),
                getCorreo());
    }
}



package models;

public class Sala {
    private int idSala;
    private int numeroSala;
    private String ubicacion;
    private int maxPersonas;

    public Sala(int idSala, int numeroSala, String ubicacion, int maxPersonas) {
        this.idSala = idSala;
        this.numeroSala = numeroSala;
        this.ubicacion = ubicacion;
        this.maxPersonas = maxPersonas;
    }

    public int getIdSala() {
        return idSala;
    }

    public int getNumeroSala() {return numeroSala;}

    public String getUbicacion() {
        return ubicacion;
    }

    public int getMaxPersonas() {
        return maxPersonas;
    }

    public void mostrarInformacion(){
        System.out.printf("%-5d %-20s %-20s %-20s%n",
                getIdSala(),
                getNumeroSala(),
                getUbicacion(),
                getMaxPersonas());
    }

}

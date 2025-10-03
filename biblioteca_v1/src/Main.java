import basedatos.conexion;
import flujos.menu;

import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            conexion.getInstancia().getConnection();
            System.out.println("base de datos funcionando");
        }catch(SQLException e){
            System.out.println("error de conexion: " + e.getMessage());
            return;
        }

        menu um = new menu();
        Scanner sc = new Scanner(System.in);

        boolean sesionOK = um.iniciarSesion(sc);

        if(sesionOK) {
            um.mostrarMenu();
        }
        else{
            System.out.printf("No se pudo iniciar sesion");

        }
    }
}
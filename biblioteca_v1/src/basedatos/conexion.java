package basedatos;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class conexion {
    private static conexion instancia;
    private Connection connection;
    private static Properties reader = new Properties();

    static{

        try{
            InputStream input = conexion.class.getResourceAsStream("/db.properties");
            reader.load(input);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private conexion() throws SQLException {
        try{
            this.connection = DriverManager.getConnection(reader.getProperty("db.url"), reader.getProperty("db.user"), reader.getProperty("db.password"));
            System.out.println("Conexion establecida!");
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    public static conexion getInstancia() throws SQLException{
        if(instancia != null){

            return instancia;
        }else{
            instancia = new conexion();
            return instancia;
        }
    }

    public Connection getConnection() throws SQLException {
//        if(connection != null || connection.isClosed()){
//            String url = reader.getProperty("db.url");
//            String user = reader.getProperty("db.user");
//            String password = reader.getProperty("db.password");
//            connection = DriverManager.getConnection(url,user,password);
//        }
        return connection;
    }


}
